package Program;

import models.Station;
import ui.Canvas2;
import ui.StartForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        Canvas2 canvas = new Canvas2(station);
        canvas.start();
        System.out.println("Strategy: " + strategy);
    }

    //EXAMPLE
    public void someAlgorithm(){
        //EXAMPLE OF TIMER
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ACTION REPEATS EVERY 1000ms
            }
        });
        timer.setRepeats(true); //repeat or do it once after delay
        timer.start();
        timer.stop();
    }
}
