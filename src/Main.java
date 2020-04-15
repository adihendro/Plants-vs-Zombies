import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame = new JFrame();

        frame.add(new World());
        frame.setTitle("Plants vs Zombies");
        frame.setBounds(130,0, 1024, 648);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
