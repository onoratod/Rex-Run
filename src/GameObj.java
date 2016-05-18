/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  moves.
 */
public class GameObj implements Comparable<Object>, Moveable {
    //gravity constant
	public static final int GRAVITY = 3;
	public String img_file;
	public int pos_x; 
	public int pos_y;

	/** Size of object, in pixels */
	public int width;
	public int height;
	
	/** Velocity: number of pixels to move every time move() is called */
	public int v_x;
	public int v_y;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public int max_x;
	public int max_y;

	//hit boxes keeping track of all obstacle and rex hit boxes
	Set<HitBox> hitBoxes = new TreeSet<HitBox>();
	
	/**
	 * Constructor
	 */
	public GameObj(int v_x, int v_y, int pos_x, int pos_y, 
		int width, int height, int court_width, int court_height){
		this.v_x = v_x;
		this.v_y = v_y;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.width = width;
		this.height = height;

	}


	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public void move(){
		pos_x += v_x;
		pos_y += v_y;
	}

	/**
	 * Determine whether this game object is currently intersecting
	 * another object.
	 * 
	 * Intersection is determined by comparing bounding boxes. If the 
	 * bounding boxes overlap, then an intersection is considered to occur.
	 * 
	 * @param obj : other object
	 * @return whether this object intersects the other object.
	 */
	public boolean intersects(GameObj obj){
		return (pos_x + width >= obj.pos_x
				&& pos_y + height >= obj.pos_y
				&& obj.pos_x + obj.width >= pos_x 
				&& obj.pos_y + obj.height >= pos_y);
	}

	
    /**
     * Checks whether the object is offscreen so that it
     * can wrap around.
     * @return
     */
	public boolean offScreen() {
		if(pos_x + width < -2) {
			return true;
		}
		
		else {
			return false;
		}
		
	}

	
	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	public void draw(Graphics g) {
	}

	/**
	 * Function which tells the objects how to wrap around to the other side
	 * . This is the default method, however, objects that extend this class
	 * can override this for custom wrap methods.
	 */
	public void wrap() {
		pos_x = max_x + width + 15;
	}
	
	@Override
	public int compareTo(Object o) {
		GameObj other = (GameObj)o;
		if(this.pos_x > other.pos_x) {
			return 1;
		} else if (this.pos_x == other.pos_x) {
			if(this.pos_y > other.pos_y) {
				return 1;
			} else if (this.pos_y == other.pos_y) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return - 1;
		}
	}
	
}
