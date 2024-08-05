package de.kalosystems.protectionsystem.mysql;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQL {

    private static String host;
    private static int port;
    private static String database;
    private static String user;
    private static String password;
    private Connection con;

    /**
     * Connect to database
     *
     * @param host     The mysql host
     * @param port     The mysql port
     * @param database The database to connect to
     * @param user     The user
     * @param password The password
     */
    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;

        connect();
    }

    /**
     * Connect to database
     */
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database
                            + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    user, password);

            System.out.println("[MySQL] Connected to database");
        } catch (final SQLException ex) {
            ex.printStackTrace();

            System.out.println("[MySQL] Could not connect");
        }

    }

    /**
     * Disconnect
     */
    public void disconnect() {
        try {
            con.close();

            System.out.println("[MySQL] Disconnected from database");
        } catch (final SQLException ex) {
            ex.printStackTrace();

            System.out.println("[MySQL] Could not disconnect");
        }
    }

    /**
     * Get a result from query
     *
     * @param qry The query to send
     * @return The ResultSet
     */
    public ResultSet getResult(final String qry) {
        if (isConnected()) {
            try {
                final FutureTask<ResultSet> task = new FutureTask<ResultSet>(new Callable<ResultSet>() {

                    PreparedStatement ps;

                    @Override
                    public ResultSet call() throws Exception {
                        ps = con.prepareStatement(qry);

                        return ps.executeQuery();
                    }
                });

                task.run();

                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            connect();
        }

        return null;
    }

    /**
     * Check if mysql is connected
     *
     * @return True if mysql is connected, false otherwise
     */
    public boolean isConnected() {
        return (con != null ? true : false);
    }

    /**
     * Send update query
     *
     * @param qry The query to send
     */
    public void update(final String qry) {
        if (isConnected()) {
            new FutureTask<>(new Runnable() {

                PreparedStatement ps;

                @Override
                public void run() {
                    try {
                        ps = con.prepareStatement(qry);

                        ps.executeUpdate();
                        ps.close();
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                }
            }, 1).run();
        } else {
            connect();
        }
    }

    /**
     * Send boolean query update
     *
     * @param qry   The query to send
     * @param value The boolean value
     */
    public void updateWithBoolean(final String qry, final boolean value) {
        if (isConnected()) {
            new FutureTask<>(new Runnable() {

                PreparedStatement ps;

                @Override
                public void run() {
                    try {
                        ps = con.prepareStatement(qry);
                        ps.setBoolean(1, value);

                        ps.executeUpdate();
                        ps.close();
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                }
            }, 1).run();
        } else {
            connect();
        }
    }

    public void createTable(String table_name, String table_type) {
        update("CREATE TABLE IF NOT EXISTS " + table_name + "(" + table_type + ")");
    }

    public Connection getConnection() {
        return con;
    }
}
