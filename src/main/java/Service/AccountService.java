package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

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

    public Account registerAccount(Account account)
    {
        if (account.password.length() >= 4 && account.username != "")
        {
            return accountDAO.registerAccount(account);
        }
        else
        {
            return null;
        }
    }

    public Account loginAccount(Account account)
    {
        return accountDAO.loginAccount(account);
    }
    
}
