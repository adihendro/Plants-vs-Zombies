public abstract class Actor{
    protected int health;

    //Abstract method
    public abstract void attack();

    public void hit(int damage){
        health-=damage;
    }

    //is Actor dead method
    public boolean isDead(){
        return health<=0;
    }
}