package Program;

import models.*;
import ui.Canvas;
import ui.StartForm;

import java.util.*;
import java.util.stream.IntStream;

import static helpers.Helper.generateRandomStatus;
import static helpers.Helper.randomIntInRange;
import static java.util.Comparator.comparingDouble;

//клас програм це сінглтон
//тримає в собі інформацію про модельку станції і про канвас
//також має поле стратегія який визначає час, за який генерується новий клієнт в мс, якшо -1 то рандом
public class Program {
    private static Program _instance;
    private int strategy = 1000;
    private final Station station;

    private Program() {
        station = new Station();
    }

    public static Program getInstance() {
        if (_instance == null) {
            _instance = new Program();
        }
        return _instance;
    }

    //статичний клас мейн який створюю обєкт класу в якому він находиться, це може бути калхоз, але я хочу так зробити, може то і правильно
    public static void main(String[] args) {
        Program a = Program.getInstance();
        a.configure();
    }

    // запускає меню для конфігурування вхідних даних, з якими працює програма і записує їх в станцію яка створюється в конструкторі Program
    public void configure() {
        StartForm startForm = new StartForm(station);
        startForm.start();
    }

    //починає роботу емулятора, запускає канвас
    //в канвас передаю станцію, він виводить всю інфу раз в якись термін часу
    //цей клас старт викликається з класу StartForm і передає класу старт інформацію про стратегію
    public void start(int strategy) {
        Canvas canvas = new Canvas(station);
        canvas.start();
        this.strategy = strategy;

        //запускає функцію для генерування клієнтів
        createClients();
        makeBreakCashOffice();

        //запускає роботу станції, каси починають продавати квитки клієнтам які до них підходять
        station.notifyForSelling();
    }


    //функція для початку генерування клієнтів
    public void createClients() {
        final Random random = new Random();


        Timer timer = new Timer();

        //викликаю таймер і даю йому задачу як окремий клас з перевизначеним методом run, якшо стратегія -1 тоді рандомні значення
        //більше про таймер написано над класом ClientCreateTimerTask
        if (strategy < 0) {
            timer.schedule(new ClientCreateTimerTask(timer, random), random.nextInt(500, 2000));
        } else {
            timer.schedule(new ClientCreateTimerTask(timer, random), strategy);
        }
    }


    //Функція яка робить касам  перерву
    public void makeBreakCashOffice() {
        Random random = new Random();
        Timer timer = new Timer();
        timer.schedule(new makeDisableCashOffice(timer), random.nextInt(15000, 20000));
    }

    //добавляє клієнта на станцію
    //викликається після створення клієнта
    //отримує інформацію про клієнта і затримку за яку він має дойти до каси = час створення наступного клієнта(калхоз)
    public void addClientToStation(Client client, int delay) {

        // checks if there are enough people in station
        if (station.getClients().size() == station.getMaxClients()) {
            station.setBlocked(true);
            return;
        } else if (station.isBlocked() && station.getClients().size() > 0.7 * station.getMaxClients()) {
            return;
        }
        station.setBlocked(false);

        final Random random = new Random();
        //вибирає вхід на якому спавниться юзер, і зразу записує його позицію
        int entrance = random.nextInt(station.getEntranceCount());
        Position startPos =
                new Position(station.getEntrancePosition(entrance).x, station.getEntrancePosition(entrance).y);
        startPos.y -= 30;
        startPos.x += 12;
        client.setPosition(startPos);

        //добавляє клієнта на станцію(але ше не в чергу)
        station.addClient(client);


        //запускає таймер який через певну кількість часу добавляє клієнта в чергу до каси
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                station.addClientToQueue(client);
            }
        }, delay);

    }

    //Є два таймера таймер в java.util i javax.swing. Перший створює свій потік  і через то в ньому неможна міняти вюшку
    //тому в ui я юзаю таймер зі javax.swing який не створює інший потік а для алгоритмів юзаю java.util
    //таймер в окремому класі тому, що потрібно кожен раз змінювати затримку пере викликом на рандомну, і неможливо
    //реверсивно викликати декілька разів одину і ту саму задачу таймера
    private class ClientCreateTimerTask extends TimerTask {
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
            Status status = generateRandomStatus();
            Random random = new Random();
            Client newClient = new Client(i++, new Position(0, 0), status, randomIntInRange(1, 5));
            addClientToStation(newClient, delay);

            delay = random.nextInt(500, 2000);
            if (strategy < 0) {
                timer.schedule(new ClientCreateTimerTask(timer, random), delay);
            } else {
                timer.schedule(new ClientCreateTimerTask(timer, random), strategy);
            }
        }
    }

    private class makeDisableCashOffice extends TimerTask {

        private final Timer timer;

        makeDisableCashOffice(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {

            Random random = new Random();
            int index = random.nextInt(1, station.getCashOffices().size());
            CashOffice cashOfficeToDisable = station.getCashOffices().get(index);

            if (!cashOfficeToDisable.isDisabled()) {
                System.out.println("Disable office number " + index);
                station.getCashOffices().get(0).makeEnabled();
                cashOfficeToDisable.makeDisabled();
                cashOfficeToDisable.getQueue().forEach(Program.this.station::addClientToQueueReserve);
                cashOfficeToDisable.clearQueue();
            } else {
                System.out.println("Enable office number " + index);
                cashOfficeToDisable.makeEnabled();
                if (station.getTechnicCashOffice().size() == 0) {
                    station.getReservedStation().makeDisabled();
                    station.getReservedStation().getQueue().forEach(cashOfficeToDisable::addClient);
                    station.getReservedStation().clearQueue();
                }

            }
            timer.schedule(new makeDisableCashOffice(timer), 15000);
        }
    }
}