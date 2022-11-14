package models;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.LinkedList;

//каса має інформацію про позицію, чи вона робоча і список клієнтів в черзі
//всякі гетери сетери то ясно, найголовніше то public Client sellTicket() який продає квиток першому клієнту в черзі
//викликається селТікет з станції і повертає їй клієнта шоб та станція видалила його зі свого списку(калхоз)
public class CashOffice {
    private Position position;
    private boolean disabled;
    private Deque<Client>  queue = new LinkedList<Client>();

    public CashOffice(Position position, boolean disabled) {
        queue = new LinkedList<Client>();
        this.disabled = disabled;
        this.position = new Position(position.x, position.y);
    }
    public CashOffice(Position position) {
        queue = new LinkedList<Client>();
        this.position = new Position(position.x, position.y);
        disabled = false;
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
        return disabled;
    }

    public int getQueueSize(){
        return queue.size();
    }

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
}
