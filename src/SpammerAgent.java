import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpammerAgent extends BaseAgent {
    Behaviour mainBehaviour;
    int MessagesSent = 0;
    int MaxMessages = 0;

    @Override
    protected void StartTask() {
        addBehaviour(mainBehaviour);
        MessagesSent = 0;
        MaxMessages = NumberOfMessages * AgentsToCommunicate.size();
    }


    //Override
    protected void setup() {
        super.setup();
        mainBehaviour = new OneShotBehaviour(this) {
            public void action() {
                ACLMessage[] messages = createMessages();
                for (ACLMessage message : messages) {
                    send(message);
                }
            }
        };
    }

    private ACLMessage[] createMessages() {
        ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
        for (int i = 0; i < NumberOfMessages; i++) {

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

            msg = addAllAdresses(msg,  AgentsToCommunicate.toArray(new AgentCommunication[AgentsToCommunicate.size()]));

            msg.setLanguage("English");
            msg.setOntology("Weather-Forecast");
            msg.setContent(createRandomString(SizeOfMessage) + " " + i);
            msg.hashCode();
            messages.add(msg);
        }
        return messages.toArray(new ACLMessage[messages.size()]);
    }

    private ACLMessage addAllAdresses(ACLMessage msg, AgentCommunication... adresses) {
        for (AgentCommunication address : adresses) {
            AID receiver = new AID(address.Address.ComputerAdress, AID.ISGUID);
            receiver.addAddresses(address.Address.AgentAdress);
            msg.addReceiver(receiver);
        }
        return msg;
    }

    private String createRandomString(int stringSize) {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTW".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < stringSize; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }


}
