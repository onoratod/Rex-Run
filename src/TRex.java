/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * This the TRex class which represents the T-Rex in the game
 * He extend the GameObj class and inherets a few functions from that class
 * to be more efficient with code. He also has his own functions. He also 
 * implements the Moveable interface, which requires that objects that move
 * have a move function. This just allows for consistency over the game. He als
 * has a collection of hit boxes, which are used for greater precision during 
 * collision detection.
 */
public class TRex extends GameObj implements Moveable {
	public static final String img_file = "rex.png"; //image file
	private String img_file2; //animation image files
	private String img_file3; //animate image files for running
	//dimensions
	public static final int HEIGHT = 25; 
	public static final int WIDTH = 27; 
	//initial values
	public static final int INIT_X = 20; 
	public static final int INIT_Y = 250; 
	public static final int INIT_VEL_X = 0; 
	public static final int INIT_VEL_Y = 0;
	//values that can change
	private int x = INIT_X;
	private int y = INIT_Y;
	public int vx = INIT_VEL_X; 
	public int vy = INIT_VEL_Y; 
	
	//a set of hit boxes, for advanced detection
	public Set<HitBox> hitBoxes = new TreeSet<HitBox>();
	
	//iterator for animating the T-Rex
	private int iter = 1;
	
	//image for animating
	BufferedImage animate;
	
	//different steps in the animation
	private static BufferedImage img;
	private static BufferedImage step;
	private static BufferedImage run;

	//constructor
	public TRex(int courtWidth, int courtHeight) {
		//passes everything onto gameObj constructor
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, HEIGHT, WIDTH, courtWidth,
				courtHeight);
		
	    //reading in images, and checking for exceptions
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
                this.step = ImageIO.read(new File("rex_step.png"));
                this.run = ImageIO.read(new File("rex_run.png"));
                this.animate = img;
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		//creating pre-calcuated hitBoxes
		HitBox legs = new HitBox(INIT_X + 6,  INIT_Y + 21, 9, 6); 
		HitBox torso = new HitBox(INIT_X + 4,  INIT_Y + 10, 13, 11); 
		HitBox tail = new HitBox(INIT_X,  INIT_Y + 9, 4, 11); 
		HitBox arms = new HitBox(INIT_X + 17,  INIT_Y + 12, 3, 3);
		HitBox head = new HitBox(INIT_X + 12,  INIT_Y, 13, 10); 
		//adding them to the set
		hitBoxes.add(legs);
		hitBoxes.add(torso);
		hitBoxes.add(tail);
		hitBoxes.add(arms);
		hitBoxes.add(head);
		
	}
	
	//the TRex does not move, he only jumps
	public void move() {
		 this.pos_x += 0;
		 this.pos_y += 0;
		 for(HitBox box : hitBoxes) {
			 box.x += 0;
			 box.y += 0;
		 }
	}
	
	//jumping
	public void jump() {	
		pos_y += -v_y;
		
		//move hit boxes as well
		for (HitBox box : hitBoxes) {
			box.y += -v_y;
		 }
		v_y -= GRAVITY; //update gravity to simulate parabolic jump
		
	}
	

	//overriding the draw function to account for 
	//animating the t-rex
	@Override
	public void draw(Graphics g) {
		
		if(iter == 1) {
			animate = img;
			iter ++; 
		} else if(iter == 3) {
			animate = step;
			iter++;
		} else if(iter == 5) {
			animate = run;
			iter ++;
		} else if (iter == 7){
			iter = 1;
		} else {
			iter++;
		}
		g.drawImage(animate, pos_x, pos_y, width, height, null); 
	}

}

