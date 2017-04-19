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
    Behaviour StartExperiment;
    Behaviour ReceiveEndReports;

    protected void setup(){
        Addresses = new ArrayList<>();
     //   Addresses.add(new Address("http://DESKTOP-G6IPDM6:7778/acc","James@192.168.56.1:1099/JADE"));
        Addresses.add(new Address("http://DESKTOP-IJG535C.am.edu.pl:7778/acc","Bob@192.168.56.1:1099/JADE"));

         StartExperiment = new OneShotBehaviour() {
            @Override
            public void action() {
                // Dodajemy event czekania na odpowiedz
                addBehaviour(ReceiveEndReports);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
                for(Address x : Addresses)
                {
                    msg = AddReceiver(msg,x);
                }
                msg.setLanguage("English");
                msg.setOntology("Weather-Forecast");
                msg.setContent(PrepareContent());
                msg.hashCode();
                // TODO: Wykomentowac pozniej
                // TODO: A swoją drogą to to się będzie gryźć bo nie przekazujemy tego w tabeli adresatów
                msg.addReceiver(new AID("Sam",AID.ISLOCALNAME));
                send(msg);
            }
        };
        addBehaviour(StartExperiment);

        ReceiveEndReports = new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if( msg != null)
                {
                    // Zliczanie i raportowanie informacji o zakonczonych zadaniach

                }
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

    private String PrepareContent()
    {
        String RetValue = "";
        RetValue += new Integer(SizeOfMessage).toString() +";";
        RetValue += new Integer(NumberOfMessages).toString() +";";

        for( Address x : Addresses)
        {
            RetValue += x.AgentAdress +";";
            RetValue += x.ComputerAdress+";";
        }

        return RetValue;
    }

}
