package ui;

import models.Station;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartForm {
    private JPanel startPanel;
    private JButton startButton;
    private JButton addStationButton;
    private JSpinner entranceSpinner;
    private JSpinner ticketSpinner;
    private Station station;

    public StartForm() {
        this.station = new Station();


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Canvas2 canvas = new Canvas2(station);
                    canvas.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        addStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CashInput a = new CashInput(station);
                a.start();
            }
        });
        entranceSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                station.setEntranceCount((Integer) entranceSpinner.getValue());
                System.out.println("Entrance count was changed to " + station.getEntranceCount());
            }
        });
        ticketSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                station.setTimePerTicket((Integer) ticketSpinner.getValue());
                System.out.println("Time per ticket was changed to " + station.getTimePerTicket());
            }
        });
    }
    public void start() {
        JFrame frame = new JFrame("Configure System");
        Image icon = Toolkit.getDefaultToolkit().getImage("kp_project-Models/src/sprites/icon.png");
        frame.setIconImage(icon);

        frame.setContentPane(this.startPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        entranceSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        entranceSpinner.setMinimumSize(new Dimension(100, 25));
        ticketSpinner = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 1));
        ticketSpinner.setMinimumSize(new Dimension(100, 25));
    }
}
