package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    

    /**
     * Registers an account object to the database
     * @param account w/o an ID to be registered
     * @return the account with ID if registered, otherwise null
     */
    public Account registerAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if(pkeyResultSet.next())
            {
                int generated_author_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_author_id, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to select the correct account for login using account object w/o an ID
     * @param account username and password w/o an ID used for finding an account and logging in
     * @return account with ID if found, otherwise null
     */
    public Account loginAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try 
        {    
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Account retrievedAccount = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return retrievedAccount;
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }



}
