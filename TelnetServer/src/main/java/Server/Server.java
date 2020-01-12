package Server;

import Threads.FindFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  public static void main(String[] args)
  {
    try
    {
      ServerSocket serverSocket = new ServerSocket(8000);
      System.out.println("server starts");
      Socket socket = serverSocket.accept();

      System.out.println("client connected");

      InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
      printWriter.println("You are connected. Input path and mask as in example: 5,gold");
      printWriter.flush();

      String str = bufferedReader.readLine();
      System.out.println("User says " + str);
      FindFiles findFiles = new FindFiles(str, socket);
      findFiles.start();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
