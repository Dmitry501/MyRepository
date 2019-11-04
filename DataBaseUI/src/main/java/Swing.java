import javax.swing.*;
import ui.MainFrame;

public class Swing
{
  public static void main(String[] args)
  {
    MainFrame mainFrame = new MainFrame();

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        mainFrame.initUI();
      }
    });
  }
}