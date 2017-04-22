import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class MCA extends BaseAgent {
    private boolean ContinueReceiving = false;
    List<AgentCommunication> Senders;
    int Maxcount = 0;
    int ReceivedFromSpecial = 0;
    boolean BeganCounting = false;
    long StartTimer;
    long EndTimer;

    Behaviour mainBehaviour;
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);

    @Override
    protected void StartTask() {
        Maxcount = NumberOfMessages * AgentsToCommunicate.size();
        ReceivedFromSpecial = 0;
        counter = 0;
        Senders = new ArrayList<>();
        addBehaviour(mainBehaviour);
    }

    protected void setup() {
        super.setup();
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        mainBehaviour = new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg;
                if (TestModePart2 && ReceivedFromSpecial < NumberOfMessages) {
                    msg = receive(mt);
                    if (msg != null)
                        ReceivedFromSpecial++;
                    if (msg == null)
                        msg = receive();
                } else {
                    msg = receive();
                }
                if (msg != null && counter < Maxcount) {
                    BeganCounting = false;
                    switch (msg.getPerformative()) {
                        case ACLMessage.CFP:
                        case ACLMessage.REQUEST: {
                            HandleReceivedMessage(msg);
                        }
                        default: {
                            break;
                        }
                    }

                }
                if( msg == null )
                {
                    if(BeganCounting == false)
                    {
                        BeganCounting = true;
                        StartTimer = System.currentTimeMillis();
                    }
                    else
                    {
                        EndTimer = System.currentTimeMillis();
                        if( EndTimer - StartTimer > 5000 ) {
                            addBehaviour(SendSuccessReport);
                            removeBehaviour(mainBehaviour);
                        }
                    }

                }
                // Teoretyczny warunek brzegowy
                if (counter >= Maxcount) {
                    addBehaviour(SendSuccessReport);
                    removeBehaviour(mainBehaviour);
                }
            }
        };

    }


    private void HandleReceivedMessage(ACLMessage msg) {
        // Zapisz jako odczytana
        // Jezeli nieodczytana wczesniej to zwieksz counter
        // wyslij response o dostarczeniu
        String Sender = msg.getSender().toString();
        int SenderIndex = FindSenderIndex(msg);
        String number = msg.getContent().replaceAll("\\D+", "");
        int NumberInt = Integer.parseInt(number);
        AgentCommunication agent = Senders.get(SenderIndex);
        if (!agent.Messages.get(NumberInt).WasReceived) {
            counter++;
         //   System.out.println(number);

            agent.Messages.get(NumberInt).WasReceived = true;
            agent.MessagesSend++;
            if (agent.MessagesSend == NumberOfMessages) {
                System.out.println(msg.getSender().getName() + ": Otrzymałem wszystkie wiadomości od: " + msg.getSender().toString());
            }
        }
      //  SendConfirmationResponse(NumberInt, msg);
    }

    private void SendConfirmationResponse(int number, ACLMessage msg) {
        ACLMessage response = new ACLMessage(ACLMessage.AGREE);
        response.setContent(new Integer(number).toString());
        response.addReceiver(msg.getSender());
        send(response);
    }

    private int FindSenderIndex(ACLMessage msg) {
        String SenderString = msg.getSender().toString();
        for (int i = 0; i < AgentsToCommunicate.size(); i++) {
            if (AgentsToCommunicate.get(i).Address.AgentAdress.equals( SenderString))
                return i;
        }
        AddSender(msg);
        return Senders.size() - 1;
    }

    private void AddSender(ACLMessage msg) {
        Senders.add(new AgentCommunication(new Address("", msg.getSender().toString()),NumberOfMessages));
    }
}