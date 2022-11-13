package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Station {
    private List<CashOffice> offices;
    private List<Position> entrances;
    private List<Client> clients;
    private int timePerTicket; // in ms
    private int maxClients = 10;

    public Station(){
        this.offices = new ArrayList<CashOffice>();
        entrances = new ArrayList<Position>();
        entrances.add(new Position(400-25, 591));
        clients = new ArrayList<Client>();
        clients.add(new Client(1, 1, new Position(100,100), Status.NONE));
        timePerTicket = 1000;
    }


    public void addClient(Client client){
        clients.add(client);
    }
    public List<Client> getClients(){
        return Collections.unmodifiableList(clients);
    }

    public void setEntranceCount(int count){
        if(entrances.size() != count) {
            entrances.clear();
            int space = 800/(count+1);
            for (int i = 1; i <= count; i++){
                entrances.add(new Position(space*i-25, 591));
            }
        }
    }
    public Position getEntrancePosition(int index){
        return entrances.get(index);
    }
    public int getEntranceCount(){
        return entrances.size();
    }

    public Station(ArrayList<CashOffice> offices, int entranceCount, int timePerTicket) {
        this.offices = new ArrayList<CashOffice>(offices);
        this.timePerTicket = timePerTicket;
    }

    public void addCashOffice(CashOffice office){
        offices.add(office);
    }
    public List<CashOffice> getCashOffices(){
        return Collections.unmodifiableList(offices);
    }


    public void setTimePerTicket(int timePerTicket) {
        this.timePerTicket = timePerTicket;
    }
    public int getTimePerTicket(){
        return timePerTicket;
    }
}
