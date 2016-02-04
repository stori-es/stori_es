package org.consumersunion.stories.server.persistence.funcs;

import java.sql.SQLException;
import java.util.List;

import org.consumersunion.stories.common.shared.service.GeneralException;

public abstract class RetrieveListFunc<Output> extends ProcessFunc<List<Integer>, List<Output>> {
    protected RetrieveListFunc(List<Integer> ids) {
        super(ids);
    }

    @Override
    public List<Output> process() {
        try {
            return retrieveConcrete();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }
    }

    protected abstract List<Output> retrieveConcrete() throws SQLException;
}
