import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 

public class Wallnut {
    private int type;
    private int damage;
    private int x, y; 
    private int coorX, coorY;
    private static point [][] wallnut_f= new Point [5][9];
    private Clip clip;
}

public Wallnut (int type, int x, int y){
    this.type=type;
    this.x=x;
    this.y=y;
    convert(x,y);
    if(type==4){
        damage=7; //normal wallnut
    } else {}
    try{
        clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(Audio.class.getResource(("Assets/Eat.wav")))); 
    }catch(Exception ex)  { 
        ex.printStackTrace();
    }
}

    static{
        for (int i=0;i<5;i++){
            for (int j=0; j<9;j++){
                //set coordinat wallnut
                wallnut_f[i][j] = new Point(296+j*81 +28,117+i*98 -19);
            }
        }
    }
    //getter
    public int getCoorX(){
        return coorX;
    }
    public int getCoorX(){
        return coorX;
    }
    public int X(){
        return x;
    }
    public int Y(){
        return y;
    }
    public int getType(){
        return type;
    }
    public int getDamage(){
        return damage;
    }

    public void convert(int i, int j){
        coorX=wallnut_f[i][j].getX();
        coorY=wallnut_f[i][j].getY();
    }

    public void eat(){
        clip.start();
    }
}
