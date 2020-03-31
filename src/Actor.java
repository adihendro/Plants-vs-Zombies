public abstract class Actor{
    protected String type;
    protected int health;

    // Abstract method
    public abstract void attack();
    public void hit(int damage){
    	health -= damage;
    }
    ;

    // Method apakah mati
    protected boolean isDead(){
        return health<=0;
    }
}