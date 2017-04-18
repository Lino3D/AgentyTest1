import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * Created by Mike on 18.04.2017.
 */
public class BaseAgent  extends Agent{
    Behaviour MainLoop;
    boolean TestStarted = false;
    protected void setup() {
        MainLoop = new TickerBehaviour(this,3000) {
            @Override
            protected void onTick() {
                ACLMessage msg = receive();
                if(msg!=null) {
                    switch(msg.getPerformative())
                    {
                        case ACLMessage.CONFIRM: {
                            //if( msg.getContent() == "Start")
                            {
                                // TODO: Sprawdzanie, czy juz wczensiej nie bylo wystartowen
                                // ALe do tego bedziemy potrzebowac TestStarted dawac na false, jak sie zakonczy
                                // Czekamy wiadomosc od naszego EMA, jak dostaniemy, to robimy
                                TestStarted = true;
                                System.out.println("Startuje swoje zadanie! " + getAID());
                                StartTask();
                            }
                        }
                        default:
                        {

                            break;

                        }
                    }

                }
            }
        };
        addBehaviour(MainLoop);
    }

    protected void StartTask() {
    }

}
