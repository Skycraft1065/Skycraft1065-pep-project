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

    public Message postMessage(Message message)
    {
        if(message.getMessage_text().length() <= 255 && message.getMessage_text() != "")
        {
            return messageDAO.postMessage(message);
        }
        else
        {
            return null;
        }
    }

    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(String Id)
    {
        int id = Integer.valueOf(Id);

        return messageDAO.getMessageById(id);
    }

    public Message updateMessage(int message_id, Message newMessage)
    {
        String message = newMessage.getMessage_text();
        if(messageDAO.getMessageById(message_id) == null || message == "" || message.length() > 255)
        {
            return null;
        }
        
        messageDAO.updateMessage(message_id, newMessage);

        return messageDAO.getMessageById(message_id);
    }
    
}
