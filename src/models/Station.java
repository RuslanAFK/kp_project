package models;

import java.util.ArrayList;

public class Station {
    private ArrayList<CashOffice> offices;
    private int entranceCount;
    private int timePerTicket; // in ms
    private int maxClients = 10;
    public Station(ArrayList<CashOffice> offices, int entranceCount, int timePerTicket) {
        this.offices = new ArrayList<>();
        this.offices.addAll(offices);
        this.entranceCount = entranceCount;
        this.timePerTicket = timePerTicket;
    }

}
