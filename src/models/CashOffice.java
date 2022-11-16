package models;


import java.util.Deque;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

//каса має інформацію про позицію, чи вона робоча і список клієнтів в черзі
//всякі гетери сетери то ясно, найголовніше то public Client sellTicket() який продає квиток першому клієнту в черзі
//викликається селТікет з станції і повертає їй клієнта шоб та станція видалила його зі свого списку(калхоз)
public class CashOffice {
    private Position position;
    private boolean isDisabled;
    private boolean isFree = true;
    private Deque<Client>  queue;
    private boolean isReserve;

    public boolean getIsReserve(){
        return isReserve;
    }
    public CashOffice(Position position, boolean isDisabled) {
        queue = new LinkedList<>();
        this.isDisabled = isDisabled;
        this.position = new Position(position.x, position.y);
    }
    public CashOffice(Position position) {
        queue = new LinkedList<>();
        this.position = new Position(position.x, position.y);
        isDisabled = false;
    }

    public CashOffice(boolean reserve) {
        queue = new LinkedList<>();
        this.position = new Position(0, 0);
        isDisabled = false;
        isReserve = reserve;
    }

    public boolean isFree() {
        return isFree;
    }
    public Client sellTicket(){
        if(queue.size() >= 1) {
            Client tempClient = queue.remove();
            calculateClientPos();
            return tempClient;
        }
        return null;
    }

    //super kalhoz treba pererobyty des` v program
    public void calculateClientPos(){
        int sizeBefore = 0;
        for (Client client : queue) {
            Position newPos = new Position(position.x, position.y);
            if (position.x <= 400){
                newPos.x += 50 +  sizeBefore*25;
            }else{
                newPos.x -= 25 + sizeBefore*25;
            }
            client.setPosition(newPos);
            sizeBefore++;
        }

    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public int getQueueSize(){
        return queue.size();
    }
    public Client getFirstClient() {
        return queue.isEmpty() ? null : queue.getFirst();
    }

    public void clearQueue(){ queue = new LinkedList<>();}
    public Deque<Client> getQueue(){ return queue;};
    public void addClient(Client client){
        if(client.isDisabled()){
            queue.addFirst(client);
        }else{
            queue.addLast(client);
        }
        calculateClientPos();
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "X: " + position.x + ", Y: " + position.y;
    }
    public void updateForSelling(Station station) {
        if(!queue.isEmpty()) {
            isFree = false;
            var ticketCount = getFirstClient().getTicketCount();
            Timer timer = new Timer();
            var office = this;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    station.deleteFirstClient(office);
                    isFree = true;
                }
            }, (long) station.getTimePerTicket() * ticketCount);
        }
    }

}