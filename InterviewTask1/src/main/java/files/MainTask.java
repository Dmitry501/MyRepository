package files;

import db.ConnectionDB;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainTask
{
  private static Connection connection;

  public static void Start() throws IOException, SQLException
  {
    File inputFile = new File("src\\main\\resources\\input.csv");
    File outputFile = new File("src\\main\\resources\\output.csv");
    FileWriter fileWriter = new FileWriter(outputFile, false);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
    connection = ConnectionDB.getConnection("jdbc:sqlite:src/main/resources/DataBase.sqlite3");
    Statement statement = connection.createStatement();
    ArrayList<String> inputList = new ArrayList<String>();
    String tmp = bufferedReader.readLine();
    while(tmp != null)
    {
      inputList.add(tmp);
      tmp = bufferedReader.readLine();
    }
    for (String str: inputList
         )
    {
      String[] split = str.split(",");


        if(str.charAt(0) == 'u')
        {
          switch (split[3])
          {
            case "bid":
              split[3] = "B";
              break;
            case "ask":
              split[3] = "A";
              break;
            case "spread":
              split[3] = "S";
              break;
          }
          String temp;
          if (split.length == 4)
          {
            temp = "null";
          }
          else
          {
            temp = new StringBuilder().append("'").append(split[4]).append("'").toString();
          }

          statement.execute(new StringBuilder().append("INSERT INTO OrderBook (PRICE, SIZE, TYPE, COMMENT) VALUES")
              .append("(" + split[1] + "," + split[2] + ",'" + split[3] + "'," + temp + ")")
              .toString());

            fileWriter.write(new StringBuilder().append("Added " + split[1] + "," + split[2] + "," + split[3] + '\n').toString());
          }

          if(str.charAt(0) == 'q')
          {
            if(split[1].equals("best_bid"))
          {
            ResultSet resultSet = statement.executeQuery(new StringBuilder().append("SELECT * FROM OrderBook ")
                .append("WHERE Price=(Select MAX(Price) FROM OrderBook WHERE TYPE = 'B' AND SIZE != 0)").toString());
            fileWriter.write(new StringBuilder().append("Highest price is " + resultSet.getString("PRICE") + " with ")
                .append(resultSet.getString("SIZE") + " size\n").toString());
          }
          if(split[1].equals("best_ask"))
          {
            ResultSet resultSet = statement.executeQuery(new StringBuilder().append("SELECT * FROM OrderBook ")
                .append("WHERE Price=(Select MIN(Price) FROM OrderBook WHERE TYPE = 'A' AND SIZE != 0)").toString());
            fileWriter.write(new StringBuilder().append("Lowest price is " + resultSet.getString("PRICE") + " with ")
                .append(resultSet.getString("SIZE") + " size\n").toString());
          }
          if(split.length == 3)
          {
            ResultSet resultSet = statement.executeQuery(new StringBuilder().append("SELECT * FROM OrderBook Where Size =")
                .append(split[2] + "").toString());
            fileWriter.write(new StringBuilder().append("There are " + resultSet.getString("PRICE") + " PRICE for " +
                split[2] + " SIZE. " + resultSet.getString("TYPE") ).toString());
          }
          }
        if(str.charAt(0) == 'o')
        {
          if (split[1].equals("buy"))
          {
            ResultSet resultSet = statement.executeQuery(new StringBuilder().append("SELECT * FROM OrderBook WHERE ")
                .append("TYPE = 'A' AND PRICE=(SELECT MIN(PRICE) From OrderBook WHERE Type = 'A')").toString());
            fileWriter.write(new StringBuilder().append("You have bought " + split[2])
                .append(" by " + resultSet.getString("PRICE") + " each").toString());
            statement.execute(new StringBuilder().append("UPDATE OrderBook SET Size = (Size - ")
                .append(split[2] + ") WHERE PRICE=(SELECT PRICE FROM OrderBook WHERE TYPE = 'A'")
                .append(" AND PRICE=(SELECT MIN(PRICE) FROM OrderBook WHERE Type = 'A'))").toString());
          }
          if (split[1].equals("sell"))
          {
            ResultSet resultSet = statement.executeQuery(new StringBuilder().append("SELECT * FROM OrderBook WHERE ")
                .append("TYPE = 'B' AND PRICE=(SELECT MAX(PRICE) From OrderBook WHERE Type = 'B')").toString());
            fileWriter.write(new StringBuilder().append("You have sold " + split[2])
                .append(" by " + resultSet.getString("PRICE") + " each").toString());
            statement.execute(new StringBuilder().append("UPDATE OrderBook SET Size = (Size - ")
                .append(split[2] + ") WHERE PRICE=(SELECT PRICE FROM OrderBook WHERE TYPE = 'B'")
                .append(" AND PRICE=(SELECT MAX(PRICE) FROM OrderBook WHERE Type = 'B'))").toString());
          }
        }
    }
    fileWriter.flush();
  }
}
