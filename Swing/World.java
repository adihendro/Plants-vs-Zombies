import java.awt.event.*;  
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX=950f, posZombieY=133f;
    private int sunX, sunY=-85;
    //width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+2, rheight=pheight+2;
    //private int zwidth
    private Timer timer;
    private int delay=25;
    //img: 0.background, 1.menu, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 8.zombie, 9.sun
    private Image[] img = new Image[10];
    private Rectangle r_sunflower, r_peashooter, r_repeater, r_zombie; //rectangle for plants menu and others
    private Ellipse2D e_sun;
    private Shape[][] field = new Shape[5][9]; //rectangle array with 5 rows and 9 columns for field area
    private Point mouse = new Point();
    private Point[][] plant_field = new Point[5][9]; //array for placing plants
    private int choice=0, trans=0, i=0,j=0, xa,ya;
    private long startTime, elapsed, sun_elapsed; //for timer
    private boolean b1=false, b2=false;
    //int seconds;
    // private List<Integer> suns = new ArrayList<Integer>(); //store sunX data


    //public class ListMap<K, V> implements Map<K, V> {


    public World(){
        // setFocusable(true);
        startTime=System.currentTimeMillis();
        // seconds=1;
        timer = new Timer(delay, this);
        timer.start();
        
        getImg();     
        setImg(); 

        sunX=(int)(Math.random() * (900-270+1)+270); //generate sunX from x=270 to x=900

        addMouseListener(new MListener1());

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        elapsed=(System.currentTimeMillis()-startTime)/1000; // count time in seconds
        // seconds++;
        // timer.setInitialDelay((int)(startTime+seconds*1000-now));
        timer.start();
        posZombieX-=0.35;
        
        if(elapsed>1 && elapsed%6==0){ //drop sun every 6 seconds
            b1=true;
        }


        // r_zombie.setLocation(Math.round(posZombieX), Math.round(posZombieY));
        r_zombie = new Rectangle(Math.round(posZombieX)+43, Math.round(posZombieY)+60, 20, 40);

        repaint();
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        img[5]=t.getImage("Assets/Sunflower.gif");
        img[6]=t.getImage("Assets/Peashooter.gif");
        img[7]=t.getImage("Assets/Repeater.gif");
        img[8]=t.getImage("Assets/Zombie.gif");
        g.drawImage(img[0], 0, 0, 1024, 626, this); //background
        // g.drawImage(img[1], 22, 60, 105, 487, this); //menu
        // g.drawImage(img[2], 43, 200, swidth, sheight, this); //sunflower
        // g.drawImage(img[3], 43, 320, pwidth, pheight, this); //peashooter
        // g.drawImage(img[4], 43, 433, rwidth, rheight, this); //repeater

        Graphics2D g2 = (Graphics2D) g;
        Graphics2D g3 = (Graphics2D) g;
        // make a transparent plant selection which follows mouse
        if(trans==1){ //transparent plant
            if(choice==1){ //sunflower
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[2], mouse.getX()-swidth/2, mouse.getY()-sheight/2, swidth, sheight, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }else if(choice==2){ //peashooter
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[3], mouse.getX()-pwidth/2, mouse.getY()-pheight/2, pwidth, pheight, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }else if(choice==3){ //repeater
                g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
                g2.drawImage(img[4], mouse.getX()-(rwidth+8)/2, mouse.getY()-(rheight+8)/2, rwidth+4, rheight+4, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }
        }else if(trans==2){ //select field
            if(choice==1){ //sunflower gif
                g.drawImage(img[5], plant_field[xa][ya].getX()-swidth/2, plant_field[xa][ya].getY()-sheight/2, swidth, sheight, this);
            }else if(choice==2){ //peashooter gif
                g.drawImage(img[6], plant_field[xa][ya].getX()-(pwidth+2)/2, plant_field[xa][ya].getY()-(pheight+2)/2, pwidth+2, pheight+2, this);
            }else if(choice==3){ //repeater gif
                g.drawImage(img[7], plant_field[xa][ya].getX()-(rwidth+20)/2, plant_field[xa][ya].getY()-(rheight+9)/2, rwidth+26, rheight+13, this);
            }
        }

        if(!r_zombie.intersects(field[1][6].getBounds2D())){
            g.drawImage(img[8], Math.round(posZombieX), Math.round(posZombieY), pwidth+11, pheight+53, this); //zombie
        }
        
        if(b1){ //drop sun every 6 seconds
            if(sunY<470){ 
                g.drawImage(img[9],sunX,sunY,80,80,this);
                e_sun = new Ellipse2D.Float(sunX, sunY, 80, 80);
                sunY+=1;
            }else{ //sun reach bottom
                sunY=-85;
                sunX=(int)(Math.random() * (900-270+1)+270); //generate sunX from x=270 to x=900
                b1=false;
            }
        }


        
        // show plant menu rectangle
        // g3.setColor(Color.RED);
        // g3.fill(r_sunflower);
        // g3.setColor(Color.YELLOW);
        // g3.fill(r_peashooter);
        // g3.setColor(Color.ORANGE);
        // g3.fill(r_repeater);
        
        g3.setColor(Color.WHITE);
        // g3.setComposite(AlphaComposite.SrcOver.derive(0.7f));
        // g3.fill(r_zombie);

        // show rectangle field
        // for(int i=0;i<9;i++){
            // for(int j=0;j<5;j++){
                // g3.fill(field[i][j]);
        //     }
        // }
        g3.drawString(mouse.print(), 10, 20);
        g.dispose();
    }
    

    private class MListener1 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e_sun.contains(e.getPoint())){
                sunY=470;
                //add sun points;
            }else{ // check if mouse clicked plants
                if (r_sunflower.contains(e.getPoint())) {
                    choice= (choice==1) ? 0:1;
                    trans=1;
                }else if(r_peashooter.contains(e.getPoint())) {
                    choice= (choice==2) ? 0:2;
                    trans=1;
                }else if(r_repeater.contains(e.getPoint())) {
                    choice= (choice==3) ? 0:3;
                    trans=1;
                }else if(trans==1 && (choice==1 || choice==2 || choice==3)){
                    trans=2;
                    for(i=0;i<5;i++){
                        for(j=0;j<9;j++){
                            if(field[i][j].contains(e.getPoint())){
                                xa=i;
                                ya=j;
                                i=10;j=9; //break
                            }
                        }
                    }
                    if(i==5){trans=0;} //not selected a plant-able area
                }

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


    private void getImg(){
        try{
            //try to load image
            img[0]=ImageIO.read(new File("Assets/Background.jpg"));
            // img[1]=ImageIO.read(new File("Assets/Menu.png"));
            img[2]=ImageIO.read(new File("Assets/Sunflower.png"));
            img[3]=ImageIO.read(new File("Assets/Peashooter.png"));
            img[4]=ImageIO.read(new File("Assets/Repeater.png"));
            img[9]=ImageIO.read(new File("Assets/Sun.png"));
        } catch(IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void setImg(){
        //create rectangle for plant menu
        r_sunflower = new Rectangle(30, 185, pwidth+25, pheight+55);
        r_peashooter = new Rectangle(30, 190+pheight+50, pwidth+25, pheight+52);
        r_repeater = new Rectangle(30, 190+2*pheight+95, pwidth+25, pheight+53);
        e_sun = new Ellipse2D.Float(sunX, sunY, 80, 80);
        
        //create rectangle for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                field[i][j] = new Rectangle(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                //set point for plant field
                plant_field[i][j] = new Point(296+j*81,117+i*98);
            }
        }

        //create oval for peas
        // Shape shape = new Ellipse2D.Double(10, 10, 90, 90);
    }
}
