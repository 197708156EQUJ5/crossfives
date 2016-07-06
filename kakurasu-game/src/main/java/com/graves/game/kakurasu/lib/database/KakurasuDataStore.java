package com.graves.game.kakurasu.lib.database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.jdbc.EmbeddedDriver;

public class KakurasuDataStore
{
    private static final String DATABASE_URL = "jdbc:derby:kakurasu;create=true";
    private static final String USER_TABLE = "users";
    private static final String USER_TABLE_ID = "id";
    private static final String USER_TABLE_NAME = "name";
    private static final String USER_TABLE_PASSWORD = "password";
    private static final String USER_TABLE_SALT = "salt";
    private static String userTable;

    static
    {
        StringBuilder sb = new StringBuilder();
        sb.append("create table " + USER_TABLE + " (");
        sb.append(USER_TABLE_ID + " integer not null generated always as identity "
                + "(start with 0, increment by 1),");
        sb.append(USER_TABLE_NAME + " varchar(30) not null,");
        sb.append(USER_TABLE_PASSWORD + " char(20) for bit data,");
        sb.append(USER_TABLE_SALT + " char(8) for bit data,");
        sb.append("constraint primary_key primary key (id))");

        userTable = sb.toString();
    }

    private static Connection conn = null;

    public static void createdDatabase()
    {

        try
        {
            Driver derbyEmbeddedDriver = new EmbeddedDriver();
            DriverManager.registerDriver(derbyEmbeddedDriver);
            conn = DriverManager.getConnection(DATABASE_URL, "karkurasu", "karkurasu1234");
            conn.setAutoCommit(false);
            if (!tableExists(conn, USER_TABLE))
            {
                Statement stmt = conn.createStatement();
                stmt.execute(userTable);
                conn.commit();
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static User getUser(String userName)
    {
        User user = null;
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createSelectStatement(USER_TABLE, USER_TABLE_ID,
                    USER_TABLE_NAME, USER_TABLE_PASSWORD, USER_TABLE_SALT));
            if (rs.next())
            {
                user = new User(rs.getInt(1), rs.getString(2), rs.getBytes(3), rs.getBytes(4));
            }
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return user;
    }

    public static boolean addUser(String userName, String password)
    {
        try
        {
            byte[] salt = PasswordEncryptionService.generateSalt();
            byte[] encryptedPassword = PasswordEncryptionService.getEncryptedPassword(password,
                    salt);
            PreparedStatement pstmt = conn.prepareStatement(createInsertStatement(USER_TABLE,
                    USER_TABLE_NAME, USER_TABLE_PASSWORD, USER_TABLE_SALT));
            pstmt.setString(1, userName);
            pstmt.setBytes(2, encryptedPassword);
            pstmt.setBytes(3, salt);
            pstmt.executeUpdate();
        }
        catch (NoSuchAlgorithmException e1)
        {
            System.err.println(e1.getMessage());
            return false;
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
        catch (InvalidKeySpecException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static String createSelectStatement(String tableName, String... columns)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        for (int i = 0; i < columns.length - 1; i++)
        {
            sb.append(columns[i] + ",");
        }
        sb.append(columns[columns.length - 1]);
        sb.append(" from " + tableName);
        return sb.toString();
    }

    private static String createInsertStatement(String tableName, String... columns)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into " + tableName + "(");
        for (int i = 0; i < columns.length - 1; i++)
        {
            sb.append(columns[i] + ",");
        }
        sb.append(columns[columns.length - 1]);
        sb.append(") values (");
        for (int i = 0; i < columns.length - 1; i++)
        {
            sb.append("?,");
        }
        sb.append("?)");
        return sb.toString();
    }

    /**
     * Unit testing purposes
     */
    static void dropTables()
    {
        try
        {
            Statement stmt = conn.createStatement();
            stmt.execute("drop table " + USER_TABLE);

            conn.commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        try
        {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (SQLException ex)
        {
            if (((ex.getErrorCode() == 50000) && ("XJ015".equals(ex.getSQLState()))))
            {
                System.out.println("Derby shut down normally");
            }
            else
            {
                System.err.println("Derby did not shut down normally");
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Derby doesn't support the standard SQL views. To see if a table exists you normally query the
     * right view and see if any rows are returned (none if no such table, one if table exists).
     * Derby does support a non-standard set of views which are complicated, but standard JDBC
     * supports a DatabaseMetaData.getTables method. That returns a ResultSet but not one where you
     * can easily count rows by "rs.last(); int numRows = rs.getRow()". Hence the loop.
     * 
     * @param con
     * @param table
     * @return
     */
    private static boolean tableExists(Connection con, String table)
    {
        int numRows = 0;
        try
        {
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, table.toUpperCase(), null);
            while (rs.next())
            {
                ++numRows;
            }
        }
        catch (SQLException e)
        {
            String theError = e.getSQLState();
            System.out.println("Can't query DB metadata: " + theError);
            System.exit(1);
        }
        return numRows > 0;
    }
}
