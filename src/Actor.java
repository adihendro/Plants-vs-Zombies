public abstract class Actor{
    protected int health;

    //Abstract method
    public abstract void attack();

    public void hit(int damage){
        health-=damage;
    }

    //check is Actor dead
    public boolean isDead(){
        return health<=0;
    }
}