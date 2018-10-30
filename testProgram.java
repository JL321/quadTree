import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;

public class testProgram extends JFrame{

	private static JFrame window;
	private Random numGenerator;
	//private Shape player;
	private quadTree<Shape> tree;
	
	int radius;
	int maxHeight;
	int maxWidth;
	
	public static void main(String[] args) {
		
		window = new testProgram();

	}

	private testProgram() {
		
		//MAIN Constructor
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		numGenerator = new Random();
		maxHeight = (int)Math.round(screenSize.getHeight())-35; //-35 forces all balls to remain within the figure
		maxWidth = (int)Math.round(screenSize.getWidth());
		radius = 5;
		tree = new quadTree<Shape>(maxWidth, maxHeight);
		
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setResizable (false);
		
        for (int i = 0; i < 5000; i++) {
        	tree.addAgent(new Shape(radius, numGenerator.nextInt(maxWidth-radius*2), numGenerator.nextInt(maxHeight-radius*2), numGenerator.nextInt(9)-4,(numGenerator.nextInt(9) - 4)));
        }
        
        JPanel gamePanel = new GameAreaPanel ();
        this.add (gamePanel);

        MyKeyListener keyListener = new MyKeyListener ();
        this.addKeyListener (keyListener);

        this.requestFocusInWindow();

        this.setVisible (true);
        
	}
	
	private class GameAreaPanel extends JPanel {
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
            setDoubleBuffered(true);
            
            ArrayList<Shape> agents = tree.getList();
            
            for (int i = 0; i < agents.size(); i++) {
            	
            	Shape player = agents.get(i);
            	
            	if ((player.getX() + player.getDx() + radius > (maxWidth)) || (player.getX() + player.getDx() - radius < 0)) {
                	player.setDx(-(player.getDx()));
                } 
                
                if ((player.getY() + player.getDy() + radius>= (maxHeight)) || (player.getY() + player.getDy() - radius < 0)) {
                	player.setDy(-(player.getDy()));
                }
                
                
                g.fillRect(0, maxHeight, maxWidth, radius);
                
                player.newPos();
                
                g.setColor(Color.BLUE);
                g.fillOval(player.getX()-radius, player.getY()-radius, radius*2, radius*2);
            	
            }
            
            tree.drawGrid(g);
            
            tree.updateTree();
            
            repaint();
			
            //tree.display();
            
            //System.out.println("_----------------------_");
            
		}
		
	}
	
	 private class MyKeyListener implements KeyListener {
		 
		public void keyTyped (KeyEvent e) {

	    }

        public void keyPressed (KeyEvent e) {
        	if (KeyEvent.getKeyText(e.getKeyCode()).equals("M")) {
        		tree.addAgent(new Shape(radius, numGenerator.nextInt(maxWidth-radius), numGenerator.nextInt(maxHeight-radius), numGenerator.nextInt(9)-4, numGenerator.nextInt(9)-4));
        		
        	}
        }

        public void keyReleased (KeyEvent e) {
        }
		 
	 }
	
}
