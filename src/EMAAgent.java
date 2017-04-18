import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SenderBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mike on 18.04.2017.
 */
public class EMAAgent  extends Agent{
    List<Address> Addresses;
    int SizeOfMessage = 10;
    int NumberOfMessages = 10;

    protected void setup(){
        Addresses = new ArrayList<>();
        Addresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc","James@192.168.56.1:1099/JADE"));
        Behaviour StartExperiment = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                for(Address x : Addresses)
                {
                    msg = AddReceiver(msg,x);
                }
                msg.setLanguage("English");
                msg.setOntology("Weather-Forecast");
                msg.setContent("Start");
                msg.hashCode();
            }
        };
    }

    private ACLMessage AddReceiver(ACLMessage msg, Address AgentAddress)
    {
        AID receiver = new AID( AgentAddress.AgentAdress , AID.ISGUID);
        receiver.addAddresses(AgentAddress.ComputerAdress);

        msg.addReceiver(receiver);

        return msg;
    }

}
