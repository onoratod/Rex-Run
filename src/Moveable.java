/**
 * Inteface that enforces that objects that move must have a move() function
 * . This allows me to just call a move() function on every GameObj instead 
 * of being confused about each objects path of movement or function name. It allows
 * for consistency over the game. All moveables must also have a compareTo method
 * since I want to store moveables in collections so that I can easily iterate over the
 * collection, calling move() on each object, and then this move function varies depending
 * on what time of object it is, and how its move function is implemented.
 * @author danny_onorato
 *
 */
public interface Moveable {
	public int vx = 0;
	public int vy = 0; 
	
  void move();

int compareTo(Object o);	
}
