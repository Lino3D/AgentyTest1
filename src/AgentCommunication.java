import java.util.ArrayList;
import java.util.List;

/**
 * Created by michał.grabowski on 19.04.2017.
 */
public class AgentCommunication {
    Address Address;
    int MessagesSend = 0;
    int MessagesReceived = 0;
    List<String> Messages;

    public AgentCommunication(Address address) {
        Address = address;
        Messages = new ArrayList<>();
    }
}
