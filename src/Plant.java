import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Plant<T> extends Actor{
    private T type;
    private int x, y; //array for plant location [5][9]
    private boolean repeat=false;
    private boolean idle=true, threaten=false;
    private Timer timer, timer2, timer3; //set timer
    private static int[][] occ = new int[5][10];
    
    public Plant(T type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
        if(type.equals(1)){ //Sunflower
            super.health = 40;
        }else if(type.equals(2)){ //Peashooter
            super.health = 40;
        }else if(type.equals(3)){ //Repeater
            super.health = 60;
        }else{}
    }

    //initialization block
    {
        //shoot pea every 2 seconds
        timer=new Timer(2000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.peas.add(new Pea((int)type, x, y));
            }
        });

        //repeater shoots second pea every 2.15 seconds
        timer2=new Timer(150, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(type.equals(3)){ //repeater
                    if(repeat){
                        World.peas.add(new Pea(3, x, y));
                    }else{
                        repeat=true;
                    }
                }
            }
        });
        timer2.setDelay(2000);

        //drop sun every 10 seconds
        timer3=new Timer(10000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.suns.add(new Sun(x, y));
            }
        });
    }

    //getter
    public int X(){return x;}
    public int Y(){return y;}
    public T getType(){return type;}
    public boolean isThreaten(){return threaten;}
    public static int getOcc(int x, int y){return occ[x][y];}
    public boolean isIdle(){
        return idle;
    }
    
    //setter
    public static void setOcc(int i, int j){
        occ[i][j]=0;
    }
    public void setThreat(boolean threat){
        threaten=threat;
    }

    public boolean put(int x, int y, T type){
        if(occ[x][y]==0){ //empty spot
            occ[x][y]=(int)type;
            World.plants.add(new Plant<Integer>((int)type, x, y));
            return true;
        }else{
            return false;
        }
    }
    public void attack(){
        timer.start();
        timer2.start();
        idle=false;
    }
    public void stop(){
        timer.stop();
        timer2.stop();
        idle=true;
    }
    public void act(){
        timer3.start();
    }
}