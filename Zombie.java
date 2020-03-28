public class Zombie extends Actor{
    protected int zombieHealth;
    protected int zombieDamage;
    protected int zombieSpeed;
    }

protected void setHealth(int  health){
        zombieHealth = health;
    }

protected void setDamage (int damage){
    zombieDamage = damage;
}

protected void setSpeed (int speed){
    if(isTouching(Plant.class)){
        zombieSpeed=0;
    } else {
        zombieSpeed =speed;
    }
}
