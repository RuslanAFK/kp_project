package models;

import java.util.Optional;

//тупо клієнт шо казати
public class Client {
    private Status status;
    private int uniqueId;
    private Position position;
    private int ticketCount;
    public Client(int uniqueId, Position position, Status status, int ticketCount) {
        this.uniqueId = uniqueId;
        this.position = new Position(position.x, position.y);
        this.status = status;
        this.ticketCount = ticketCount;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public boolean isDisabled(){
        return status != Status.NONE;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public Position getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public void setPosition(Position position){
        this.position.x = position.x;
        this.position.y = position.y;
    }
}