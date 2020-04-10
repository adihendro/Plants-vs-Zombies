import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        World world = new World();

        frame.setTitle("Plants vs Zombies");
        frame.add(world);
        frame.setBounds(130,0, 1024, 646);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
