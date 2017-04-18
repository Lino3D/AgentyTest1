import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MessageConsumingAgent extends Agent
{
    private boolean isFree = false;
    //Override
    protected void setup()
    {
        Behaviour mainBehaviour = new OneShotBehaviour(this)

        {
            //Override
            public void action()
            {
                //System.out.println("My name is " + getAID().getName());
                //System.out.println(!isFree ? "Not free" : "Free");
              //  if (isFree) isFree = false;
               // else isFree = true;
                ACLMessage msg = receive();
                if(msg!=null)
                {
                    ACLMessage response = msg.createReply();
                    //System.out.println("dostalem wiadomosc");
                    switch(msg.getPerformative())
                    {

                        case ACLMessage.REQUEST:
                        {
                            response.setPerformative(ACLMessage.INFORM);
                            response.setContent("Hello my honey");
                            System.out.println("Sent to my boss");
                            break;
                        }
                        default:
                        {
                            response.setPerformative(ACLMessage.INFORM);
                            response.setContent("Hello my honey");
                            break;

                        }
                    }
                    send(response);
                }
            }

        };
        addBehaviour(mainBehaviour);
    }
}