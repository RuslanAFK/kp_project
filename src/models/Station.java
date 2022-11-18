package models;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//станція яка має список всіх кас, всіх клієнтів, позиції входів і ше якісь параметри незначні
//внизу розпишу по методах конкретно
public class Station {
    private boolean isBlocked = false;
    private final List<CashOffice> offices;
    private final List<Position> entrances;
    private final List<Client> clients;
    private final List<LoggingItem> loggingTable;
    private int timePerTicket = 1000; // in ms
    public Station(){
        this.offices = new ArrayList<>();
        entrances = new ArrayList<>();
        entrances.add(new Position(400-25, 591));
        clients = new ArrayList<>();
        loggingTable = new ArrayList<>();
    }

    public int getMaxClients() {
        return 20;
    }
    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public List<LoggingItem> getLoggingTable() {
        return loggingTable;
    }

    // every 50 ms updates offices that they can start selling tickets
    public void notifyForSelling(){
        Timer timer = new Timer();
        var station = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (CashOffice office : offices) {
                    if(!office.isDisabled() && office.isFree()){
                        office.updateForSelling(station);
                    }
                }
            }
        }, 0, 50);

    }

    // pops first client of the station and the queue
    public void deleteFirstClient(CashOffice office) {
        if(!office.isDisabled()){
            var client = office.sellTicket();
            if(client != null) {

                clients.remove(client);

                // create log in logging table
                try {
                    for(var item: loggingTable) {
                        if(item.getClientId() == client.getUniqueId()) {
                            item.setEndTime();
                            logTable();
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Can't modify now");
                }

                System.out.println("\nClient removed: " + client.getPosition().toString());
            }
        }
    }

    //задаю кількість входів а метод сам рахує їхнє місцеположення;
    public void setEntranceCount(int count){
        if(entrances.size() != count) {
            entrances.clear();
            int space = 800/(count+1);
            for (int i = 1; i <= count; i++){
                entrances.add(new Position(space*i-25, 591));
            }
        }
    }
    public int getCashOfficeIndex(CashOffice cashOffice){
        return offices.indexOf(cashOffice);
    }

    public void addClient(Client client){
        clients.add(client);
    }
    public List<Client> getClients(){
        return Collections.unmodifiableList(clients);
    }


    public Position getEntrancePosition(int index){
        return entrances.get(index);
    }
    public int getEntranceCount(){
        return entrances.size();
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

    // повертає список касс на технічній перерві
    public List<CashOffice> getTechnicCashOffice(){
        return this.offices.stream().filter(CashOffice::isDisabled).collect(Collectors.toList());
    }
    // prints logging table to the file
    public void logTable() {
        try {
            FileWriter myWriter = new FileWriter("logging_table.txt", false);
            myWriter.write("Client Id\tCash Id\tStart Time\tEnd Time\tTicket Count\r\n");
            var tableSnapshot = new ArrayList<>(loggingTable);
            for(var item: tableSnapshot) {
                myWriter.write(item.getClientId() + "\t" + item.getOfficeId() + "\t" + item.getStartTime() +
                        "\t" + item.getEndTime() + "\t" + item.getTicketCount() + "\r\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}