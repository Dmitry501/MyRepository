package Server;

import java.io.IOException;
import java.net.Socket;

public class Client
{
  public static void main(String[] args) throws IOException
  {
    Socket socket = new Socket("localhost", 8000);

  }
}
