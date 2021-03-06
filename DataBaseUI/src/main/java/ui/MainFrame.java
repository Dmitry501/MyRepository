package ui;

import db.ConnectionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainFrame extends JFrame
{
  JPanel connectionPanel;
  JPanel tableListPanel;
  JPanel dataPanel;
  ArrayList<String> tables = new ArrayList<>();
  ArrayList<String> columnNames = new ArrayList<>();
  ArrayList<String[]> data = new ArrayList<>();

  private static Connection connection;

  public void initUI()
  {
    this.setSize(600 ,400);
    this.setTitle("DB client");
    this.setLayout(new BorderLayout());
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    connectionPanel = createConnectionPanel();
    tableListPanel = createTableListPanel();
    dataPanel = createDataPanel();

    this.add(connectionPanel, BorderLayout.NORTH);
    this.add(tableListPanel, BorderLayout.WEST);
    this.add(dataPanel, BorderLayout.CENTER);
  }

  private JPanel createConnectionPanel()
  {
    JPanel panel = new JPanel(new FlowLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    panel.add(new Label("JDBC URL: "));
    JTextField textField = new JTextField("jdbc:sqlite:src/main/java/resources/DB1.sqlite3");
    panel.add(textField);

    JButton openButton = new JButton("Open");
    panel.add(openButton);

    openButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        try
        {
          ConnectionDB connectionDB = new ConnectionDB();
          connection = connectionDB.getConnection(textField.getText());
          String query = "SELECT * FROM sqlite_master WHERE type='table'";
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          ResultSet resultSet = preparedStatement.executeQuery();
          tables = new ArrayList<>();
          while(resultSet.next())
          {
            tables.add(resultSet.getString("tbl_name"));
          }
          tableListPanel.removeAll();
          tableListPanel.add(createTableListPanel());
          tableListPanel.revalidate();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    });

    return panel;
  }

  private JPanel createTableListPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createLineBorder(Color.black));

    for(int i = 0; i < tables.size(); i++)
    {
      String temp = tables.get(i);
      if(!(temp.equals("sqlite_sequence")))
      {
        JButton button = new JButton(temp);
        button.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            try
            {
              data.clear();
              columnNames.clear();
              String query = "PRAGMA table_info(" + e.getActionCommand() + ")";
              PreparedStatement preparedStatement = connection.prepareStatement(query);
              ResultSet resultSet = preparedStatement.executeQuery();
              while(resultSet.next())
              {
                String clmName = resultSet.getString("name");
                columnNames.add(clmName);
                String query1 = "SELECT " + clmName + " FROM " + e.getActionCommand();
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                ArrayList<String> temp = new ArrayList<>();
                while(resultSet1.next())
                {
                  temp.add(resultSet1.getString(clmName));
                }
                data.add(temp.toArray(new String[temp.size()]));
              }
              dataPanel.removeAll();
              dataPanel.add(createDataPanel());
              dataPanel.revalidate();
            }
            catch (SQLException ex)
            {
              ex.printStackTrace();
            }
          }
        });
        panel.add(button);
      }
    }
    return panel;
  }

  private JPanel createDataPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createLineBorder(Color.black));
    if( ! columnNames.isEmpty())
    {
      String[] testClmn = columnNames.toArray(new String[columnNames.size()]);
      int a = data.size();
      int b = data.get(0).length;
      String[][] testData = new String[b][a];
      for(int i = 0; i < b; i++)
        for(int j = 0; j < a; j++)
        {
          testData[i][j] = data.get(j)[i];
        }
      JTable table = new JTable(testData, testClmn);
      panel.add(new JScrollPane(table));
      return panel;
    }
    return panel;
  }
}