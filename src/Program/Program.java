package Program;

import models.*;
import ui.Canvas;
import ui.StartForm;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;


public class Program {

    private static Program _instance;
    private int strategy;
    private Canvas canvas;
    private Station station;


    private Program(){
        strategy = 1000;
        station = new Station();
    }

    public static Program getInstance() {
        if (_instance == null) {
            _instance = new Program();
        }
        return _instance;
    }

    public static void main(String[] args) throws IOException {
        Program a = Program.getInstance();
        a.configure();
    }
    public void configure(){

        StartForm startForm = new StartForm(station);
        startForm.start();
    }
    public void start(int strategy){
        Canvas canvas = new Canvas(station);
        canvas.start();
        this.strategy = strategy;
        createClients();

        station.startSellingTickets();
        System.out.println("Start strategy");

        System.out.println("Strategy: " + strategy);
    }


    public void createClients(){
        int i = 0;
        final Random random = new Random();
        Timer timer = new Timer(strategy, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Status status = Status.NONE;
                if(random.nextInt(4) == 0){
                    status = Status.DISABLED;
                }
                Client newClient = new Client(i, random.nextInt(3) + 1, new Position(0,0), status);
                System.out.println("\nClient Created");
                addClientToStation(newClient);
            }
        });
        timer.setRepeats(true);
        System.out.println("Creating timer started: " + strategy);
        timer.start();
        //timer.stop();
    }

    public void addClientToStation(Client client){
        //choose doors to spawn client
        final Random random = new Random();
        int entrance = random.nextInt(station.getEntranceCount());
        Position startPos = new Position(station.getEntrancePosition(entrance).x, station.getEntrancePosition(entrance).y);
        startPos.y -= 30;
        startPos.x += 12;
        client.setPosition(startPos);
        station.addClient(client);
        System.out.println("ClientEntrance: " + entrance);

        Timer timer = new Timer(strategy, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add client to queue");
                addClientToQueue(client);
            }
        });
        timer.setRepeats(false);
        timer.start();

    }



    public void addClientToQueue(Client client){
        //generate list of cashOffices position with minimum clients

        int minQueue = station.getCashOffices().stream().filter(c -> !c.isDisabled()).mapToInt(c -> c.getQueueSize()).min().orElseThrow();
        List<CashOffice> cashOffices = station.getCashOffices().stream().filter(c -> c.getQueueSize() == minQueue).toList();


        System.out.println("Start queue algorithm");
        if(cashOffices.size() == 1){
            //set clients pos and put him to cashOffice queue
            client.setPosition(cashOffices.get(0).getPosition());
            int index = station.getCashOfficeIndex(cashOffices.get(0));
            station.getCashOffices().get(index).addClient(client);
        }else{
            //find list of distances
            List<Double> distanceToCash = new ArrayList<Double>();
            for (CashOffice pos : cashOffices) {
                distanceToCash.add(findVectorDistance(client.getPosition(), pos.getPosition()));
            }

            //find index of min distance
            int minIndex = IntStream.range(0, distanceToCash.size()).boxed()
                    .min(comparingDouble(distanceToCash::get))
                    .get();

            //set clients pos and put him to cashOffice queue
            client.setPosition(cashOffices.get(minIndex).getPosition());
            int index = station.getCashOfficeIndex(cashOffices.get(minIndex));
            station.getCashOffices().get(index).addClient(client);

        }

    }


    private double findVectorDistance(Position start, Position end){
        return Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
    }


}
