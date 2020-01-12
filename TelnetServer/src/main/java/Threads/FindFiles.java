package Threads;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFiles extends Thread
{
  private String str;
  private Socket socket;

  public FindFiles(String str, Socket socket)
  {
    this.str = str;
    this.socket = socket;
  }

  @Override
  public void run()
  {
    String rootPath = ".//src//main//resources//MainDir";
    String[] split = str.split(",");
    int depth = Integer.parseInt(split[0]);
    String mask = split[1];

    File file = new File(rootPath);
    List<List<File>> files = new ArrayList<>();
    files.add(0, Arrays.asList(file));

    for (int i = 0; i < files.size(); i++)
    {
      List<File> tempFiles = new ArrayList<>();
      List<File> tempDirs = new ArrayList<>();
      for (File f : files.get(i))
      {
        if(f.getName().contains(mask))
        {
          Results results = new Results(f.getAbsolutePath(), socket);
          results.start();
        }
        if (f.isDirectory() && i < depth)
        {
          tempDirs.add(f);
        }
      }
      for (File f : tempDirs)
      {
        tempFiles.addAll(Arrays.asList(f.listFiles()));
      }
      if(!tempFiles.isEmpty())
      {
        files.add(i+1, tempFiles);
      }
    }
  }
}
