package models;

import java.util.ArrayList;

public class Station {
    private ArrayList<CashOffice> offices;
    private ArrayList<Client> clients;
    private ArrayList<LoggingItem> loggingTable;
    private ArrayList<Position> entrances;
    private int entranceCount;
    private int timePerTicket; // in ms
    private int maxClients = 10;
    private boolean canClientsGoIn = true;
    private int width = 0; // in px
    private int height = 0;
    public Station(ArrayList<CashOffice> offices, int entranceCount, int timePerTicket) {
        this.clients = new ArrayList<>();
        this.offices = new ArrayList<>();
        this.offices.addAll(offices);
        this.entranceCount = entranceCount;
        this.timePerTicket = timePerTicket;
    }
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
    public ArrayList<Client> getClients() {
        return clients;
    }
}
