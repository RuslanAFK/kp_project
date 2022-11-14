package models;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingItem {
    private int clientId;
    private int cashOfficeId;
    private String startTime;
    private String endTime = "not yet";
    private int ticketCount = 1;
    public LoggingItem(int clientId, int cashOfficeId) {
        this.cashOfficeId = cashOfficeId;
        this.clientId = clientId;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        this.startTime = dtf.format(now);
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
        System.out.println(dtf.format(now));
        this.endTime = dtf.format(now);
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
