import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpammerAgent extends BaseAgent {
    Behaviour mainBehaviour;

    @Override
    protected void StartTask() {
        addBehaviour(mainBehaviour);
    }


    //Override
    protected void setup() {
        super.setup();

        // TODO: Tutaj lepiej zrobic oneshotBehaviour
        // TODO: Pytanie czy czekamy na response czy zakladamy ze jak poszlo to poszlo i wysylamy kolejna ture?
        mainBehaviour = new CyclicBehaviour(this) {
            //Override
            public void action() {
                //send(createMessage(1)[0]);

                ACLMessage[] messages = createMessages();
                for (ACLMessage message : messages) {
                    send(message);
                }
                ACLMessage response = receive();
                if (response != null) {
                    System.out.println("GGood jooob");
                }
            }
        };
        //   addBehaviour(mainBehaviour);
    }

    private ACLMessage[] createMessages() {

        ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();

        // TODO: to addresses trzeba zamienic na liste
        // AgentsToCommunicate
        Addresses = new ArrayList<>();
        Addresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc", "James@192.168.56.1:1099/JADE"));
        for (int i = 0; i < super.NumberOfMessages; i++) {

            Address jamesAddress = new Address("http://192.168.1.112:7778/acc", "James@192.168.1.100:1099/JADE");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

            // to można zastąpić przez zakomentowany kod
            msg = addAllAdresses(msg, jamesAddress);
            //msg = addAllAdresses(msg, (Address[]) Addresses.toArray());

            msg.setLanguage("English");
            msg.setOntology("Weather-Forecast");
            msg.setContent(createRandomString(super.SizeOfMessage) + " " + i);
            msg.hashCode();
            messages.add(msg);
        }
        return messages.toArray(new ACLMessage[0]);
    }

    private ACLMessage addAllAdresses(ACLMessage msg, Address... adresses) {
        for (Address address : adresses) {
            AID receiver = new AID(address.AgentAdress, AID.ISGUID);
            receiver.addAddresses(address.ComputerAdress);
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
