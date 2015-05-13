package com.netcracker.education.beans;

import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.TaskImpl;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Task;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * Created by Mill on 19.03.2015.
 */
public class DataBaseService {
    private static Logger log = Logger.getLogger(DataBaseService.class.getName());
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
            log.error("Error when searching user in database.",e);
        }
        return user_id;
    }

    private static void establishConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:tasks.s3db");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Cannot establish database connection.",e);
        }
    }

    private static void closeConnection() {
        try {
            if (resultSet != null) resultSet.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Cannot close database connection.", e);
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
                String alertDate = resultSet.getString("ALERT_DATE");
                java.util.Date dbDate = new java.util.Date(Long.parseLong(alertDate));

                DateFormat myFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                myFormat.setTimeZone(TimeZone.getDefault());
                String parsedFormat = myFormat.format(dbDate);
                Task task = new TaskImpl(resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), myFormat.parse(parsedFormat));
                result.addTask(task);
            }

            closeConnection();
        } catch (SQLException|ParseException e) {
            log.error("Cannot load task list from database.", e);
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
            log.error("Cannot authorize user.", e);
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
            log.error("Cannot add task to database.", e);
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
        } catch (NullPointerException|SQLException e) {
            log.error("Cannot register user " + authData.getLogin(), e);
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
            log.error("Cannot remove task "+ task.getName(),e);
        }

        closeConnection();
    }
}
