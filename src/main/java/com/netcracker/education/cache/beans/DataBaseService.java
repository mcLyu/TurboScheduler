package com.netcracker.education.cache.beans;

import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.TaskImpl;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Task;

import java.sql.*;


/**
 * Created by Mill on 19.03.2015.
 */
public class DataBaseService {
    public static Connection connection;
    public static ResultSet resultSet;

    private static int getUserId(String login){
        int user_id = -1;
        try {
            PreparedStatement preparedStatement = null;
            String SQL = "SELECT USER_ID FROM USER WHERE LOGIN=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();
            user_id = resultSet.getInt("USER_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_id;
    }

    private static void establishConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:tasks.s3db");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (resultSet != null) resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TaskList loadUserTasks(String login)  {
        TaskList result = new TaskList();
        establishConnection();
        PreparedStatement preparedStatement = null;

        try {
            int user_id = getUserId(login);

            String SQL = "SELECT * FROM TASK WHERE USER_ID=?";
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();//prepared

            while (resultSet.next()) {
                Task task = new TaskImpl(resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getDate("ALERT_DATE"));
                result.addTask(task);
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String authorizeUser(AuthenticationData authData) {
        String session = "";
        establishConnection();
        String SQL = "SELECT * FROM USER WHERE LOGIN = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, authData.getLogin());

            resultSet = preparedStatement.executeQuery();
            AuthenticationData dataBaseAuthData = new AuthenticationData(resultSet.getString("LOGIN"),
                    resultSet.getString("HASH_PASSWORD"),resultSet.getString("SALT"));
            SecurityService securityService = new SecurityService();
            session = securityService.checkUserExistence(authData,dataBaseAuthData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return session;
    }

    public static void addTask(Task task) {
        establishConnection();

        String SQL = "INSERT INTO TASK(NAME,DESCRIPTION,ALERT_DATE,USER_ID) VALUES" +
                "(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new java.sql.Date(task.getAlertTime().getTime()));
            preparedStatement.setInt(4, getUserId(task.getOwner()));

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        closeConnection();
    }

    public static void registerUser(AuthenticationData authData) {
        SecurityService securityService = new SecurityService();
        securityService.createHashSaltPassword(authData);

        establishConnection();

        try {
            String SQL = "INSERT INTO USER(LOGIN,HASH_PASSWORD,SALT) VALUES " + "(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, authData.getLogin());
            preparedStatement.setString(2, authData.getHashSaltPassword());
            preparedStatement.setString(3, authData.getSalt());
            preparedStatement.executeUpdate();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
    }

    public static void removeTask(Task task) {

        establishConnection();

        String SQL = "DELETE FROM TASK WHERE NAME=? AND DESCRIPTION=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        closeConnection();
    }
}
