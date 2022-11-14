package ui;

import models.CashOffice;
import models.Position;
import models.Station;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

//маленьке вікно для того шоб зібрати інформацію про координати каси і відправити назад в стартФорм
//викликається з стартФорм
//при закритті просто відміняється добавлення каси
public class CashInput {
    JFrame frame;
    private JSpinner spinnerX;
    private JSpinner spinnerY;
    private JPanel inputPanel;
    private JButton addButton;
    private Station station;

    public CashInput(Station st) {
        station = st;

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Position pos = new Position((Integer)spinnerX.getValue(),(Integer)spinnerY.getValue());
                station.addCashOffice(new CashOffice(pos));
                JOptionPane.showMessageDialog(frame, "New station at (" + pos.x + ", " + pos.y + ") was added.");
                List<CashOffice> a = station.getCashOffices();
                System.out.println(a.get(a.size()-1).toString());
                frame.dispose();
            }
        });
    }

    public void start() {
        frame = new JFrame("Add new Cash Office");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/sprites/icon.png");
        frame.setIconImage(icon);
        frame.setContentPane(this.inputPanel);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        spinnerX = new JSpinner(new SpinnerNumberModel(0,0,800,1));
        spinnerY = new JSpinner(new SpinnerNumberModel(0,0,600,1));
    }
}
