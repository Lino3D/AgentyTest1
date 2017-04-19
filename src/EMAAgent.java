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
public class EMAAgent extends Agent {
    List<Address> Addresses;
    List<Address> SpammerAddresses;
    long StartTime;
    long EndTime;
    int ReceivedSuccessReports = 0;
    int SizeOfMessage = 10;
    int NumberOfMessages = 10;
    Behaviour StartExperiment;
    Behaviour ReceiveEndReports;

    protected void setup() {
        Addresses = new ArrayList<>();
        Addresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc", "Master@192.168.90.107:1099/JADE"));
        Addresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc", "John@192.168.90.107:1099/JADE"));

        SpammerAddresses = new ArrayList<>();
        SpammerAddresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc", "Bob@192.168.90.107:1099/JADE"));

        StartExperiment = new OneShotBehaviour() {
            @Override
            public void action() {
                ReceivedSuccessReports = 0;
                addBehaviour(ReceiveEndReports);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StartTime = System.currentTimeMillis();
                ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
                for (Address x : Addresses) {
                    msg = AddReceiver(msg, x);
                }
                for(Address x : SpammerAddresses){
                    msg = AddReceiver(msg,x);
                }

                msg.setLanguage("English");
                msg.setOntology("Weather-Forecast");
                msg.setContent(PrepareContent());
                msg.hashCode();
                send(msg);
            }
        };
        addBehaviour(StartExperiment);

        ReceiveEndReports = new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    if( msg.getPerformative() == ACLMessage.INFORM)
                    {
                        ReceivedSuccessReports++;
                        if( ReceivedSuccessReports == Addresses.size() -1 )
                        {
                            EndTime = System.currentTimeMillis();
                            long estimatedTime = EndTime - StartTime;
                            String Podsumowanie = String.valueOf(estimatedTime);
                        }
                    }
                }
            }
        };
    }

    private ACLMessage AddReceiver(ACLMessage msg, Address AgentAddress) {
        AID receiver = new AID(AgentAddress.AgentAdress, AID.ISGUID);
        receiver.addAddresses(AgentAddress.ComputerAdress);

        msg.addReceiver(receiver);

        return msg;
    }

    private String PrepareContent() {
        String RetValue = "";
        RetValue += new Integer(SizeOfMessage).toString() + ";";
        RetValue += new Integer(NumberOfMessages).toString() + ";";

        for (Address x : Addresses) {
            RetValue += x.AgentAdress + ";";
            RetValue += x.ComputerAdress + ";";
        }

        return RetValue;
    }

}
