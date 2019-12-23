import db.FillDB;
import files.MainTask;

import java.io.IOException;
import java.sql.SQLException;

public class Main
{
  public static void main(String[] args) throws SQLException, IOException
  {
    FillDB.fill();
    MainTask.Start();
  }
}
