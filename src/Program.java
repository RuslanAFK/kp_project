import models.Client;

import java.util.ArrayList;

public class Program {
    private ArrayList<Client> clients;

    private int maxClients = 10;
    private boolean canClientsGoIn = true;
    public boolean addClient() {
        if(clients.size() == maxClients) {
            return canClientsGoIn = false;
        }
        else if(!canClientsGoIn && clients.size() > 0.7*maxClients) {
            return false;
        }
        canClientsGoIn = true;
        //var client = new Client();
        //clients.add(client);
        return true;
    }
}
