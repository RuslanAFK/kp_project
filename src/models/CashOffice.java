package models;

import java.util.LinkedList;

public class CashOffice {
    private Position position;
    private LinkedList<Client> queue;
    private boolean disabled = false;
    public CashOffice(Position position, boolean disabled) {
        this.disabled = disabled;
        this.position = new Position(position.x, position.y);
        queue = new LinkedList<>();
    }
    public CashOffice(Position position) {
        this.position = new Position(position.x, position.y);
        queue = new LinkedList<>();
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "X: " + position.x + ", Y: " + position.y;
    }
    public void addClient(Client client) {
        queue.add(client);
    }
    public void deleteClient() {
        queue.removeFirst();
    }
}
