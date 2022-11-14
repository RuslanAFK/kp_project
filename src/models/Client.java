package models;

import java.util.Optional;

//тупо клієнт шо казати
public class Client {
    private Status status;
    private int uniqueId;
    private Position position;
    public Client(int uniqueId, Position position, Status status) {
        this.uniqueId = uniqueId;
        this.position = new Position(position.x, position.y);
        this.status = status;
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
    public void setPosition(Position position){
        this.position.x = position.x;
        this.position.y = position.y;
    }
}
