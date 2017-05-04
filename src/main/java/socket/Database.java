package socket;

import java.sql.*;

public class Database {

    private Connection connection;

    public Database() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/zakharova", "zakharova", "zakharova");
    }

    public void write(String userName, String line) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO message (login, message) VALUES (?, ?) RETURNING id");
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, line);
        preparedStatement.executeUpdate();
    }

    public void displayHistory() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM message");
        ResultSet result = preparedStatement.executeQuery();
        while (result.next())
            System.out.println(result.getString("login") + ":" + result.getString("message"));
    }

    public void displayHistoryForClient(String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM message");
        ResultSet result = preparedStatement.executeQuery();
        while (result.next())
            if (result.getString("login").equals(login))
                System.out.println(result.getString("message"));
            else System.out.println(result.getString("login") + ":" + result.getString("message"));
    }
}