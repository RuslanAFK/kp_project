package models;

import java.util.Optional;

public class CashOffice {
    private Position position;
    private boolean disabled = false;
    public CashOffice(Position position, boolean disabled) {
        this.disabled = disabled;
        this.position = new Position(position.x, position.y);
    }
    public CashOffice(Position position) {
        this.position = new Position(position.x, position.y);
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "X: " + position.x + ", Y: " + position.y;
    }
}
