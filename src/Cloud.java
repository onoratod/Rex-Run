import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * This Cloud class simulates the background cloud images. This class inherets
 * a lot of functions from the super GameObj class. It also implements
 * Moveable since it has to move.
 * @author danny_onorato
 *
 */
public class Cloud extends GameObj implements Moveable {
	//image file
	public static final String img_file = "cloud.png";
	//dimensions
	public static final int HEIGHT = 40;
	public static final int WIDTH = 12; 
	//inital values
	public static int INIT_VEL_X ;
	public static final int INIT_VEL_Y = 0;
	//velocity, can change
	public int vx = INIT_VEL_X;
	public int vy = INIT_VEL_Y;

	
	private static BufferedImage img;
	
	//constructor
	public Cloud(int courtWidth, int courtHeight, int x, int y, int v) {
		//passes info to super
		super(v, INIT_VEL_Y, x, y, HEIGHT, WIDTH, courtWidth,
				courtHeight);
		this.INIT_VEL_X = v;

		//reads in file and crafts an image
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}
	
	/**
	 * Must implement the move function for Moveable interface
	 */
	public void move() {
		//only moves horizontally
		this.pos_x += -vx;
		this.pos_y += 0;
	}
	
	@Override
	public void wrap() {
		pos_x = 550;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}

}
