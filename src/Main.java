import javax.swing.JFrame;


public class Main{
    public static void main(String[] args){
        JFrame frame = new JFrame();

        int inset;
        if(System.getProperty("os.name").equals("Mac OS X")){
            inset=23;
        }else if(System.getProperty("os.name").equals("Windows")){
            inset=30;
        }else{
            inset=30;
        }
        System.out.println(System.getProperty("os.name"));

        frame.add(new World());
        frame.setTitle("Plants vs Zombies");
        frame.setBounds(130,0, 1024, 625+inset);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
