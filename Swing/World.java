import java.awt.*;
import java.awt.event.*;  
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.io.*;

import javax.swing.*;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX = 950;
    private float posZombieY = 133;
    private int pwidth = 62;
    private int pheight = 66;
    private Timer timer;
    private int delay=25;
    private Image background, zombie, sunflower, peashooter, repeater, menu;

    private Shape shape, r_sunflower, r_peashooter, r_repeater;
    private Point mouse = new Point();
    private boolean po=false;
    private int choice=0;

    // ArrayList<item> arr = new ArrayList<item>();
    
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
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        zombie=t.getImage("Assets/Zombie.gif");
        g.drawImage(background, 0,0, this);
        g.drawImage(zombie, Math.round(posZombieX), Math.round(posZombieY), pwidth+11, pheight+53, this);
        g.drawImage(menu, 20, 60, 110, 500, this);
        g.drawImage(sunflower, 43, 200, pwidth, pheight+5, this);
        g.drawImage(peashooter, 43, 320, pwidth, pheight, this);
        g.drawImage(repeater, 43, 433, pwidth+4, pheight+4, this);
        
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D g3 = (Graphics2D) g;
        if(choice==1){
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            g2.drawImage(sunflower, mouse.x-pwidth/2, mouse.y-(pheight+10)/2, pwidth, pheight+5, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
        }else if(choice==2){
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            g2.drawImage(peashooter, mouse.x-pwidth/2, mouse.y-pheight/2, pwidth, pheight, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
        }else if(choice==3){
            g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            g2.drawImage(repeater, mouse.x-(pwidth+4)/2, mouse.y-(pheight+4)/2, pwidth+4, pheight+4, this);
            g2.setComposite(AlphaComposite.SrcOver.derive(1f));
        }
        
        // g3.setComposite(AlphaComposite.SrcOver.derive(0.7f));
        // g3.setColor(Color.RED);
        // g3.fill(r_sunflower);
        // g3.setColor(Color.YELLOW);
        // g3.fill(r_peashooter);
        // g3.setColor(Color.ORANGE);
        // g3.fill(r_repeater);

        g3.setColor(Color.WHITE);
        g3.drawString("(" + (mouse.x) + ", " + (mouse.y) + ")" , 10, 20);
        g3.drawString(Boolean.toString(po), 10, 30);
        if(po){
            g3.setColor(Color.GRAY);
            g3.fill(shape);
        }else{
            g3.setColor(Color.BLUE);
            g3.fill(shape);
        }
        g.dispose();
    }
    
  
    public World(){
        // setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
        
        getImage();        

        shape = new Rectangle2D.Double(140, 140, 100, 100);
        r_sunflower = new Rectangle2D.Double(36, 190, pwidth+17, pheight+50);
        r_peashooter = new Rectangle2D.Double(36, 190+pheight+50, pwidth+17, pheight+52);
        r_repeater = new Rectangle2D.Double(36, 190+2*pheight+102, pwidth+17, pheight+53);
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
            po = (po) ? false:true;
            // check if mouse clicked plants
            if (r_sunflower.contains(e.getPoint())) {
                choice=1;
            }else if(r_peashooter.contains(e.getPoint())) {
                choice=2;
            }else if(r_repeater.contains(e.getPoint())) {
                choice=3;
            }else{
                choice=0;
            }
        }
    }
    

    // public void mouseClicked(MouseEvent e) { 
    // }  
    // public void mouseEntered(MouseEvent e) {  
    // }  
    // public void mouseExited(MouseEvent e) {  
    // }  
    // public void mouseReleased(MouseEvent e) {  
    // }  

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        posZombieX-=0.35;
        repaint();
    }
}
