/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import java.sql.*;

/**
 *
 * @author Peter
 */
public class ScoreDB {
    /// score server address
    private static final String serverAddr = "localhost";

    /// score database name
    private static final String dbName = "test";

    /// MySQL JDBC driver name
    private static final String driverName = "org.gjt.mm.mysql.Driver";

    /// DB user name
    private static final String userName = "";
    
    /// DB password
    private static final String userPass = "";

    /// connection session
    private Connection con = null;

    /**
     * try to open connection to local duckhunt database
     * 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public ScoreDB() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);

        String url = "jdbc:mysql://" + serverAddr + "/" + dbName;

        con = DriverManager.getConnection(url, userName, userPass);
    }

    /// close DB connection on exit
    @Override public void finalize(){
        try{
            con.close();
            con=null;
        }catch(SQLException e){
            System.out.println(" * DB finalize exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * try to read score
     *
     * @return score if valid DB was found, otherwise -1, or exception is thrown
     * @throws java.sql.SQLException
     */
    public int getScore() {        
        try{
            Statement stmt = con.createStatement();

            if(!stmt.execute("SELECT TopScore FROM DuckHunt WHERE pk=0"))
                return -1;
        
            if(!stmt.getResultSet().next())
                return -1;

            return stmt.getResultSet().getInt(1);
        }catch(SQLException e){
            System.out.println(" * getScore : "+e.getLocalizedMessage());
            return -1;
        }
        
    }

    /**
     * updates (or creates) top score
     *
     * @param score new score
     */
    public void setScore(int score) throws SQLException {
        if(getScore()<0){
            System.out.println(" * creating table");
            con.createStatement().execute("DROP TABLE DuckHunt");
            con.createStatement().execute("CREATE TABLE DuckHunt ( pk int primary key, TopScore int )");
            con.createStatement().execute("INSERT INTO DuckHunt VALUES(0, "+Integer.toString(score) +")");
        }else
            con.createStatement().execute("UPDATE DuckHunt SET TopScore="+Integer.toString(score)+" WHERE pk=0");
    }
}
