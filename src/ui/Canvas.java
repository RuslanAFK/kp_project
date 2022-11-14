package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import models.*;


public class Canvas extends JPanel{

    BufferedImage surface;
    final int width = 800;
    final int height = 640;//window nav is 40px
    Graphics g;

    Image cashImg; //50 x 50
    Image clientImg;//50 x 25

    Station station;
    public Canvas(Station station) {
        this.station = station;
        System.out.println("Const");
        try{
            cashImg = ImageIO.read(new File("src/sprites/cashier.png"));
            clientImg = ImageIO.read(new File("src/sprites/client.png"));
        }catch (IOException e) {
            System.out.println("Cash and client img set failed");
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Start");
        JFrame frame = new JFrame("Cash Emulator");
        try{
            Image icon = Toolkit.getDefaultToolkit().getImage("src/sprites/icon.png");
            frame.setIconImage(icon);
        }catch (Exception e) {
            System.out.println("Icon set failed");

        }


        frame.add(this);
        frame.setSize(width,height);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                repaint(0,0,width,height);
            }
        });
        timer.setRepeats(true);
        timer.start();

    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for (var office:  station.getCashOffices()) {
            g.drawImage(cashImg, office.getPosition().x, office.getPosition().y, null);
        }

        for (int i = 0; i < station.getEntranceCount(); i++){
            Position tempPos = station.getEntrancePosition(i);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(tempPos.x, tempPos.y, 50, 10);
            //g.drawRect(tempPos.x, tempPos.y, 50, 20);
        }

        for (var client:  station.getClients()) {
            g.drawImage(clientImg, client.getPosition().x, client.getPosition().y, null);
        }

    }


}
