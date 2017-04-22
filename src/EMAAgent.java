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
    boolean TestModePart2 = false;
    long StartTime;
    long EndTime;
    int ReceivedSuccessReports = 0;
    int SizeOfMessage = 100;
    int NumberOfMessages = 2000;
    List<String> ReceivedMessages;
    Behaviour StartExperiment;
    Behaviour ReceiveEndReports;

    protected void setup() {
        Addresses = new ArrayList<>();
        ReceivedMessages = new ArrayList<>();
        // First must be a Master address
        Addresses.add(new Address("http://10.0.0.201:7778/acc", "Master@10.0.0.201:1099/JADE"));
        // The rest belongs to the receivers
        Addresses.add(new Address("http://10.0.0.201:7778/acc", "MichalMCA@10.0.0.201:1099/JADE"));
        Addresses.add(new Address("http://10.0.2.230:7778/acc", "OknoRec@10.0.2.230:1099/JADE"));
        //ddresses.add(new Address("http://192.168.56.1:7778/acc", "MichalSA@192.168.56.1:1099/JADE"));

        SpammerAddresses = new ArrayList<>();
        SpammerAddresses.add(new Address("http://10.0.0.201:7778/acc", "MichalSA@10.0.0.201:1099/JADE"));
        SpammerAddresses.add(new Address("http://10.0.2.230:7778/acc", "OknoSpam@10.0.2.230:1099/JADE"));
        //SpammerAddresses.add(new Address("http://192.168.90.100:7778/acc", "SADell@192.168.90.100:1099/JADE"));

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
                        ReceivedMessages.add(msg.getSender().getName()+ ": "+msg.getContent());
                        ReceivedSuccessReports++;
                        if( ReceivedSuccessReports == Addresses.size() -1 )
                        {
                            EndTime = System.currentTimeMillis();
                            long estimatedTime = EndTime - StartTime;
                            String Podsumowanie = String.valueOf(estimatedTime);

                            System.out.println("#################################################");
                            System.out.println("Zakończyłem prace!");
                            System.out.println("Ilosc agentów: " + (Addresses.size() -1));
                            System.out.println("Ilosc wiadomości do wysłania: " + NumberOfMessages);
                            System.out.println("Wielkość wiadomości: " + SizeOfMessage);
                            for( int j =0 ;j < ReceivedMessages.size(); j++)
                                System.out.println(ReceivedMessages.get(j));

                            System.out.println("Czas trwania symulacji: " + estimatedTime);
                            System.out.println("#################################################");
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
        if( TestModePart2)
            RetValue += "1;";
        else
            RetValue += "0;";

        RetValue += SpammerAddresses.get(0).AgentAdress +";";
        RetValue += SpammerAddresses.get(0).ComputerAdress + ";";

        for (Address x : Addresses) {
            RetValue += x.AgentAdress + ";";
            RetValue += x.ComputerAdress + ";";
        }

        return RetValue;
    }

}
