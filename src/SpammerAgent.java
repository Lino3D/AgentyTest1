import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jakub on 09.03.2017.
 */
public class SpammerAgent extends BaseAgent
{
    Behaviour SpamMessage;
    Behaviour mainBehaviour;
    @Override
    protected void StartTask()
    {
        // Startujemy Cyclica do spamowania wiadomosci
    //    addBehaviour(SpamMessage);
        addBehaviour(mainBehaviour);
    }







    private boolean isFree = false;
    //Override
    protected void setup()
    {
      super.setup();
//        SpamMessage = new CyclicBehaviour() {
//            @Override
//            public void action() {
//                // Spamujemy sobie
//            }
//        };
        mainBehaviour = new CyclicBehaviour(this)
        {
            //Override
            public void action()
            {
                //send(createMessage(1)[0]);

                ACLMessage[] messages = createMessages(2);
                for(ACLMessage message : messages)
                {
                    send(message);
                }
                ACLMessage response = receive();
                if(response!=null)
                {
                    System.out.println("GGood jooob");
                }
            }
                    };
                  //  addBehaviour(mainBehaviour);
            }
    private ACLMessage[] createMessages(int messageNumber)
        {
            ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
            for(int i=0; i< messageNumber; i++) {
                Address jamesAddress = new Address("http://192.168.1.112:7778/acc", "James@192.168.1.100:1099/JADE");
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg = addAllAdresses(msg, jamesAddress);
                msg.setLanguage("English");
                msg.setOntology("Weather-Forecast");
                msg.setContent(createRandomString(25));
                msg.hashCode();
                messages.add(msg);
            }
            return messages.toArray(new ACLMessage[0]);
        }
    private ACLMessage addAllAdresses(ACLMessage msg,Address... adresses)
    {
        for(Address address : adresses)
        {
            AID receiver = new AID(address.AgentAdress, AID.ISGUID);
            receiver.addAddresses(address.ComputerAdress);
            msg.addReceiver(receiver);
        }
        return msg;
    }
    private String createRandomString(int stringSize)
    {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
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
