import java.awt.*;
import java.awt.event.*;  
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.util.ArrayList;

public class World extends JPanel implements ActionListener{
    private float posZombieX = 950;
    private float posZombieY = 120;
    private int pwidth = 60;
    private int pheight = 65;
    private Timer timer;
    private int delay=8;
    Image background, zombie, sunflower, peashooter, repeater, menu;

    private Shape shape;
    private Point mouse = new Point();
    private JLabel l;
    boolean po = false;

    // ArrayList<item> arr = new ArrayList<item>();

    @Override
    public void paint(Graphics g) {
        Toolkit t=Toolkit.getDefaultToolkit();
        background=t.getImage("Assets/Frontyard.png");
        zombie=t.getImage("Assets/Zombie.gif");
        sunflower=t.getImage("Assets/Sunflower.png");
        peashooter=t.getImage("Assets/Peashooter.png");
        repeater=t.getImage("Assets/Repeater.png");
        menu=t.getImage("Assets/Menu.png");
        g.drawImage(background, 0,0,this);
        g.drawImage(zombie, Math.round(posZombieX), Math.round(posZombieY), this);
        g.drawImage(menu, 20, 60, 110, 500, this);
        g.drawImage(sunflower, 43, 200, pwidth, pheight, this);
        g.drawImage(peashooter, 43, 320, pwidth, pheight, this);
        g.drawImage(repeater, 43, 440, pwidth, pheight, this);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.drawString("contains(" + (mouse.x) + ", " + (mouse.y) + ")" , 10, 10);
        g2.drawString(Boolean.toString(po), 10, 20);
        g2.setColor(Color.GRAY);
        // g2.fill(shape);
        g.dispose();
    }

    public World(){
        // setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();

        shape = new Rectangle2D.Double(140, 140, 200, 200);
        addMouseListener(new MyMouseListener());
         
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (shape.contains(e.getPoint())) {
                po = true;
            }
            else{
                po=false;
            }
        }
    }

    public void mouseClicked(MouseEvent e) { 
        mouse = e.getPoint(); 
    }  
    public void mouseEntered(MouseEvent e) {  
    }  
    public void mouseExited(MouseEvent e) {  
    }  
    // public void mousePressed(MouseEvent e) {  
    //     l.setText("Mouse Pressed");  
    // }  
    public void mouseReleased(MouseEvent e) {  
    }  

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        posZombieX-=0.1;
        repaint();
    }
}
