import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MessageConsumingAgent extends BaseAgent {
    private boolean ContinueReceiving = false;
    int Maxcount = 0;
    Behaviour mainBehaviour;

    @Override
    protected void StartTask() {
         Maxcount = NumberOfMessages *AgentsToCommunicate.size();
        addBehaviour(mainBehaviour);
    }

    //Override
    protected void setup() {
        super.setup();

        // Love this Comment! :D
        //liczba wiadomości * liczba agentów. jestem zbyt śpiący by to teraz ładnie ogarnąć. najlepiej jakaś klasa.


        mainBehaviour = new CyclicBehaviour(this) {
            int counter = 0;

            //Override
            public void action() {

                ACLMessage msg = receive();
            //    MessageTemplate templ



                if (msg != null && counter < Maxcount) {
                    switch (msg.getPerformative()) {
                    case ACLMessage.REQUEST: {
                        System.out.println("Received message: " + msg.getContent());
                        String number = msg.getContent().replaceAll("\\D+", "");
                        System.out.println("Nr:" + number);
                        counter++;
                    }
                        default:{
                            break;
                        }
                    }

                }
                // Teoretyczny warunek brzegowy
                if (counter >= Maxcount) {
                    // wysylamy raport o zakonczonym zadaniu i konczymy prace
                    addBehaviour(SendSuccessReport);
                    removeBehaviour(mainBehaviour);
                }
            }
        };

    }
}