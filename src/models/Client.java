package models;

import java.util.Optional;

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

    public Position getPosition() {
        return position;
    }
}
