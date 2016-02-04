package org.consumersunion.stories.server.persistence.funcs;

import java.sql.Connection;

public interface Func<Input, Output> {
    Output process();

    void setConnection(Connection conn);
}
