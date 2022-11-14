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

//просто канвас яким приймає станцію і малює всю інформаціює з неї
//
public class Canvas extends JPanel{

    BufferedImage surface;
    final int width = 800;
    final int height = 640;//window nav is 40px
    Graphics g;

    Image cashImg; //50 x 50
    Image clientImg;
    Image disabledImg;//50 x 25

    Station station;
    public Canvas(Station station) {
        this.station = station;
        System.out.println("Const");
        try{
            cashImg = ImageIO.read(new File("src/sprites/cashier.png"));
            clientImg = ImageIO.read(new File("src/sprites/client.png"));
            disabledImg = ImageIO.read(new File("src/sprites/clientDisabled.png"));
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

        //таймер джавах.свінг, читати про то в класі програм над вкладеним класом ClientCreateTimerTask
        //обновлює вюшку кожні 50мс, в кого лагає комп може поставити побільше
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                repaint(0,0,width,height);
            }
        });
        timer.setRepeats(true);
        timer.start();

    }

    //перемальовує всю модель з нуля на початку запуску і коли викликаний метод репеінт
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //малює станції
        for (var office:  station.getCashOffices()) {
            g.drawImage(cashImg, office.getPosition().x, office.getPosition().y, null);
        }

        //малює входи
        for (int i = 0; i < station.getEntranceCount(); i++){
            Position tempPos = station.getEntrancePosition(i);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(tempPos.x, tempPos.y, 50, 10);
            //g.drawRect(tempPos.x, tempPos.y, 50, 20);
        }

        //малює людей
        for (var client:  station.getClients()) {
            if(client.isDisabled()){
                g.drawImage(disabledImg, client.getPosition().x, client.getPosition().y, null);
            }else{
                g.drawImage(clientImg, client.getPosition().x, client.getPosition().y, null);
            }

        }

    }


}
