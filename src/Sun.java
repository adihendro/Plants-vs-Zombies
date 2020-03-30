import java.awt.geom.Ellipse2D;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.lang.Math;
import javax.swing.Timer;

public class Sun{
    private int sunX, sunY, limitSunY; //falling sun x and y position
    private Timer timer; //set timer
    private Ellipse2D e_sun; //ellipse for falling sun

    public Sun(){
        setX();
        sunY=-85;
        setLimit();
    }

    public void start(int interval){
        timer=new Timer(interval*1000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.suns.add(new Sun());
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    //getter
    public int getX(){return sunX;}
    public int getY(){return sunY;}
    public int getLimit(){return limitSunY;}
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
        sunY+=1;
    }
}