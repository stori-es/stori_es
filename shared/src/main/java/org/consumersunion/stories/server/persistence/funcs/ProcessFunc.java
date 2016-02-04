package org.consumersunion.stories.server.persistence.funcs;

import java.sql.Connection;

public abstract class ProcessFunc<Input, Output> implements Func<Input, Output> {
    protected final Input input;

    protected Connection conn;

    public ProcessFunc(Input input) {
        this.input = input;
    }

    @Override
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}
