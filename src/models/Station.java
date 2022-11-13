package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Station {
    private List<CashOffice> offices;
    private int entranceCount;
    private int timePerTicket; // in ms
    private int maxClients = 10;

    public Station(){
        this.offices = new ArrayList<CashOffice>();
        entranceCount = 1;
        timePerTicket = 1000;
    }

    public Station(ArrayList<CashOffice> offices, int entranceCount, int timePerTicket) {
        this.offices = new ArrayList<CashOffice>(offices);
        this.entranceCount = entranceCount;
        this.timePerTicket = timePerTicket;
    }

    public void addCashOffice(CashOffice office){
        offices.add(office);
    }
    public List<CashOffice> getCashOffices(){
        return Collections.unmodifiableList(offices);
    }

    public void setEntranceCount(int entranceCount) {
        this.entranceCount = entranceCount;
    }

    public void setTimePerTicket(int timePerTicket) {
        this.timePerTicket = timePerTicket;
    }
    public int getEntranceCount(){
        return entranceCount;
    }
    public int getTimePerTicket(){
        return timePerTicket;
    }
}
