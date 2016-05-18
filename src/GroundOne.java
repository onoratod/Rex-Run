import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * This class represents the ground of the game. The ground must move to 
 * simulate the idea that the trex is moving forwards. To avoid a large
 * continuous ground, I have two parts that simply continuously wrap around.
 * @author danny_onorato
 *
 */
public class GroundOne extends GameObj implements Comparable<Object>, Moveable {
	//dimensions
	public static final int HEIGHT = 504;
	public static final int WIDTH = 15;
	//initial values
	public static int INIT_X; 
	public static final int INIT_Y = 265; 
	public static int INIT_VEL_X; 
	public static final int INIT_VEL_Y = 0;
	public int vx = INIT_VEL_X; 
	public int vy = INIT_VEL_Y; 
	//image file
	public String img_file;
	
	private static BufferedImage img;
	
	public GroundOne(int courtWidth, int courtHeight, int v, int init_x, String file) {
		super(v, INIT_VEL_Y, init_x, INIT_Y, HEIGHT, WIDTH, courtWidth,
				courtHeight);
		
		this.INIT_VEL_X = v;
		this.INIT_X = init_x;
		this.img_file = file;
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	/** 
	 * Overrides the GameObj wrap method, since it has to be much more specific
	 * so that it seems like each part of the ground seamlessy connects with the 
	 * next.
	 */
	@Override
	public void wrap() {
		pos_x = HEIGHT; // because of the offScreen constraint along with, the position of part2
	}
	
	/**
	 * Redefines the offScreen function to make it easier to write the 
	 * wrap() function above so the two parts connect to each other 
	 * easily. 
	 */
	@Override
	public boolean offScreen() {
		return (pos_x <= -HEIGHT);
	}
	
	/**
	 * Implements Moveable, so must have this function.
	 */
	public void move() {
		 this.pos_x += -vx;
		 this.pos_y += vy;
	}

		@Override
		public void draw(Graphics g) {
			g.drawImage(img, pos_x, pos_y, width, height, null); 
		}
	
	
}
