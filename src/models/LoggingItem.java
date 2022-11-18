package models;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingItem {
    private final int clientId;
    private final int cashOfficeId;
    private final String startTime;
    private String endTime = "not yet";
    private final int ticketCount;
    public LoggingItem(int clientId, int cashOfficeId, int ticketCount) {
        this.cashOfficeId = cashOfficeId;
        this.clientId = clientId;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.startTime = dtf.format(now);
        this.ticketCount = ticketCount;
    }

    public int getClientId() {
        return clientId;
    }

    public int getOfficeId() {
        return cashOfficeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.endTime = dtf.format(now);
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
