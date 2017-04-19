import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mike on 18.04.2017.
 */
public class BaseAgent extends Agent {
    Behaviour SendSuccessReport;
    Address MasterAgentAddress;
    List<AgentCommunication> AgentsToCommunicate;
    List<Address> Addresses;
    int SizeOfMessage = 10;
    int NumberOfMessages = 10;
    Behaviour MainLoop;
    boolean TestStarted = false;

    protected void setup() {
        MainLoop = new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                ACLMessage msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.CONFIRM: {
                            //if( msg.getContent() == "Start")
                            {
                                // TODO: Sprawdzanie, czy juz wczensiej nie bylo wystartowen
                                // ALe do tego bedziemy potrzebowac TestStarted dawac na false, jak sie zakonczy
                                // Czekamy wiadomosc od naszego EMA, jak dostaniemy, to robimy
                                CreateCommunicationArray(msg.getContent());
                                TestStarted = true;
                                System.out.println("Startuje swoje zadanie! " + getAID());
                                StartTask();
                            }
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        };
        addBehaviour(MainLoop);

        SendSuccessReport = new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setLanguage("English");
                message.setOntology("Test-Ending");
                message.setContent("Job done!");
                AID receiver = new AID(MasterAgentAddress.AgentAdress,AID.ISGUID);
                receiver.addAddresses(MasterAgentAddress.ComputerAdress);
                message.addReceiver(receiver);
                send(message);
            }
        };
    }

    private void CreateCommunicationArray(String message) {
        String[] parts = message.split(";");
        SizeOfMessage = Integer.parseInt(parts[0], 10);
        NumberOfMessages = Integer.parseInt(parts[1], 10);
        MasterAgentAddress = new Address(parts[2], parts[3]);
        if (AgentsToCommunicate != null)
            AgentsToCommunicate.clear();
        else
            AgentsToCommunicate = new ArrayList<>();
        for (int i = 4; i < parts.length; i += 2) {
            AgentsToCommunicate.add(new AgentCommunication(new Address(parts[5], parts[6])));
        }

    }

    protected void StartTask() {
    }

}
