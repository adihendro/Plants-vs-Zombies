public class Zombie extends Actor{
    protected int zombieDamage;
    protected int zombieSpeed;

    public Zombie(String type){
        super.type=type;
        if(type.equals("Zombie")){
            health=50;
            zombieDamage=10;
            zombieSpeed=3;
        }else { //Football zombie
            health=80;
            zombieDamage=15;
            zombieSpeed=5;
        }
    }

    public void setSpeed(int speed){
        if(isTouching(Plant.class)){
            zombieSpeed=0;
        } else {
            zombieSpeed =speed;
        }
    }
}