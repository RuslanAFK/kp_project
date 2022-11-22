package models;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static helpers.Helper.findVectorDistance;
import static java.util.Comparator.comparingDouble;

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
        //Створюється резерва каса
        addCashOffice(new CashOffice(true));
    }


    public CashOffice getReservedStation(){
        for (var office: offices) {
            if (office.isReserved() == true){
                return office;
            }
        }
        return null;
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
                    System.err.println("Can't modify now logging tree.");
                }

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
    //добавляє клієнта в чергу до каси
    //коменти на англ писав раніше того впадлу перекладати, то складний алгоритм його і так ніхто не буде читати
    //по простому вибирає накращу касу для клієнта і поміщає його в чергу + змінює позицію
    public void addClientToQueue(Client client) {

        //generate list of cashOffices with minimum clients
        List<CashOffice> cashOffices = new ArrayList<CashOffice>();

        if (client.isDisabled()) {

            //if client is disabled he chooses closest cashOffice
            cashOffices = getCashOffices().stream().filter(c -> !c.isDisabled()).toList();
            // Хотів не робити умову в умові, а просто провіряти після умови чи треба залучати резерву касу і добавляти в cashOffices, але чогось воно кидало ексепшин
            if (getCashOffices().size() == 1 ) {
                getCashOffices().get(0).makeEnabled();
                cashOffices = getCashOffices().stream().filter(c -> !c.isDisabled() && c.isReserved()).toList();
            }
        } else {

            //else he looks first on people in queue before him && !c.isReserved()
            try {
                int minQueue = getCashOffices().stream().filter(c -> !c.isDisabled() ).mapToInt(CashOffice::getQueueSize).min().orElseThrow();
                cashOffices = getCashOffices().stream().filter(c -> (c.getQueueSize() == minQueue)).toList();
            }
            catch (NoSuchElementException ex){

            }
        }


        if (cashOffices.size() == 1) {
            //set clients pos and put him to cashOffice queue
            client.setPosition(cashOffices.get(0).getPosition());
            int index = getCashOfficeIndex(cashOffices.get(0));
            var office = getCashOffices().get(index);
            office.addClient(client);

            getLoggingTable().add(new LoggingItem(client.getUniqueId(), index, client.getTicketCount()));
            logTable();
        } else {
            //find list of distances
            List<Double> distanceToCash = new ArrayList<Double>();
            for (CashOffice pos : cashOffices) {
                distanceToCash.add(findVectorDistance(client.getPosition(), pos.getPosition()));
            }


            //find index of min distance
            int minIndex = IntStream.range(0, distanceToCash.size()).boxed()
                    .min(comparingDouble(distanceToCash::get)).orElse(0);


            //set clients pos and put him to cashOffice queue
            client.setPosition(cashOffices.get(minIndex).getPosition());
            int index = getCashOfficeIndex(cashOffices.get(minIndex));
            var office = getCashOffices().get(index);
            office.addClient(client);

            getLoggingTable().add(new LoggingItem(client.getUniqueId(), index, client.getTicketCount()));
            logTable();
        }
    }


    // Цей метод получає каси які не активні і переносить людей на резервну касу
    public void addClientToQueueReserve(Client client){
        CashOffice reserve = getCashOffices().get(0);
        var disabledOffice = getTechnicCashOffice();
        if(disabledOffice.size()>=1){
            disabledOffice.forEach(off -> {off.getQueue().forEach(reserve::addClient); off.clearQueue();});
        }
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
        } catch (IOException e) {
            System.err.println("Cannot write to file.");
        }
    }
}