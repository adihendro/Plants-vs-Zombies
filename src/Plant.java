import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Plant<T> extends Actor{
    private T type;
    private int x, y; //array for plant location [5][9]
    private boolean repeat=false;
    private boolean idle=true, threaten=false;
    // private int health;
    private Timer timer, timer2, timer3; //set timer
    private static int[][] occ = new int[5][10];
    
    public Plant(T type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
        if(type.equals(1)){ //Sunflower
            super.type="Sunflower";
            super.health = 40;
        }else if(type.equals(2)){ //Peashooter
            super.type="Peashooter";
            super.health = 40;
        }else if(type.equals(3)){ //Repeater
            super.type="Repeater";
            super.health = 60;
        }else{}
    }

    {
        timer=new Timer(2000, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                World.peas.add(new Pea((int)type, x, y));
            }
        });

        timer3=new Timer(300, new ActionListener(){
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

        timer2=new Timer(10000, new ActionListener(){
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
        timer3.start();
        timer3.setDelay(2000);
        idle=false;
    }
    public void stop(){
        timer.stop();
        timer2.stop();
        timer3.stop();
        idle=true;
    }
    public void act(){
        timer2.start();
    }
}