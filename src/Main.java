import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        World world = new World();

        frame.setTitle("Plants vs Zombies");
        frame.add(world);

        //Get the top taskbar height
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());

        frame.setBounds(130,0, 1024, 625+screenInsets.top);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
