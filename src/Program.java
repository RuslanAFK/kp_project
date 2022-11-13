import models.Station;
import ui.Canvas2;
import ui.StartForm;

import java.io.IOException;


public class Program {

    private static Program _instance;
    private StartForm startForm;
    private int clientGenerateTime;
    private Canvas2 canvas;
    private Station station;


    private Program(){
        clientGenerateTime = 1000;
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

        startForm = new StartForm(station);
        startForm.start();
    }
    public void start(int strategy){
        System.out.println("Strategy: " + strategy);
    }
}