import java.lang.Math;

public class Sun{
    private int sunX, sunY=-85, limitSunY, interval; //falling sun x and y position
    private boolean fall=false;

    public Sun(int interval){
        this.interval=interval;
        setX();
        setLimit();
    }

    //getter
    public int inter(){return interval;}
    public int getX(){return sunX;}
    public int getY(){return sunY;}
    public int limit(){return limitSunY;}
    public boolean getFall(){return fall;}

    public void setX(){
        sunX=(int)(Math.random() * (900-270+1)+270); //generate falling sunX from x=270 to x=900
    }
    public void setLimit(){
        limitSunY=(int)(Math.random() * (470-200+1)+200); //generate limit falling sunY from y=200 to y=470
    }
    public void drop(boolean fall){this.fall=fall;} //drop sun?
    public void lower(){sunY+=1;} //lower sun position
    public void remove(){sunY=limitSunY+150;} //remove the sun
    public void reset(){sunY=-85;} //reset sun position
}