package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    public MessageService()
    {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO;
    }

    /**
     * Communicates with the DAO to post a message to the database
     * @param message w/o an ID to add to the database
     * @return the posted message if its contents follow req. criteria, otherwise null
     */
    public Message postMessage(Message message)
    {
        //If the message text is less than or equal to 255 and the text isn't blank, proceed
        if(message.getMessage_text().length() <= 255 && message.getMessage_text() != "")
        {
            return messageDAO.postMessage(message);
        }
        else
        {
            return null;
        }
    }

    /**
     * Communicates with the DAO to get all messages from the database
     * @return a list containing all messages. Can be empty
     */
    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    /**
     * Communicates with the DAO to get a message by a given ID
     * @param id used to find the message to get
     * @return the message object if found, otherwise null
     */
    public Message getMessageById(int id)
    {
        return messageDAO.getMessageById(id);
    }

    /**
     * Communicates with the DAO to update a message using an ID and a provided new message
     * @param id used to find the message to update
     * @param newMessage the message to be persisted. Must follow certain criteria
     * @return the updated message object if persisted, otherwise returns null
     */
    public Message updateMessage(int id, Message newMessage)
    {
        String message = newMessage.getMessage_text();
        
        //If the given ID is null or the message is blank or is greater than 255
        if(messageDAO.getMessageById(id) == null || message == "" || message.length() > 255)
        {
            return null;
        }

        messageDAO.updateMessage(id, newMessage);

        return messageDAO.getMessageById(id);
    }

    /**
     * Communicates with the DAO to get all messages posted by a given account ID
     * @param posted_by acts as the account_id for a message and is used to identify messages posted by that account
     * @return a list of messages posted by the account. Can be empty
     */
    public List<Message> getAllMessagesByAccount(int posted_by)
    {
        return messageDAO.getAllMessagesByAccount(posted_by);
    }

    /**
     * Communicates with the DAO to delete a message from the database
     * @param id used to find the message to delete
     * @return the deleted message if persisted, otherwise null
     */
    public Message deleteMessage(int id)
    {
        Message oldMessage = messageDAO.getMessageById(id);

        //If the message actually exists
        if(oldMessage != null)
        {
            messageDAO.deleteMessage(id);
        }

        return oldMessage;
    }
    
}
