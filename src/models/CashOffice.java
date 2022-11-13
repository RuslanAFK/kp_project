package models;

import java.util.Optional;

public class CashOffice {
    private int id;
    private Position position;
    private boolean disabled = false;
    private StationQueue queue;
    public CashOffice(Position position, boolean disabled) {
        this.disabled = disabled;
        this.position = new Position(position.x, position.y);
    }
    public CashOffice(Position position) {
        this.position = new Position(position.x, position.y);
    }
}
