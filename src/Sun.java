import java.awt.geom.Ellipse2D;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.lang.Math;

import javax.swing.Timer;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class Sun{
    private int sunX, sunY, limitSunY, sunY2; //falling sun x and y position (coordinate)
    private boolean sunflower=false; //for sunflower's sun
    private static Timer timer; //set timer
    private Ellipse2D e_sun; //ellipse for falling sun
    private Clip clip;

    public Sun(){
        setX();
        sunY=-85;
        setLimit();
        e_sun = new Ellipse2D.Float(-10, -10, 1, 1);
    }

    public Sun(int x, int y){
        sunX=World.plant_f[x][y].getX()-15;
        sunY=World.plant_f[x][y].getY()-30;
        sunY2=sunY;
        limitSunY=sunY+250;
        sunflower=true;
    }

    //initialization block
    {
        try{
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Points.wav")))); 
        }catch(Exception ex){ 
            ex.printStackTrace();
        }
    }

    public static void start(int interval){
        timer=new Timer(interval*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.suns.add(new Sun());
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    public static void stop(){
        timer.stop();
    }

    //getter
    public int getX(){return sunX;}
    public int getY(){return sunY;}
    public int getY2(){return sunY2;}
    public int getLimit(){return limitSunY;}
    public boolean isSunflower(){return sunflower;}
    public Ellipse2D getE(){
        return e_sun;
    }

    //setter
    public void setE(Ellipse2D e_sun){
        this.e_sun=e_sun;
    }
    public void setX(){
        sunX=(int)(Math.random() * (900-270+1)+270); //generate falling sunX from x=270 to x=900
    }
    public void setLimit(){ 
        limitSunY=(int)(Math.random() * (470-200+1)+200); //generate limit falling sunY from y=200 to y=470
    }

    public void lower(){ //lower sun position
        sunY+=2;
    }

    public void points(){ //play points sound
        clip.start();
    }
}