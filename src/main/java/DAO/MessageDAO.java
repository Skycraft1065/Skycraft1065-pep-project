package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * Inserts a given message object into the database
     * @param message object w/o an ID to be added
     * @return message added if persisted, otherwise null
     */
    public Message postMessage(Message message)
    {
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if(pkeyResultSet.next())
            {
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_author_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gets all messages from the database
     * @return list of all messages. Can be empty
     */
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try 
        {
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Gets a message from the database by ID
     * @param id used to find the message to get
     * @return the message object with data from the database if it exists, or null if not
     */
    public Message getMessageById(int id)
    {
        Connection connection = ConnectionUtil.getConnection();
        Message message = new Message();

        try 
        {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Updates a message in the database with new text
     * @param message_id used to find the message in the database to update
     * @param message contains the message by which to replace the old with
     */
    public void updateMessage(int message_id, Message message)
    {
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets all the messages posted by a specific account
     * @param posted_by the ID by which the messages are identified
     * @return a list of messages. Can be empty
     */
    public List<Message> getAllMessagesByAccount(int posted_by)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try 
        {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            System.out.println(posted_by);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, posted_by);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                System.out.println(message.toString());
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }
    
    /**
     * Deletes a message from the database
     * @param message_id the ID to be deleted
     */
    public void deleteMessage(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();

        try
        {
            String sql = "DELETE FROM Message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
