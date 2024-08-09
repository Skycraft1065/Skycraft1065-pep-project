package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    
    
    /**
     * Initializes the different Uris
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);

        return app;
    }

    /**
     * 
     * Handles registering (creating) a new Account
     * @param ctx handles HTTP requests
     * @throws JsonProcessingException
     * 
     */
    private void postRegisterHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        
        Account account = mapper.readValue(ctx.body(), Account.class);
        
        Account addedAccount = accountService.registerAccount(account);
        
        //If the account isn't null, proceed. Otherwise, client error
        if(addedAccount!=null)
        {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
        else
        {
            ctx.status(400);
        }
    }

    /**
     * Handles login requests
     * @param ctx handles HTTP requests
     * @throws JsonProcessingException
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        
        Account loginAttempt = mapper.readValue(ctx.body(), Account.class);
        
        Account account = accountService.loginAccount(loginAttempt);
        
        //If the account isn't null, and the username and password match on both accounts, proceed. Otherwise, access denied
        if(account != null && account.getUsername().equals(loginAttempt.getUsername()) && account.getPassword().equals(loginAttempt.getPassword()))
        {
            ctx.json(mapper.writeValueAsString(account));
        }
        else
        {
            ctx.status(401);
        }
    }

    /**
     * Handles posting new messages to the database
     * @param ctx
     * @throws JsonProcessingException
     */
    private void postMessagesHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        Message addedMessage = messageService.postMessage(message);
        
        //If the message isn't null, proceed. Otherwise, client error
        if(addedMessage!=null)
        {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
        else
        {
            ctx.status(400);
        }
    }

    /**
     * Handles getting all messages from the database
     * @param ctx handles HTTP requests
     */
    private void getAllMessagesHandler(Context ctx)
    {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handles getting a message using a given ID
     * @param ctx handles HTTP requests
     */
    private void getMessageByIdHandler(Context ctx)
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);

        //If the message isn't null, show to json. Otherwise, leave empty
        if (message != null)
        {
            ctx.json(message);
        }
        else
        {
            ctx.json("");
        }
    }

    /**
     * Handles deleting a message from the database
     * @param ctx handles HTTP requests
     */
    private void deleteMessageHandler(Context ctx)
    {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);

        //If the message isn't null, show to json. Otherwise, leave empty
        if (message != null)
        {
            ctx.json(message);
        }
        else
        {
            ctx.json("");
        }
    }

    /**
     * Handles updating messages in the database
     * @param ctx handles HTTP requests
     * @throws JsonProcessingException
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);

        //If the message isn't null, show to json. Otherwise, client error
        if(updatedMessage != null)
        {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
        else
        {
            ctx.status(400);
        }
    }

    /**
     * Handles retrieving all messages by a specific account
     * @param ctx handles HTTP requests
     */
    private void getAllMessagesByAccountHandler(Context ctx)
    {
        int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccount(posted_by));
    }




}