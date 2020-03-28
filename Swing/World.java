// import java.awt.*;
import java.awt.Image;
import java.awt.Shape;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.*;  
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

public class World extends JPanel implements ActionListener{
    private float posZombieX=950, posZombieY=133;
    //width and height for (p)eashooter, (s)unflower, (r)epeater
    private int pwidth=62, pheight=66, swidth=pwidth, sheight=pheight+5, rwidth=pwidth+4, rheight=pwidth+4;
    private Timer timer;
    private int delay=25;
    //0.background, 1.menu, 2.sunflower, 3.peashooter, 4.repeater, 5.sungif, 6.peagif, 7.repgif, 8.zombie;
    private Image[] img = new Image[9];
    private Shape r_sunflower, r_peashooter, r_repeater;
    private Shape[][] field = new Shape[5][9]; // 2D Shape array with 5 rows and 9 columns
    private Point mouse = new Point();
    private Point[][] plant_field = new Point[5][9];
    private int choice=0, trans=0, i=0,j=0, xa,ya;

    // ArrayList<item> arr = new ArrayList<item>();

    public World(){
        // setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();
        
        getImg();     
        setImg(); 

        addMouseListener(new MListener1());

        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouse.setX(e.getX());
                mouse.setY(e.getY());
            }
        });
    }
    

    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);
        Toolkit t=Toolkit.getDefaultToolkit();  
        img[5]=t.getImage("Assets/Sunflower.gif");
        img[6]=t.getImage("Assets/Peashooter.gif");
        img[7]=t.getImage("Assets/Repeater.gif");
        img[8]=t.getImage("Assets/Zombie.gif");
        g.drawImage(img[0], 0, 0, 1024, 626, this); //background
        g.drawImage(img[1], 20, 60, 110, 500, this); //menu
        g.drawImage(img[2], 43, 200, swidth, sheight, this); //sunflower
        g.drawImage(img[3], 43, 320, pwidth, pheight, this); //peashooter
        g.drawImage(img[4], 43, 433, rwidth, rheight, this); //repeater
        g.drawImage(img[8], Math.round(posZombieX), Math.round(posZombieY), pwidth+11, pheight+53, this); //zombie

        
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
                g2.drawImage(img[4], mouse.getX()-rwidth/2, mouse.getY()-rheight/2, rwidth, rheight, this);
                g2.setComposite(AlphaComposite.SrcOver.derive(1f));
            }
        }else if(trans==2){ //select field
            if(choice==1){ //sunflower gif
                g.drawImage(img[5], plant_field[xa][ya].getX()-swidth/2, plant_field[xa][ya].getY()-sheight/2, swidth, sheight, this);
            }else if(choice==2){ //peashooter gif
                g.drawImage(img[6], plant_field[xa][ya].getX()-(pwidth+2)/2, plant_field[xa][ya].getY()-(pheight+2)/2, pwidth+2, pheight+2, this);
            }else if(choice==3){ //repeater gif
                g.drawImage(img[7], plant_field[xa][ya].getX()-(rwidth+20)/2, plant_field[xa][ya].getY()-(rheight+9)/2, rwidth+20, rheight+9, this);
            }
        }
        
        // show plant menu rectangle
        // g3.setComposite(AlphaComposite.SrcOver.derive(0.7f));
        // g3.setColor(Color.RED);
        // g3.fill(r_sunflower);
        // g3.setColor(Color.YELLOW);
        // g3.fill(r_peashooter);
        // g3.setColor(Color.ORANGE);
        // g3.fill(r_repeater);
        
        g3.setColor(Color.WHITE);
        // g3.fill(field[xa][ya]);
        // for(int i=0;i<9;i++){
        //     for(int j=0;j<5;j++){
        //     }
        // }
        g3.drawString(mouse.print(), 10, 20);
        g3.drawString(Integer.toString(plant_field[xa][ya].getX()), 10, 30);
        g.dispose();
    }
    

    private class MListener1 extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // check if mouse clicked plants
            if (r_sunflower.contains(e.getPoint())) {
                choice= (choice==1) ? 0:1;
                trans=1;
            }else if(r_peashooter.contains(e.getPoint())) {
                choice= (choice==2) ? 0:2;
                trans=1;
            }else if(r_repeater.contains(e.getPoint())) {
                choice= (choice==3) ? 0:3;
                trans=1;
            }else if(choice==1 || choice==2 || choice==3){
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


    private void getImg(){
        try{
            //try to load image
            img[0]=ImageIO.read(new File("Assets/Frontyard.png"));
            img[1]=ImageIO.read(new File("Assets/Menu.png"));
            img[2]=ImageIO.read(new File("Assets/Sunflower.png"));
            img[3]=ImageIO.read(new File("Assets/Peashooter.png"));
            img[4]=ImageIO.read(new File("Assets/Repeater.png"));
        } catch(IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void setImg(){
        //create rectangle for plant menu
        r_sunflower = new Rectangle2D.Double(36, 190, pwidth+17, pheight+50);
        r_peashooter = new Rectangle2D.Double(36, 190+pheight+50, pwidth+17, pheight+52);
        r_repeater = new Rectangle2D.Double(36, 190+2*pheight+102, pwidth+17, pheight+53);
        
        //create rectangle for field
        int[] fw = {0,90,165,250,330,410,492,570,651,749}; //field width
        int[] fh = {0,118,215,323,405,516}; //field height
        for(i=0;i<5;i++){
            for(j=0;j<9;j++){
                field[i][j] = new Rectangle2D.Double(245+fw[j], 50+fh[i], fw[j+1]-fw[j], fh[i+1]-fh[i]);
                //set point for plant field
                plant_field[i][j] = new Point(296+j*81,117+i*98);
            }
        }

        //create oval for peas
        Shape shape = new Ellipse2D.Double(10, 10, 90, 90);
    }
}
