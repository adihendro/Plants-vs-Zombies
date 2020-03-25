import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;

public class World extends JPanel implements ActionListener{
    private float posZombieX = 950;
    private float posZombieY = 120;
    private Timer timer;
    private int delay=8;
    Image background, zombie;

    public void paint(Graphics g) {
        Toolkit t=Toolkit.getDefaultToolkit();
        background=t.getImage("Assets/Frontyard.png");
        zombie=t.getImage("Assets/Zombie.gif");
        g.drawImage(background, 0,0,this);
        g.drawImage(zombie, Math.round(posZombieX), Math.round(posZombieY), this);

        g.dispose();
    }

    public World(){
        // setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        posZombieX-=0.1;
        repaint();
    }
}
