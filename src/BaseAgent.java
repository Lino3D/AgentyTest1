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
    protected void setup() {
        MainLoop = new TickerBehaviour(this,300) {
            @Override
            protected void onTick() {
                // Czekamy wiadomosc od naszego EMA, jak dostaniemy, to robimy
                StartTask();
            }
        };
        addBehaviour(MainLoop);
    }

    protected void StartTask() {
    }

}
