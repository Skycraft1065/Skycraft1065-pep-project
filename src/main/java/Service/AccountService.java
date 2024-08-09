package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    /**
     * Communicates with the DAO to register account to database
     * @param account w/o an ID to register to database
     * @return a registered account if correct, otherwise returns null
     */
    public Account registerAccount(Account account)
    {
        //If the given account's password is longer than 4 and the username isn't blank, proceed
        if (account.password.length() >= 4 && account.username != "")
        {
            return accountDAO.registerAccount(account);
        }
        else
        {
            return null;
        }
    }

    /**
     * Communicates with the DAO to login an account
     * @param account w/o an ID to test against
     * @return the account with ID if correct, otherwise returns null
     */
    public Account loginAccount(Account account)
    {   
        return accountDAO.loginAccount(account);
    }
    
}
