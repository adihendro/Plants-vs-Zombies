public abstract class Actor{
    protected String type;
    protected int health;

    protected boolean isDead(){
        return health<=0;
    }
}