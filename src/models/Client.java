package models;

public class Client {
    private final Status status;
    private final int uniqueId;
    private final Position position;
    private final int ticketCount;
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

    public void setPosition(Position position){
        this.position.x = position.x;
        this.position.y = position.y;
    }
}