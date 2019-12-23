package db;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class FillDB
{
  private static Connection connection;
  
  public static void fill() throws SQLException
  {
    String DBpath = "jdbc:sqlite:src/main/resources/DataBase.sqlite3";
    String query = new StringBuilder().append("CREATE TABLE IF NOT EXISTS [OrderBook] ")
                                      .append("(PRICE INTEGER,")
                                      .append("SIZE INTEGER,")
                                      .append("TYPE CHAR,")
                                      .append("COMMENT STRING);")
                                      .toString();

    connection = ConnectionDB.getConnection(DBpath);
    Statement statement = connection.createStatement();
    statement.execute(query);
    statement.execute("DELETE  From OrderBook");

    String query2 = new StringBuilder().append("INSERT INTO OrderBook (PRICE, SIZE, TYPE, COMMENT) VALUES")
                                       .append("(100, 0,  'A', 'size is zero, but it is still ask price, because it is higher than best ask'),")
                                       .append("(99,  50, 'A', 'best ask (lowest non-zero ask price)'),")
                                       .append("(97,  0,  'S',  null),")
                                       .append("(96,  0,  'S',  null),")
                                       .append("(95,  40, 'B', 'best bid (highest non-zero bid price)'),")
                                       .append("(94,  30, 'B',  null),")
                                       .append("(93,  0,  'B',  null),")
                                       .append("(92,  77, 'B',  null)")
                                       .toString();
    statement.execute(query2);
  }
}
