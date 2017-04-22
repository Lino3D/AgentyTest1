/**
 * Created by michał.grabowski on 22.04.2017.
 */
public class JadeMessage {
public JadeMessage(int id, String message) {
        Id = id;
        Message = message;
        IsSent = false;
        WasReceived = false;
        WasRespondedTo = false;
        }

public int Id;
public String Message;
public Boolean IsSent;
public Boolean WasReceived;
public Boolean WasRespondedTo;
        }
