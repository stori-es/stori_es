package org.consumersunion.stories.server.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.StoriesCountByDate;
import org.consumersunion.stories.common.shared.dto.StoriesCountByDayAndTime;
import org.consumersunion.stories.common.shared.dto.StoriesCountByState;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.metrics.ItemCountByDate;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

@Component
public class MetricsPersister {
    private final PersistenceService persistenceService;

    @Inject
    MetricsPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public StoriesCountByDayAndTime getStoryCountByDayAndTime(int collectionId, final String timezone) {
        return persistenceService.process(new ProcessFunc<Integer, StoriesCountByDayAndTime>(collectionId) {
            @Override
            public StoriesCountByDayAndTime process() {
                StoriesCountByDayAndTime storiesCountByDayAndTime = new StoriesCountByDayAndTime();

                try {
                    String query = "SELECT r.count, r.d FROM (" +
                            "SELECT count(distinct(se.id)) count, CONVERT_TZ(se.created, '+00:00', ?) d " +
                            "FROM systemEntity se " +
                            "JOIN collection_story cs ON cs.story = se.id " +
                            "JOIN collection c ON c.id = cs.collection " +
                            "JOIN systemEntity ce ON ce.id=c.id WHERE cs.collection = ? " +
                            "GROUP BY DATE(d), HOUR(d)) r " +
                            "ORDER BY r.d ASC";

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, timezone);
                    ps.setInt(2, input);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Date date = new Date(rs.getTimestamp(2).getTime());

                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTime(date);
                        storiesCountByDayAndTime.add(calendar.get(Calendar.DAY_OF_WEEK),
                                calendar.get(Calendar.HOUR_OF_DAY) + 1, rs.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return storiesCountByDayAndTime;
            }
        });
    }

    public StoriesCountByDate getStoriesCountByDate(int collectionId, final String timezone) {
        return persistenceService.process(new ProcessFunc<Integer, StoriesCountByDate>(collectionId) {
            @Override
            public StoriesCountByDate process() {
                StoriesCountByDate storiesCountByDate = new StoriesCountByDate();

                try {
                    String query = "SELECT r.count, r.d FROM (" +
                            "SELECT count(distinct(se.id)) count, CONVERT_TZ(se.created, '+00:00', ?) d " +
                            "FROM systemEntity se " +
                            "JOIN collection_story cs ON cs.story = se.id " +
                            "JOIN collection c ON c.id = cs.collection " +
                            "JOIN systemEntity ce ON ce.id=c.id WHERE cs.collection = ? " +
                            "GROUP BY DATE(se.created)) r " +
                            "ORDER BY r.d ASC";

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, timezone);
                    ps.setInt(2, input);

                    ResultSet rs = ps.executeQuery();

                    DateTimeZone dateTimeZone = DateTimeZone.forID(timezone);
                    while (rs.next()) {
                        DateTime date = new DateTime(rs.getTimestamp(2).getTime(), dateTimeZone);
                        storiesCountByDate.add(date.toDate(), rs.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return storiesCountByDate;
            }
        });
    }

    public StoriesCountByState getStoriesCountByState(int collectionId) {
        return persistenceService.process(new ProcessFunc<Integer, StoriesCountByState>(collectionId) {
            @Override
            public StoriesCountByState process() {
                StoriesCountByState storiesCountByState = new StoriesCountByState();

                try {
                    String query = "SELECT r.count, r.state FROM (" +
                            "SELECT count(distinct(se.id)) count, a.state " +
                            "FROM systemEntity se " +
                            "JOIN collection_story cs ON cs.story = se.id " +
                            "JOIN address a ON a.entity = se.id " +
                            "JOIN collection c ON c.id = cs.collection " +
                            "JOIN systemEntity ce ON ce.id=c.id " +
                            "WHERE cs.collection = ? AND a.state IS NOT NULL " +
                            "GROUP BY a.state) r " +
                            "ORDER BY r.state ASC";

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, input);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        storiesCountByState.add(rs.getString(2), rs.getInt(1));
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return storiesCountByState;
            }
        });
    }

    public ProcessFunc<Integer, List<ItemCountByDate>> getStoryCountByCreationDate(int organizationId) {
        return new ProcessFunc<Integer, List<ItemCountByDate>>(organizationId) {
            @Override
            public List<ItemCountByDate> process() {
                ArrayList<ItemCountByDate> metrics = new ArrayList<ItemCountByDate>();

                try {
                    String query = "SELECT r.count, r.d FROM (" +
                            "SELECT count(distinct(se.id)) count, DATE(se.created) d " +
                            "FROM systemEntity se " +
                            "JOIN collection_story cs ON cs.story = se.id " +
                            "JOIN collection c ON c.id = cs.collection " +
                            "JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner = ? " +
                            "GROUP BY DATE(se.created) " + " ) r " +
                            "ORDER BY r.d ASC";

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, input);

                    ResultSet rs = ps.executeQuery();

                    int totalCount = 0;
                    while (rs.next()) {
                        ItemCountByDate metric = new ItemCountByDate();

                        int dayCount = rs.getInt(1);
                        metric.setItemCount(totalCount + dayCount);
                        metric.setStartDate(rs.getDate(2));
                        metrics.add(metric);

                        totalCount += dayCount;
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return metrics;
            }
        };
    }

    public ProcessFunc<Integer, List<ItemCountByDate>> getUpdatesOptInCountByCreationDate(int organizationId) {
        return new ProcessFunc<Integer, List<ItemCountByDate>>(organizationId) {
            @Override
            public List<ItemCountByDate> process() {
                ArrayList<ItemCountByDate> metrics = new ArrayList<ItemCountByDate>();

                try {
                    // Ugly but fast. We select a daily count out of the number of 'opt-ins' twice, in two inline
                    // tables. These are then joined
                    // - 'UPDATES OPT-IN' is a check box, so any value is positive. Absence is implicitly negative.
                    String dailyCountQuery =
                            "SELECT count(a.label) count, DATE(se.created) d FROM answer a"
                                    + " JOIN answerSet ans ON ans.id = a.answerSet"
                                    + " JOIN systemEntity se ON ans.id = se.id"
                                    + " JOIN block b ON ans.questionnaire = b.document"
                                    + " JOIN block_question q ON b.idx = q.idx AND q.document = b.document AND q" +
                                    ".label = a.label"
                                    + " JOIN questionnaire qnre ON qnre.id = b.document"
                                    + " JOIN collection_sources col ON col.sourceQuestionnaire = qnre.id"
                                    + " JOIN systemEntity colE ON col.targetCollection=colE.id "
                                    + " WHERE b.blockType = '" + BlockType.UPDATES_OPT_IN.code() + "'"
                                    + " AND reportValue IS NOT NULL "
                                    + " AND reportValue <> ''"
                                    + " AND colE.owner=?"
                                    + " GROUP BY DATE(se.created)";
                    String query = "SELECT r.count, r.d FROM"
                            + " (" + dailyCountQuery + ") r"
                            + " GROUP BY r.d";

                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, input);

                    ResultSet rs = ps.executeQuery();
                    int totalCount = 0;
                    while (rs.next()) {
                        ItemCountByDate metric = new ItemCountByDate();

                        int dayCount = rs.getInt(1);
                        metric.setItemCount(totalCount + dayCount);
                        metric.setStartDate(rs.getDate(2));
                        metrics.add(metric);

                        totalCount += dayCount;
                    }
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }

                return metrics;
            }
        };
    }
}
