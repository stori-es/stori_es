package org.consumersunion.stories.server.persistence;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Get the sql file in a reader object and running it in the data base.
 *
 * @author Machin
 */
public class ScriptRunner {
    private static final String DEFAULT_DELIMITER = ";";

    private final String jdbcUrl;
    private final boolean stopOnError;
    private final boolean autoCommit;
    private final boolean fullLineDelimiter = false;

    private boolean alwaysRun = false;
    private Connection connection;
    private PrintWriter logWriter = null;
    private PrintWriter errorLogWriter = new PrintWriter(System.err);
    private String delimiter = DEFAULT_DELIMITER;

    /**
     * Default constructor
     */
    public ScriptRunner(final boolean autoCommit, final boolean stopOnError) {
        this.jdbcUrl = PersistenceUtil.getJdbcUrl();
        System.out.println("Script JDBC URL: " + jdbcUrl);
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    public void setAlwaysRun(boolean alwaysRun) {
        this.alwaysRun = alwaysRun;
    }

    public boolean isAlwaysRun() {
        return alwaysRun;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader - the source of the script
     */
    public void runScript(Reader reader, String fileName) throws IOException, SQLException {
        ensureConnection();

        delimiter = DEFAULT_DELIMITER;
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }

                if (isAlwaysRun() || scriptNotExecuted(fileName)) {
                    System.out.println("running script: " + fileName);
                    runScript(connection, reader);
                    addScriptExecuted(fileName);
                    System.out.println("Executed Script: " + fileName);
                }
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (IOException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    private boolean scriptNotExecuted(String fileName) throws SQLException {
        System.out.println("checking: " + fileName + "...");

        PreparedStatement select = connection.prepareStatement("SELECT COUNT(*) FROM dbUpdate db"
                + " WHERE LOWER(db.scriptName) = LOWER(?)");
        select.setString(1, fileName);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) == 0;
        }

        return false;
    }

    private void addScriptExecuted(String fileName) throws SQLException {
        if (!fileName.contains("stories_triggers")) {
            // we don't need to create this record for triggers script
            PreparedStatement insert = connection
                    .prepareStatement("INSERT INTO dbUpdate (scriptName) VALUES (?)");
            insert.setString(1, fileName);

            int insertCount = insert.executeUpdate();
            if (insertCount != 1) {
                throw new RuntimeException("Insert Error: " + insertCount + " on file " + fileName);
            } else if (!autoCommit) {
                connection.commit();
                flush();
            }
        }
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in
     *
     * @param conn   - the connection to use for the script
     * @param reader - the source of the script
     * @throws SQLException if any SQL errors occur
     * @throws IOException  if there is an error reading from the Reader
     */
    private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }

                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    println(trimmedLine);
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (trimmedLine.startsWith("DELIMITER")) {
                    // update the delimiter
                    delimiter = trimmedLine.substring(9).trim();
                } else if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter()) || fullLineDelimiter
                        && trimmedLine.equals(getDelimiter())) {
                    command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append("\n");
                    Statement statement = conn.createStatement();

                    println(command);

                    boolean hasResults = false;
                    if (stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (SQLException e) {
                            e.fillInStackTrace();
                            printlnError("Error executing: " + command);
                            printlnError(e);
                        }
                    }

                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    ResultSet rs = statement.getResultSet();
                    if (hasResults && rs != null) {
                        ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        for (int i = 1; i <= cols; i++) {
                            String name = md.getColumnLabel(i);
                            print(name + "\t");
                        }
                        println("");
                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                String value = rs.getString(i);
                                print(value + "\t");
                            }
                            println("");
                        }
                    }

                    command = null;
                    try {
                        statement.close();
                    } catch (Exception ignored) {
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                    Thread.yield();
                } else {
                    command.append(line);
                    command.append("\n");
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } catch (IOException e) {
            e.fillInStackTrace();
            printlnError("Error executing: " + command);
            printlnError(e);
            throw e;
        } finally {
            conn.rollback();
            flush();
        }
    }

    private String getDelimiter() {
        return delimiter;
    }

    private void print(Object o) {
        if (logWriter != null) {
            System.out.print(o);
        }
    }

    private void println(Object o) {
        if (logWriter != null) {
            logWriter.println(o);
        }
    }

    private void printlnError(Object o) {
        if (errorLogWriter != null) {
            errorLogWriter.println(o);
        }
    }

    private void flush() {
        if (logWriter != null) {
            logWriter.flush();
        }
        if (errorLogWriter != null) {
            errorLogWriter.flush();
        }
    }

    public void deleteAccessKeyData() throws SQLException {
        ensureConnection();

        System.out.println("jdbcUrl: " + jdbcUrl);
        if (!jdbcUrl.contains("zdb")) {
            connection.setSavepoint();
            connection.createStatement().executeUpdate("DELETE FROM accessKey");
            connection.commit();
        }
    }

    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        ScriptRunner runner = new ScriptRunner(false, true);
        runner.setAlwaysRun(true);

        FileReader reader = new FileReader(fileName);
        runner.runScript(reader, fileName);
        runner.close();
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection ensureConnection() {
        if (connection == null) {
            connection = PersistenceUtil.getConnection();
        }

        return connection;
    }
}
