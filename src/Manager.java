import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SenderBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by Jakub on 09.03.2017.
 */
public class Manager extends Agent
{
    private boolean isFree = false;
    //Override
    protected void setup()
    {
        Behaviour mainBehaviour = new CyclicBehaviour(this)

        {
            //Override
            public void action()
            {
              //  System.out.println("My name is " + getAID().getName());
                //System.out.println(!isFree ? "Not free" : "Free");
               // if (isFree) isFree = false;
               // else isFree = true;
                send(createMessage());
                ACLMessage response = receive();
                if(response!=null)
                {
                    System.out.println("GGood jooob");
                    System.out.println(response.getContent());
                }
            }
        };
                addBehaviour(mainBehaviour);
    }
    private ACLMessage createMessage()
    {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID receiverJames = new AID( "James@192.168.1.112:1099/JADE", AID.ISGUID);
        receiverJames.addAddresses("http://192.168.1.112:7778/acc");

        // msg.addReceiver(new AID("James", AID.ISLOCALNAME));
        msg.addReceiver(receiverJames);
        msg.setLanguage("English");
        msg.setOntology("Weather-Forecast");
        msg.setContent("Today is raining");
        return msg;
    }
}
