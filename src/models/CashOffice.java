package models;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.LinkedList;

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
            return queue.remove();
        }
        return null;
    }

    //super kalhoz treba pererobyty des` v program
    private void calculateClientPos(){
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

        if(!client.isDisabled()){
            queue.addFirst(client);
        }else{
            queue.add(client);
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
