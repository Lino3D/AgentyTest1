import java.util.ArrayList;
import java.util.List;

/**
 * Created by michał.grabowski on 19.04.2017.
 */
public class AgentCommunication {
    Address Address;
    int MessagesSend = 0;
    int MessagesReceived = 0;
    Boolean WasDealtWith = false;
    List<JadeMessage> Messages;

    public AgentCommunication(Address address, int NumberOfMessages) {
        Address = address;
        Messages = new ArrayList<>();
        for(int i = 0; i < NumberOfMessages; i++)
            Messages.add(new JadeMessage(i,""));
    }
    public AgentCommunication(Address address) {
        Address = address;
        Messages = new ArrayList<>();
    }
}
