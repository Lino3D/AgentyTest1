import jade.core.AID;

/**
 * Created by Mike on 18.04.2017.
 */
public class Address {
    public String ComputerAdress;
    public String AgentAdress;

    public AID Receiver;

    public Address(String computerAdress, String agentAdress) {
        ComputerAdress = computerAdress;
        AgentAdress = agentAdress;
        AID receiver = new AID(this.ComputerAdress, AID.ISGUID);
        receiver.addAddresses(this.AgentAdress);
        this.Receiver = receiver;
    }

}
