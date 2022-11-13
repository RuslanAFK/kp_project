package models;

import java.util.PriorityQueue;
import java.util.Queue;

public class StationQueue {
    Queue<Client> queue;
    public StationQueue(){
        queue = new PriorityQueue<>();
    }
}
