package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.ExportKind;
import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.common.shared.dto.tasks.TaskStatus;
import org.consumersunion.stories.common.shared.dto.tasks.TaskType;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;

@Component
public class TaskPersister {
    private final PersistenceService persistenceService;

    @Inject
    TaskPersister(
            PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public <T extends Task> T create(T task) {
        return persistenceService.process(new CreateTaskFunc<T>(task, this));
    }

    public <T extends Task> T get(int id) {
        return (T) persistenceService.process(createRetrieveFunc(id));
    }

    public <T extends Task> T get(int id, Connection conn) {
        return (T) persistenceService.process(conn, createRetrieveFunc(id));
    }

    public void update(Task task) {
        persistenceService.process(createConcreteUpdate(task));
    }

    private <T extends Task> ProcessFunc<Integer, T> createRetrieveFunc(int id) {
        return new RetrieveTaskFunc<T>(id, this);
    }

    public void changeStatus(Task task, final TaskStatus status) {
        persistenceService.process(createChangeStatusFunc(task, status));
    }

    public void setTotal(Task task) {
        persistenceService.process(new ProcessFunc<Task, Void>(task) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update =
                            conn.prepareStatement("UPDATE task SET total=?, lastModified=NOW() WHERE id=?");

                    update.setInt(1, input.getTotal());
                    update.setInt(2, input.getId());

                    int updateCount = update.executeUpdate();

                    if (updateCount != 1) {
                        throw new GeneralException("Failed update of task total : " + input.getId());
                    }

                    checkIfCanceled(input, conn);

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void updateCount(Task task) {
        persistenceService.process(new ProcessFunc<Task, Void>(task) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update =
                            conn.prepareStatement("UPDATE task SET count=?, lastModified=NOW() WHERE id=?");

                    update.setInt(1, input.getCount());
                    update.setInt(2, input.getId());

                    int updateCount = update.executeUpdate();

                    if (updateCount != 1) {
                        throw new GeneralException("Failed update of task count : " + input.getId());
                    }

                    checkIfCanceled(input, conn);

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    private ProcessFunc<Task, Void> createChangeStatusFunc(Task task, final TaskStatus status) {
        return new ProcessFunc<Task, Void>(task) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update =
                            conn.prepareStatement("UPDATE task SET status=?, lastModified=NOW() WHERE id=?");

                    update.setString(1, status.name());
                    update.setInt(2, input.getId());

                    int updateCount = update.executeUpdate();
                    if (updateCount != 1) {
                        throw new GeneralException("Failed update of task status : " + input.getId());
                    }

                    input.setStatus(status);
                    checkIfCanceled(input, conn);

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        };
    }

    private void checkIfCanceled(Task task, Connection conn) throws SQLException {
        Task savedTask = get(task.getId(), conn);

        if (!task.isCanceling() && savedTask.isCanceling()) {
            persistenceService.process(conn, createChangeStatusFunc(task, TaskStatus.CANCELED));
        }

        if (task.isCanceled()) {
            conn.commit();
            throw new GeneralException("Task was canceled");
        }
    }

    private static class CreateTaskFunc<T extends Task> extends ProcessFunc<T, T> {
        private final TaskPersister taskPersister;

        public CreateTaskFunc(T task, TaskPersister taskPersister) {
            super(task);

            this.taskPersister = taskPersister;
        }

        @Override
        public T process() {
            try {
                return taskPersister.createConcreteCreate(input, conn);
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private static class RetrieveTaskFunc<T extends Task> extends ProcessFunc<Integer, T> {
        private final TaskPersister taskPersister;

        public RetrieveTaskFunc(int id, TaskPersister taskPersister) {
            super(id);

            this.taskPersister = taskPersister;
        }

        @Override
        public T process() {
            try {
                PreparedStatement selectTask =
                        conn.prepareStatement("SELECT id, profile, type, status, count, total, created, lastModified " +
                                "FROM task " +
                                "WHERE id=?");

                selectTask.setInt(1, input);

                ResultSet rs = selectTask.executeQuery();

                T task = null;
                if (rs.next()) {
                    task = taskPersister.createConcreteRetrieve(rs, conn);
                }

                return task;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    private <T extends Task> ProcessFunc<? extends Task, ? extends Task> createConcreteUpdate(T task) {
        ProcessFunc<? extends Task, ? extends Task> func;
        switch (task.getTaskType()) {
            case EXPORT:
                func = new UpdateExportTask((ExportTask) task);
                break;
            default:
                throw new GeneralException(task.getTaskType() + " has no UpdateFunc");
        }

        return func;
    }

    private <T extends Task> T createConcreteCreate(T task, Connection conn) throws SQLException {
        ProcessFunc<? extends Task, ? extends Task> func;
        switch (task.getTaskType()) {
            case EXPORT:
                func = new CreateExportTask((ExportTask) task);
                break;
            case ADD_STORIES:
                func = new CreateAddStoriesTask((AddStoriesToCollectionTask) task);
                break;
            default:
                throw new GeneralException(task.getTaskType() + " has no CreateFunc");
        }

        return (T) persistenceService.process(conn, func);
    }

    private <T extends Task> T createConcreteRetrieve(ResultSet taskRs, Connection conn) throws SQLException {
        int id = taskRs.getInt(1);
        TaskType taskType = TaskType.valueOf(taskRs.getString(3));

        ProcessFunc<ResultSet, ? extends Task> func;
        switch (taskType) {
            case EXPORT:
                func = new RetrieveExportTask(id, taskRs);
                break;
            case ADD_STORIES:
                func = new RetrieveAddStoriesTask(id, taskRs);
                break;
            default:
                throw new GeneralException(taskType + " has no RetrieveFunc");
        }

        return (T) persistenceService.process(conn, func);
    }

    private static class RetrieveExportTask extends RetrieveConcreteTask<ExportTask> {
        public RetrieveExportTask(int id, ResultSet taskRs) {
            super(id, taskRs);
        }

        @Override
        protected ExportTask createConcrete() throws SQLException {
            PreparedStatement selectTask =
                    conn.prepareStatement("SELECT kind, container, objectId, url, expires " +
                            "FROM exportTask " +
                            "WHERE id=?");

            selectTask.setInt(1, id);

            ResultSet rs = selectTask.executeQuery();
            ExportTask exportTask = null;
            if (rs.next()) {
                exportTask = new ExportTask();
                exportTask.setKind(ExportKind.valueOf(rs.getString(1)));
                exportTask.setContainer(ExportContainer.valueOf(rs.getString(2)));
                exportTask.setObjectId(rs.getInt(3));
                exportTask.setUrl(rs.getString(4));
                exportTask.setExpires(rs.getTimestamp(5));
            }

            return exportTask;
        }
    }

    private static class RetrieveAddStoriesTask extends RetrieveConcreteTask<AddStoriesToCollectionTask> {
        public RetrieveAddStoriesTask(int id, ResultSet taskRs) {
            super(id, taskRs);
        }

        @Override
        protected AddStoriesToCollectionTask createConcrete() throws SQLException {
            PreparedStatement selectTask =
                    conn.prepareStatement("SELECT collections, searchToken, collectionId, questionnaireId " +
                            "FROM addStoriesTask " +
                            "WHERE id=?");

            selectTask.setInt(1, id);

            ResultSet rs = selectTask.executeQuery();
            AddStoriesToCollectionTask addStoriesTask = null;
            if (rs.next()) {
                addStoriesTask = new AddStoriesToCollectionTask();

                String collectionIds = rs.getString(1);
                Set<Integer> ids = FluentIterable.from(Splitter.on(",").split(collectionIds))
                        .transform(new Function<String, Integer>() {
                            @Override
                            public Integer apply(String input) {
                                return Integer.valueOf(input);
                            }
                        }).toSet();

                addStoriesTask.setCollectionIds(ids);
                addStoriesTask.setSearchToken(rs.getString(2));
                addStoriesTask.setCollectionId(getIntOrNull(rs, 3));
                addStoriesTask.setQuestionnaireId(getIntOrNull(rs, 4));
            }

            return addStoriesTask;
        }
    }

    private static Integer getIntOrNull(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }

        return value;
    }

    private static abstract class RetrieveConcreteTask<T extends Task> extends ProcessFunc<ResultSet, T> {
        protected final int id;

        public RetrieveConcreteTask(int id, ResultSet resultSet) {
            super(resultSet);

            this.id = id;
        }

        @Override
        public final T process() {
            try {
                T concrete = createConcrete();

                concrete.setId(id);
                concrete.setProfileId(input.getInt(2));
                concrete.setTaskType(TaskType.valueOf(input.getString(3)));
                concrete.setStatus(TaskStatus.valueOf(input.getString(4)));
                concrete.setCount(input.getInt(5));
                concrete.setTotal(input.getInt(6));
                concrete.setCreated(input.getTimestamp(7));
                concrete.setModified(input.getTimestamp(8));

                return concrete;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }

        protected abstract T createConcrete() throws SQLException;
    }

    private static class CreateExportTask extends CreateConcreteTask<ExportTask> {
        public CreateExportTask(ExportTask task) {
            super(task);
        }

        @Override
        protected ExportTask createConcrete() throws SQLException {
            PreparedStatement create = conn.prepareStatement("INSERT INTO exportTask (id, kind, container, objectId) " +
                    "VALUES (?,?,?,?)");

            create.setInt(1, input.getId());
            create.setString(2, input.getKind().name());
            create.setString(3, input.getContainer().name());
            create.setInt(4, input.getObjectId());

            create.execute();

            return input;
        }
    }

    private static class CreateAddStoriesTask extends CreateConcreteTask<AddStoriesToCollectionTask> {
        public CreateAddStoriesTask(AddStoriesToCollectionTask task) {
            super(task);
        }

        @Override
        protected AddStoriesToCollectionTask createConcrete() throws SQLException {
            PreparedStatement create = conn.prepareStatement(
                    "INSERT INTO addStoriesTask (id, collections, searchToken, collectionId, questionnaireId) " +
                            "VALUES (?,?,?,?,?)");

            create.setInt(1, input.getId());
            create.setString(2, Joiner.on(",").join(input.getCollectionIds()));
            create.setString(3, input.getSearchToken());
            setIntOrNull(create, input.getCollectionId(), 4);
            setIntOrNull(create, input.getQuestionnaireId(), 5);

            create.execute();

            return input;
        }
    }

    private static void setIntOrNull(PreparedStatement create, Integer value, int parameterIndex) throws SQLException {
        if (value == null) {
            create.setNull(parameterIndex, Types.INTEGER);
        } else {
            create.setInt(parameterIndex, value);
        }
    }

    private static abstract class CreateConcreteTask<T extends Task> extends ProcessFunc<T, T> {
        public CreateConcreteTask(T task) {
            super(task);
        }

        @Override
        public T process() {
            try {
                PreparedStatement create = conn.prepareStatement("INSERT INTO task (profile, type, status) " +
                        "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

                create.setInt(1, input.getProfileId());
                create.setString(2, input.getTaskType().name());
                create.setString(3, input.getStatus().name());

                create.execute();

                ResultSet rs = create.getGeneratedKeys();
                if (!rs.next()) {
                    throw new GeneralException("Unable to create task");
                }

                input.setId(rs.getInt(1));

                PreparedStatement getCreated = conn.prepareStatement("SELECT created FROM task WHERE id=?");
                getCreated.setInt(1, input.getId());

                ResultSet result = getCreated.executeQuery();
                if (result.next()) {
                    input.setCreated(result.getTimestamp(1));
                }

                return createConcrete();
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }

        protected abstract T createConcrete() throws SQLException;
    }

    private static class UpdateExportTask extends UpdateConcreteTask<ExportTask> {
        public UpdateExportTask(ExportTask task) {
            super(task);
        }

        @Override
        protected ExportTask updateConcrete() throws SQLException {
            PreparedStatement create = conn.prepareStatement("UPDATE exportTask " +
                    "SET url=?, expires=? " +
                    "WHERE id=?");

            create.setString(1, input.getUrl());
            if (input.getExpires() != null) {
                create.setTimestamp(2, new Timestamp(input.getExpires().getTime()));
            } else {
                create.setNull(2, Types.TIMESTAMP);
            }
            create.setInt(3, input.getId());

            create.execute();

            return input;
        }
    }

    private static abstract class UpdateConcreteTask<T extends Task> extends ProcessFunc<T, T> {
        public UpdateConcreteTask(T task) {
            super(task);
        }

        @Override
        public T process() {
            try {
                PreparedStatement update = conn.prepareStatement("UPDATE task " +
                        "SET status=?, count=?, total=?, lastModified=NOW() " +
                        "WHERE id=?");

                update.setString(1, input.getStatus().name());
                update.setInt(2, input.getCount());
                update.setInt(3, input.getTotal());
                update.setInt(4, input.getId());

                update.executeUpdate();

                PreparedStatement getModified = conn.prepareStatement("SELECT lastModified FROM task WHERE id=?");
                getModified.setInt(1, input.getId());

                ResultSet result = getModified.executeQuery();
                if (result.next()) {
                    input.setModified(result.getTimestamp(1));
                }

                return updateConcrete();
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }

        protected abstract T updateConcrete() throws SQLException;
    }
}
