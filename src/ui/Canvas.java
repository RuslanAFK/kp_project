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


public class Canvas {

    BufferedImage surface;
    final int width = 800;
    final int height = 600;
    Graphics g;
    JLabel view;
    Image cash; //50 x 50
    Image client;//50 x 25

    public Canvas() throws IOException {
        System.out.println("Const");
        cash = ImageIO.read(new File("src/sprites/cashier.png"));
        client = ImageIO.read(new File("src/sprites/client.png"));
        surface = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        view = new JLabel(new ImageIcon(surface));
        Graphics g = surface.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    public void start() throws InterruptedException {
        System.out.println("Start");
        JFrame frame = new JFrame("Cash Emulator");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/sprites/icon.png");
        frame.setIconImage(icon);


        frame.add(this.view);
        frame.setSize(width,height);
        frame.setResizable(false);
        frame.setVisible(true);


        drawCashier(new Position(20, 40));
        drawClient(new Position(100, 150));

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(width);
                drawCashier(new Position(200, 200));
            }
        });
        timer.setRepeats(false);
        timer.start();

        //Thread.sleep(2000);
        //deleteClient();
        //timer.stop();

    }

    public void drawCashier(Position pos){
        g = surface.getGraphics();
        g.drawImage(cash, pos.x, pos.y, null);
        g.dispose();
    }
    public void drawClient(Position pos){
        g = surface.getGraphics();
        g.drawImage(client, pos.x, pos.y, null);

        g.dispose();
    }
    public void deleteClient(){
        g = surface.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

}
