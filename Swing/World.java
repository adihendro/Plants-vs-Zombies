import java.awt.*;
import java.awt.event.*;  
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX = 950;
    private float posZombieY = 120;
    private int pwidth = 60;
    private int pheight = 65;
    private Timer timer;
    private int delay=50;
    Image background, zombie, sunflower, peashooter, repeater, menu;

    private Shape shape, shape2;
    private Point mouse = new Point();
    boolean po = false;

    // ArrayList<item> arr = new ArrayList<item>();
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        // Image i=t.getImage("p3.gif");  
        zombie=t.getImage("Assets/Zombie.gif");
        g.drawImage(background, 0,0, this);
        g.drawImage(zombie, Math.round(posZombieX), Math.round(posZombieY), this);
        g.drawImage(menu, 20, 60, 110, 500, this);
        g.drawImage(sunflower, 43, 200, pwidth, pheight, this);
        g.drawImage(peashooter, 43, 320, pwidth, pheight, this);
        g.drawImage(repeater, 43, 440, pwidth, pheight, this);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.drawString("(" + (mouse.x) + ", " + (mouse.y) + ")" , 10, 20);
        g2.drawString(Boolean.toString(po), 10, 30);
        if(po){
            g2.setColor(Color.GRAY);
            g2.fill(shape);
        }else{
            g2.setColor(Color.BLUE);
            g2.fill(shape);
        }
        g.dispose();
    }
    
    public void getImage(){
        try{
            background=ImageIO.read(new File("Assets/Frontyard.png"));
            sunflower=ImageIO.read(new File("Assets/Sunflower.png"));
            peashooter=ImageIO.read(new File("Assets/Peashooter.png"));
            repeater=ImageIO.read(new File("Assets/Repeater.png"));
            menu=ImageIO.read(new File("Assets/Menu.png"));
        } catch(IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog( null, ex.toString() );
        }
    }
    
    public World(){
        // setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
        
        getImage();        

        shape = new Rectangle2D.Double(140, 140, 100, 100);
        shape2 = new Rectangle2D.Double(36, 193, pwidth+17, pheight+45);
        addMouseListener(new MouseListener1());

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouse = e.getPoint();
            }
        });
    }
    
    private class MouseListener1 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (shape2.contains(e.getPoint())) {
                mouse = e.getPoint();
                po = (po) ? false:true;
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
        posZombieX-=0.7;
        repaint();
    }
}
