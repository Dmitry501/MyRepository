package Threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Results extends Thread
{
  private String filePath;
  private Socket socket;

  public Results(String filePath, Socket socket)
  {
    this.filePath = filePath;
    this.socket = socket;
  }

  @Override
  public void run()
  {
    PrintWriter printWriter = null;
    try
    {
      printWriter = new PrintWriter(socket.getOutputStream());
      printWriter.println(filePath);
      printWriter.flush();
      System.out.println(filePath);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
