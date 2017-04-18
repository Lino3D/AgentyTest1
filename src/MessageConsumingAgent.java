import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

    public class MessageConsumingAgent extends BaseAgent
    {
        private boolean isFree = false;
        @Override
        protected void StartTask()
        {

        }


    //Override
    protected void setup()
    {
        super.setup();

        //liczba wiadomości * liczba agentów. jestem zbyt śpiący by to teraz ładnie ogarnąć. najlepiej jakaś klasa.
        int Maxcount = super.NumberOfMessages* 1;

        Behaviour mainBehaviour = new CyclicBehaviour(this)
        {int counter = 0;
            //Override
            public void action()
            {
                //System.out.println("My name is " + getAID().getName());
                //System.out.println(!isFree ? "Not free" : "Free");
              //  if (isFree) isFree = false;
               // else isFree = true;
                ACLMessage msg = receive();

                if(msg!=null && counter < Maxcount)
                {
                    ACLMessage response = msg.createReply();
                    //System.out.println("dostalem wiadomosc");

                    response.setPerformative(ACLMessage.INFORM);
                    response.setContent("Hello my honey");
                    System.out.println("Received message: " + msg.getContent() );
                    String number = msg.getContent().replaceAll("\\D+","");
                    System.out.println("Nr:" + number  );
                    counter++;
                    send(response);
                }
            }
        };
        addBehaviour(mainBehaviour);
    }
}