import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MessageConsumingAgent extends BaseAgent {
    private boolean ContinueReceiving = false;
    Behaviour mainBehaviour;

    @Override
    protected void StartTask() {
        addBehaviour(mainBehaviour);
    }

    //Override
    protected void setup() {
        super.setup();

        // Love this Comment! :D
        //liczba wiadomości * liczba agentów. jestem zbyt śpiący by to teraz ładnie ogarnąć. najlepiej jakaś klasa.
        int Maxcount = NumberOfMessages *1;

        mainBehaviour = new CyclicBehaviour(this) {
            int counter = 0;



            //Override
            public void action() {

                ACLMessage msg = receive();




                if (msg != null && counter < Maxcount) {
//                    MessageQueue messageQueue = createMessageQueue();
//                    messageQueue.addLast(msg);
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