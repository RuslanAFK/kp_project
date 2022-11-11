package models;

import java.util.Optional;

public class Client {
    private Status status = Status.NONE;
    private int ticketCount;
    private int uniqueId;
    private Position position;
    public Client(int uniqueId, Position position, int ticketCount) {
        this.ticketCount = ticketCount;
        this.uniqueId = uniqueId;
        this.position = new Position(position.x, position.y);
    }
    public Client(int uniqueId, int ticketCount, Status status) {
        this.status = status;
        this.ticketCount = ticketCount;
        this.uniqueId = uniqueId;
    }
}
