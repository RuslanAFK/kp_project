package models;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


//станція яка має список всіх кас, всіх клієнтів, позиції входів і ше якісь параметри незначні
//внизу розпишу по методах конкретно
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
        timePerTicket = 1000;
    }


    //починає процес продавання квитків який контролюється таймером
    //викликається з класу Program при початку емуляції програми
    //викликає initialiseTimer();
    public void startSellingTickets(){
        System.out.println("Starting selling tickets, time: " + timePerTicket);
        initialiseTimer();

    }

    //починає таймер який раз в певний проміжок часу продає квиток(на кожній касі)
    //видаляє клієнта зі станці якому вже продано квиток
    private void initialiseTimer(){
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                for (CashOffice office : offices) {
                    if(!office.isDisabled()){
                        Client client = office.sellTicket();
                        if(client != null) {
                            clients.remove(client);
                            System.out.println("\nClient removed: " + client.getPosition().toString());
                        }
                    }
                }
            }
        }, 0, timePerTicket);

    }

    //задаю кількість входів а метод сам рахує їхнє місцеположення тіпа як justify-content: space-around;
    public void setEntranceCount(int count){
        if(entrances.size() != count) {
            entrances.clear();
            int space = 800/(count+1);
            for (int i = 1; i <= count; i++){
                entrances.add(new Position(space*i-25, 591));
            }
        }
    }

    //Далі нема шо читати

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
