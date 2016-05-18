import java.util.Iterator;

/**
 * This class allows for greater hit detection precision. The main idea 
 * is that each obstacle and the main T-Rex have a bounding box, which surrounds them.
 * When these bounding boxes intersect, these hit boxes cover the T-rex and obstacles
 * with greater precision by creating a number of small hit boxes that cover
 * the obstacles and T-Rex in a more precise manner that avoids white space.
 * When the large bounding boxes collide, the program checks if these smaller
 * hit boxes are colliding, and uses that to determine if there has been a 
 * collision. These hit boxes were created by looking at the image files 
 * pixel by pixel, and determining appropriate box dimensions to cover
 * the obtsacles and T-Rex.
 * @author danny_onorato
 *
 */
public class HitBox implements Comparable {
	//initial values
	public int x_0;
	public int y_0;
	//position values
	public int x;
	public int y;
	//dimensions
	public int height;
	public int width;
	
	//constructor
	public HitBox(int x, int y, int w, int h) {
		this.x_0 = x;
		this.y_0 = y;
		this.x = x;
		this.y = y;
	    height = h;
	    width  = w;
	}
	
	/**
	 * This checks if two hit boxes intersect, uses the same
	 * method as the larger bounding box, however there are many smaller
	 * hit boxes, so it becomes more precise since these boxes only cover the 
	 * actual object, not any whit space.
	 * @param other, hit box 
	 * @return boolean
	 */
	public boolean boxIntersect(HitBox other) {
		return(x + width >= other.x
				&& y + height >= other.y
				&& other.x + other.width >= x 
				&& other.y + other.height >= y);
	}
	
	@Override
	public int compareTo(Object o) {
		HitBox other = (HitBox)o;
		if(this.x + width > other.x + other.width) {
			return 1;
		} else if (this.x + width == other.x + other.width) {
			if(this.y + height > other.y + other.height) {
				return 1;
			} else if (this.y + height == other.y + other.height) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return - 1;
		}
	}

	
}
