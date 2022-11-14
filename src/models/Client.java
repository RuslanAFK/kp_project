package models;

import java.util.Optional;

//тупо клієнт шо казати
public class Client {
    private Status status = Status.NONE;
    private int ticketCount;
    private int uniqueId;
    private Position position;
    public Client(int uniqueId, int ticketCount, Position position, Status status) {
        this.ticketCount = ticketCount;
        this.uniqueId = uniqueId;
        this.position = new Position(position.x, position.y);
        this.status = status;

    }

    public boolean isDisabled(){
        if(status == Status.NONE){
            return false;
        }
        return true;
    }

    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position){
        this.position.x = position.x;
        this.position.y = position.y;
    }
}
