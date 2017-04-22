import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.ISO8601;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SA extends BaseAgent {
    Behaviour mainBehaviour;
    int MessagesSent = 0;
    int MaxMessages = 0;

    @Override
    protected void StartTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MessagesSent = 0;
        MaxMessages = NumberOfMessages * AgentsToCommunicate.size();
        addBehaviour(mainBehaviour);

    }


    //Override
    protected void setup() {
        super.setup();
        mainBehaviour = new OneShotBehaviour(this) {
            public void action() {
//                ACLMessage[] messages = createMessages();
//                for (ACLMessage message : messages) {
//                    send(message);
//                }
                createJadeMessages();
                sendMessages();
            }
        };
    }

    private ACLMessage[] createMessages() {
        ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
        for (int i = 0; i < NumberOfMessages; i++) {

            ACLMessage msg;

            if (AmIASpecialAgent && TestModePart2)
                msg = new ACLMessage(ACLMessage.CFP);
            else
                msg = new ACLMessage(ACLMessage.REQUEST);

            msg = addAllAdresses(msg, AgentsToCommunicate.toArray(new AgentCommunication[AgentsToCommunicate.size()]));

            msg.setLanguage("English");
            msg.setOntology("Weather-Forecast");
            msg.setContent(createRandomString(SizeOfMessage) + " " + i);
            msg.hashCode();
            messages.add(msg);
        }
        return messages.toArray(new ACLMessage[messages.size()]);
    }


    private void createJadeMessages() {

        ArrayList<String> messagesContents = new ArrayList<String>();
        for (int i = 0; i < NumberOfMessages; i++) {
            String m = createRandomString(SizeOfMessage);
            messagesContents.add(m);
        }

        //  ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>(NumberOfMessages);
        for (AgentCommunication AC : AgentsToCommunicate) {
            for (int i = 0; i < NumberOfMessages; i++)
                AC.Messages.add(new JadeMessage(i, messagesContents.get(i)));
        }

    }

    private void sendMessages() {
        boolean IsDirty;
        while (true) {
            for (int i = 0; i < NumberOfMessages; i++) {
                SendSingleMessageToAgents(i);
                TryRecieveResponsesFromAgents();
            }
            IsDirty = false;
            for (int i = 0; i < AgentsToCommunicate.size(); i++) {
                if( AgentsToCommunicate.get(i).MessagesReceived < NumberOfMessages)
                    IsDirty = true;
            }
            if(IsDirty == false)
                break;
        }
    }


    private void TryRecieveResponsesFromAgents() {
        while (true) {
            ACLMessage msg;

            msg = receive();
            if (msg == null)
                break;
            if (msg.getPerformative() == ACLMessage.AGREE) {
                ParseResponse(msg);
            }
        }
    }

    private void ParseResponse(ACLMessage msg) {
        String Sender = msg.getSender().toString();
        int SenderIndex = FindSenderIndex(msg);
        String number = msg.getContent().replaceAll("\\D+", "");
        int NumberInt = Integer.parseInt(number);
        AgentCommunication agent = AgentsToCommunicate.get(SenderIndex);
        agent.Messages.get(NumberInt).WasRespondedTo = true;
        agent.MessagesReceived++;
    }

    private int FindSenderIndex(ACLMessage msg) {
        String SenderString = msg.getSender().toString();
        for (int i = 0; i < AgentsToCommunicate.size(); i++) {
            if (AgentsToCommunicate.get(i).Address.AgentAdress == SenderString)
                return i;
        }
        return -1;
    }

    private void SendSingleMessageToAgents(int i) {
        for (AgentCommunication AC : AgentsToCommunicate) {
            if (AC.Messages.get(i).IsSent && AC.Messages.get(i).WasRespondedTo)
                continue;
            ACLMessage msg;
            if (AmIASpecialAgent && TestModePart2)
                msg = new ACLMessage(ACLMessage.CFP);
            else
                msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setLanguage("English");
            msg.setOntology("Weather-Forecast");
            msg.setContent(AC.Messages.get(i).Message + " " + AC.Messages.get(i).Id);
            msg.addReceiver(AC.Address.Receiver);
            msg.hashCode();
            send(msg);
            AC.Messages.get(i).IsSent = true;
        }
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
