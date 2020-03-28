public class Plant extends Actor{
    private int price;
    public Plant(String type){
        super.type=type;
        if(type.equals("Sunflower")){
            price=50;
        }else if(type.equals("Peashooter")){
            price=100;
        }else{ //Repeater
            price=150;
        }
    }
    public int getPrice(){
        return price;
    }

}