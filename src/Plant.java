public class Plant<T> extends Actor{
    private T type;
    private int x, y; //array for plants location
    // private int health;
    private int[][] p_occ = new int[5][9];
    
    public Plant(T type, int x, int y){
        this.type=type;
        this.x=x;
        this.y=y;
        if(type.equals(1)){ //Sunflower
            super.type="Sunflower";
            super.health = 50;
        }else if(type.equals(2)){ //Peashooter
            super.type="Peashooter";
            super.health = 50;
        }else if(type.equals(3)){ //Repeater
            super.type="Repeater";
            super.health = 70;
        }else{}
    }

    //getter
    public int X(){return x;}
    public int Y(){return y;}
    public T getType(){return type;}
    
    //setter
    public void setP(int i, int j){
        p_occ[i][j]=0;
    }
    
    public boolean put(int x, int y, T type){
        if(p_occ[x][y]==0){ //empty spot
            p_occ[x][y]=(int)type;
            World.plants.add(new Plant<Integer>((int)type, x, y));
            return true;
        }else{
            return false;
        }
    }
    public void shoot(){

    }
    

}