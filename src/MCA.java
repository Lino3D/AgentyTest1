import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MCA extends BaseAgent {
    private boolean ContinueReceiving = false;
    int Maxcount = 0;
    int ReceivedFromSpecial = 0;
    int counter = 0;
    Behaviour mainBehaviour;
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

    @Override
    protected void StartTask() {
        Maxcount = NumberOfMessages * AgentsToCommunicate.size();
        ReceivedFromSpecial = 0;
        counter = 0;
        addBehaviour(mainBehaviour);
    }

    //Override
    protected void setup() {
        super.setup();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Love this Comment! :D
        //liczba wiadomości * liczba agentów. jestem zbyt śpiący by to teraz ładnie ogarnąć. najlepiej jakaś klasa.


        mainBehaviour = new CyclicBehaviour(this) {



            //Override
            public void action() {
                ACLMessage msg;
                if (TestModePart2 && ReceivedFromSpecial < NumberOfMessages) {
                    msg = receive(mt);
                    if( msg != null)
                        ReceivedFromSpecial++;
                    if (msg == null)
                        msg = receive();
                } else {
                    msg = receive();
                }
                //    MessageTemplate templ


                if (msg != null && counter < Maxcount -1 ) {
//                    MessageQueue messageQueue = createMessageQueue();
//                    messageQueue.addLast(msg);
                    switch (msg.getPerformative()) {
                        case ACLMessage.CFP:
                        case ACLMessage.REQUEST: {
                            System.out.println("Received message: " + msg.getSender().getName());
                            String number = msg.getContent().replaceAll("\\D+", "");
                            System.out.println("Nr:" + number);
                            counter++;
                            System.out.println("Counter " + counter);
                        }
                        default: {
                            break;
                        }
                    }

                }
                // Teoretyczny warunek brzegowy
                if (counter >= Maxcount -1) {
                    // wysylamy raport o zakonczonym zadaniu i konczymy prace
                    addBehaviour(SendSuccessReport);
                    removeBehaviour(mainBehaviour);
                }
            }
        };

    }
}