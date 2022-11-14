package Program;

import models.*;
import ui.Canvas;
import ui.StartForm;


import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;

//DEFINE Калхоз = Хєрово написано, тримається на доброму слові і тд

//клас програм це сінглтон
//тримає в собі інформацію про модельку станції і про вюшку канвас
//також має поле стратегія який визначає час за який генерується новий клієнт в мс, якшо -1 то рандом
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

    //статичний клас мейн який створюю обєкт класу в якому він находиться, це може бути калхоз, але я хочу так зробити, може то і правильно
    public static void main(String[] args) throws IOException {
        Program a = Program.getInstance();
        a.configure();
    }

    // запускає меню для конфігурування вхідних даних, з якими працює програма і записує їх в станцію яка створюється в конструкторі Program
    public void configure(){
        StartForm startForm = new StartForm(station);
        startForm.start();
    }

    //починає роботу емулятора, запускає канвас
    //в канвас передаю станцію, він виводить всю інфу раз в якись термін часу
    //цей клас старт викликається з класу StartForm і передає класу старт інформацію про стратегію
    public void start(int strategy){
        Canvas canvas = new Canvas(station);
        canvas.start();
        this.strategy = strategy;

        //запускає функцію для генерування клієнтів
        createClients();

        //запускає роботу станції, каси починають продавати квитки клієнтам які до них підходять
        station.startSellingTickets();

        System.out.println("Start strategy");
        System.out.println("Strategy: " + strategy);
    }


    //функція для початку генерування клієнтів
    public void createClients(){
        int i = 0;
        final Random random = new Random();


        Timer timer = new Timer();
        System.out.println("Creating timer started: " + strategy);

        //викликаю таймер і даю йому задачу як окремий клас з перевизначеним методом run, якшо стратегія -1 тоді рандомні значення
        //більше про таймер написано над класом ClientCreateTimerTask
        if(strategy < 0) {
            timer.schedule(new ClientCreateTimerTask(timer, random), random.nextInt(500, 2000));
        }else{
            timer.schedule(new ClientCreateTimerTask(timer, random), strategy);
        }



        //СТАРІ ТАЙМЕРИ ЗАКОМЕНТОВАНІ НА ВСЯКИЙ ПОЖАРНИЙ
        /*timer.schedule(new TimerTask() {
            int i = 0;
            @Override
            public void run() {
                Status status = Status.NONE;
                if(random.nextInt(4) == 0){
                    status = Status.DISABLED;
                }
                Client newClient = new Client(i, random.nextInt(3) + 1, new Position(0,0), status);
                System.out.println("\nClient Created");
                addClientToStation(newClient);
                if(strategy < 0) {
                    timer.schedule(this, random.nextInt(1000, 2000));
                }else{
                    timer.schedule(this, strategy);
                }
            }
        }, 0);*/
        /*Timer timer = new Timer(strategy, new ActionListener() {
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
        timer.start();*/
        //timer.stop();
    }




    //добавляє клієнта на станцію
    //викликається після створення клієнта
    //отримує інформацію про клієнта і затримку за яку він має дойти до каси = час створення наступного клієнта(калхоз)
    public void addClientToStation(Client client, int delay){

        final Random random = new Random();

        //вибирає вхід на якому спавниться юзер, і зразу записує його позицію(тоже калхоз)
        int entrance = random.nextInt(station.getEntranceCount());
        Position startPos = new Position(station.getEntrancePosition(entrance).x, station.getEntrancePosition(entrance).y);
        startPos.y -= 30;
        startPos.x += 12;
        client.setPosition(startPos);

        //добавляє клієнта на станцію(але ше не в чергу)
        station.addClient(client);
        System.out.println("ClientEntrance: " + entrance);


        //запускає таймер який через певну кількість часу добавляє клієнта в чергу до каси
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Add client to queue");
                addClientToQueue(client);
            }
        }, delay);


        /*
        Timer timer = new Timer(strategy, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add client to queue");
                addClientToQueue(client);
            }
        });
        timer.setRepeats(false);
        timer.start();*/

    }



    //добавляє клієнта в чергу до каси
    //коменти на англ писав раніше того впадлу перекладати, то складний алгоритм його і так ніхто не буде читати
    //по простому вибирає накращу касу для клієнта і поміщає його в чергу + змінює позицію
    public void addClientToQueue(Client client){

        //generate list of cashOffices with minimum clients
        List<CashOffice> cashOffices;

        if(client.isDisabled()) {
            //if client is disabled he chooses closest cashOffice
            cashOffices = station.getCashOffices().stream().filter(c -> !c.isDisabled()).toList();
        }else{
            //else he looks first on people in queue before him
            int minQueue = station.getCashOffices().stream().filter(c -> !c.isDisabled()).mapToInt(c -> c.getQueueSize()).min().orElseThrow();
            cashOffices = station.getCashOffices().stream().filter(c -> c.getQueueSize() == minQueue).toList();
        }


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

    //шукає довжину вектора, для пошуку дистанції до каси
    private double findVectorDistance(Position start, Position end){
        return Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
    }

    //Є два таймера таймер в java.util i javax.swing. Перший створює свій потік  і через то в ньому неможна міняти вюшку
    //тому в ui я юзаю таймер зі javax.swing який не створює інший потік а для алгоритмів юзаю java.util
    //таймер в окремому класі тому, що потрібно кожен раз змінювати затримку пере викликом на рандомну, і неможливо
    //реверсивно викликати декілька разів одину і ту саму задачу таймера
    private class ClientCreateTimerTask extends TimerTask{

        private final Timer timer;
        private final Random random;
        static private int i = 0;
        static private int delay = 500;

        ClientCreateTimerTask(Timer timer, Random random) {
            this.timer = timer;
            this.random = random;
        }

        @Override
        public void run() {
            Status status = Status.NONE;
            Random random = new Random();
            if(random.nextInt(4) == 0){
                status = Status.DISABLED;
            }
            Client newClient = new Client(i++, random.nextInt(3) + 1, new Position(0,0), status);
            System.out.println("\nClient Created");
            addClientToStation(newClient, delay);

            delay = random.nextInt(500, 2000);
            if(strategy < 0) {
                timer.schedule(new ClientCreateTimerTask(timer, random), delay);
            }else{
                timer.schedule(new ClientCreateTimerTask(timer, random), strategy);
            }
        }
    }
}
