import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * This Obstacle class represents any game object which can end the
 * game by colliding with the T-Rex. They move so they implement moveable, and
 * they are stored in collections that I want to compare, so they implement comparable.
 * Each obstacle also contains a collection of hit boxes that cover the obstacle
 * in a precise manner for greater hit detection accuracy. 
 * @author danny_onorato
 *
 */
public class Obstacle extends GameObj implements Comparable<Object>, Moveable {
	//initial values 
	public static final int INIT_VEL_Y = 0;
	public int vy = INIT_VEL_Y;
	//hit boxes
	public Set<HitBox> hitBoxes = new TreeSet<HitBox>();


	private static BufferedImage img;
	
	public Obstacle(int courtWidth, int courtHeight, String sprite, int HEIGHT, int WIDTH, int v
													, int x, int y) {
		super(v, INIT_VEL_Y, x, y, HEIGHT, WIDTH, courtWidth,
				courtHeight);
		try {
			if (img == null) {
				img = ImageIO.read(new File(sprite));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		//hit boxes calculated according the the image file
		HitBox bottum = new HitBox(pos_x + 6, pos_y + 20, 5, 10);
		HitBox middle = new HitBox(pos_x, pos_y + 7, 17, 13);
		HitBox top = new HitBox(pos_x + 6, pos_y, 5, 7);
		hitBoxes.add(bottum);
		hitBoxes.add(middle);
		hitBoxes.add(top);

		
		
	}
	
	@Override
	/**
	 * Needs to update the hit boxes associated with it as it moves.
	 */
	public void move() {
		 this.pos_x += -v_x;
		 for(HitBox box : hitBoxes) {
			 box.x += -v_x;
		 }
		 this.pos_y += 0;
	}
	
	/**
	 * This function finds the obstacle that is the furthest
	 * distance away from the t-rex in x distance. This allows me 
	 * to randomly wrap obstacles around in a way that makes the 
	 * game seem like it is randomly generating new infinite terrain.
	 * @param obs, takes in a collection of obstacles to loop through.
	 * @return
	 */
	private int findFurthestX(Collection<GameObj> obs) { 
	    int max = -100;
	    //loop through and find the furthest one
		for(GameObj o : obs) {
			if(o.pos_x > max) {
				max = o.pos_x;
			}
		}
		return max;
	}
	
	
	/**
	 * This class has a special wrap function, in addition to the 
	 * one it inherets from GameObj. This function overloads that wrap() function
	 * . This function is used for more advanced wrapping that makes it
	 * seem like the game is running forever, when really around 6 obstacles
	 * are wrapping around in a random fashion to seam like new terrain.
	 * @param obs
	 */
	public void wrap(Collection<GameObj> obs) {
		//find the last one
		int max = findFurthestX(obs);
		//two random generators
		int smallVsBig = (int) (Math.random() * 10);
		int d;
		//generate the random distance
		if(smallVsBig <= 7) {
			d = 100 + (int) (Math.random() * 241); // for large distances
		} else {
			d = 15 + (int) (Math.random() * 16); //for small distances to have two close together
		}
		
		this.pos_x = max + d;
		//update hitBoxes
		for(HitBox box : hitBoxes) {
			 box.x = this.pos_x;
		 }
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, pos_x, pos_y, width, height, null);
	}
	
	/**
	 * This is a function unique to obstacles. They can end the game
	 * when they collide with the T-Rex. They can update the game state
	 * to stop it from continuing.
	 * @param game
	 */
	public void end(GameCourt game) {
		game.playing = false;
	}
	
	@Override
	public int compareTo(Object o) {
		Obstacle other = (Obstacle) o;
		
		if(this.pos_x > other.pos_x) {
			return 1;
		} else if(this.pos_x == other.pos_x) {
			return 0;
		} else {
			return -1;
		}
		
	}
}
