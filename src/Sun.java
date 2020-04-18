import java.awt.geom.Ellipse2D;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.lang.Math;
import javax.swing.Timer;
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class Sun{
    private int sunX, sunY, limitSunY; //falling sun x and y position (coordinate)
    private boolean sunflower, waiting=false; //waiting thread
    private Ellipse2D e_sun; //ellipse for falling sun
    private static Timer timer; //set timer
    private Clip clip;
    private Thread tsun; //thread for waiting time

    public Sun(){
        setX();
        sunY=-85;
        setLimit();
        sunflower=false;
    }

    public Sun(int x, int y){
        sunX=Plant.getCoor(x,y).getX()-15;
        sunY=Plant.getCoor(x,y).getY()-30;
        sunflower=true;
    }

    //initialization block
    {
        tsun = new Thread(new SunWaits()); 
    }

    //drop sun every 5 seconds
    public static void start(){
        timer=new Timer(5000, new ActionListener(){
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

    //private class Threading
    private class SunWaits implements Runnable { 
        public void run() { 
            try{
                Thread.sleep(3000); //Sun waits for 3 seconds 
            } catch (InterruptedException e) {}
        }
    } 
    public void startTimer(){
        tsun.start();
    }

    //getter
    public int getX(){return sunX;}
    public int getY(){return sunY;}
    public int getLimit(){return limitSunY;}
    public boolean isSunflower(){return sunflower;}
    public Ellipse2D getE(){return e_sun;}
    public boolean isTsunAlive(){return tsun.isAlive();}
    public boolean isWaiting(){return waiting;}

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
    public void setWaiting(){
        waiting=true;
    }

    public void lower(){ //lower sun position
        sunY+=2;
    }

    public void points(){ //play points sound
        try{
            // create clip reference 
            clip = AudioSystem.getClip();
            // open audioInputStream to the clip 
            clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/wav/Points.wav")))); 
        }catch(Exception ex){ 
            ex.printStackTrace();
        }
        clip.start();
    }
}