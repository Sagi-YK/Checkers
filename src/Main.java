import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Checkers");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(646, 680);
		JPanel panel = new JPanel();
		frame.add(panel);
		Checkers c = new Checkers();
		frame.add(c);
		
		frame.setVisible(true);
	}

}
