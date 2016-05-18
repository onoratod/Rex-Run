/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.ImageIcon;

//import javax.swing.text.html.HTMLDocument.Iterator;
import java.util.Iterator;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. 
 * 
 */

@SuppressWarnings("serial")
public class GameCourt extends JPanel {


	private TRex rex; //the trex
	private Cloud cloud; //background objects
	private GroundOne part1; //the ground
	private GroundOne part2; //the hidden ground
	
    public int gameScore = 0; //score of the game
	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)
	private JLabel score; //the label that displays the score
	public boolean jumping = false; //is the character jumping
	public boolean started = false; //whether the rex is jumping
	public boolean gameOver = false; //whether the game is over
	
	JButton start; //start button
	Leaderboard lead; //the leaderboard for the game
	JFrame gameFrame; //the frame which holds the game
	public boolean nameEntered = false; //checks if the user has entered there name
	public String username = ""; //initial username is blank, which is not valid
	

	// Game constants
	public static final int COURT_WIDTH = 500;
	public static final int COURT_HEIGHT = 300;
	
	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;
	
	//collection storing the objects that need to be updated
	//with the game state
	public Collection<GameObj> moveables = new TreeSet<GameObj>();
	public Collection<GameObj> obstacles = new TreeSet<GameObj>();

	//constructor, pass in some swing components so that I can interact with the user
	//while the game is being played
	public GameCourt(JLabel status, JLabel score, JButton start, JFrame frame) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); 


		setFocusable(true);

		//jump key listener
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if(!(rex.pos_y < rex.INIT_Y)) { //only jump while not in the air
						jumping = true; //update jumping tracker
						rex.v_y = 20; //give an upwards velocity
					}
				}
			
			}
		});
		

		//some JComponents for interacting with the user,
		//ie. updating them that they got a high score etc.
 		this.status = status;
		this.score = score;
		this.start = start;
		//initialize the leader board using the scores file
		this.lead = new Leaderboard("scores");
		this.gameFrame = frame;
		
		
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {

		rex = new TRex(COURT_WIDTH, COURT_HEIGHT);
		Cloud cloud1 = new Cloud(COURT_WIDTH, COURT_HEIGHT, 200, 45, 3);
		Cloud cloud2 = new Cloud(COURT_WIDTH, COURT_HEIGHT, 340, 70, 4);
		GroundOne part1 = new GroundOne(COURT_WIDTH, COURT_HEIGHT, 7, 0, "floor1.png");
		GroundOne part2 = new GroundOne(COURT_WIDTH, COURT_HEIGHT, 7, part1.width, "floor2.png");
		
		//checks if there is anything to clear
		if(!moveables.isEmpty()) {
			moveables.clear();
		}
		//add moveables
		moveables.add(cloud1);
		moveables.add(cloud2);
		moveables.add(part1);
		moveables.add(part2);

		//create obstacles
		Obstacle cactus = new Obstacle(COURT_WIDTH, COURT_HEIGHT, "l_cactus.png", 
				                                17, 32, 7, 300, 247);
		Obstacle cactus2 = new Obstacle(COURT_WIDTH, COURT_HEIGHT, "l_cactus.png", 
                17, 32, 7, 400, 247);
		Obstacle cactus3 = new Obstacle(COURT_WIDTH, COURT_HEIGHT, "l_cactus.png", 
                17, 32, 7, 590, 247);
		Obstacle cactus4 = new Obstacle(COURT_WIDTH, COURT_HEIGHT, "l_cactus.png", 
                17, 32, 7, 930, 247);
		Obstacle cactus5 = new Obstacle(COURT_WIDTH, COURT_HEIGHT, "l_cactus.png", 
                17, 32, 7, 960, 247);
		
		//same check as above
		if(!obstacles.isEmpty()) {
			obstacles.clear();
		}
		//add obstacles
		obstacles.add(cactus);
		obstacles.add(cactus2);
		obstacles.add(cactus3);
		obstacles.add(cactus4);
		obstacles.add(cactus5);

		//playing = true;
		if(started) {
			status.setText("Running..."); //update label
		}
		//set score to zero
		gameScore = 0;
		
		//get username
		if(!nameEntered) {
    		//prompt the user for his name
    		//no commas allowed!
    		boolean invalidName = true; //initial name is invalid
    		
    		//check that the user has entered a valid name! If not keep
    		//prompting them for their name
    		while(invalidName) {
    		 username = (String)JOptionPane.showInputDialog(
                    gameFrame,
                    "Please Enter Username:\n"
                    + "No commas allowed, and you must enter a username",
                    "Create Username",
                    JOptionPane.PLAIN_MESSAGE, null, null,
                    "");
    		 
    		 //this checks to see if the user exited the dialogue, or pressed cancel
    		 //if they did, reprompt them.
    		 if(username != null) {
    				if(lead.validName(username.trim())) {invalidName = false;}
    			}
    		  }
    		}
		    //if they pass all tests then they have entered a valid name
    		nameEntered = true;
    	
	
		
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}
	
	public void getFocus() {
		requestFocusInWindow();
	}

	
	
	
	/**This function incorporates advanced hit detection. It is similar to 
	 * the box and bound method, and even implements the box and bound method.
	 * The first thing it does is check whether two objects are in each others bounding box,
	 * if they are, then it goes into a more advanced detection. Essentially, each object
	 * has HitBoxes associated with it, these are smaller boxes within the bounding box, that 
	 * cover the object in more detail. This way, I can have finer hit detection.
	 * @param rexBoxes, the T-Rex's hit boxes
	 * @param obBoxes, the Obstacles hit boxes
	 * @return boolean
	 */
	private boolean advancedDetect(Set<HitBox> rexBoxes,
								   Set<HitBox> obBoxes) {
		//create iterator
		Iterator<HitBox> outer = rexBoxes.iterator();
		
		//iterate through the hit boxes and check whether they intersect
		//with any of the Obstacles hit boxes
		while(outer.hasNext()) {
			HitBox rexBox = outer.next();
			Iterator<HitBox> inner = obBoxes.iterator();
			while(inner.hasNext()) {
				HitBox obBox = inner.next();
				//this calls the advanced hit detection method
				//which is a part of every HitBox object
				if(rexBox.boxIntersect(obBox)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * This checks whether two objects bounding boxes meet, before doing advanced detection
	 * this allows my program to run faster since it does not have to check 20 boxes every tick
	 *
	 * @param check, obstacles to check for collision with
	 * @return
	 */
	private boolean collision(Collection<GameObj> check) {
		for(GameObj ob : check) {
			if(ob.intersects(rex)) {
				//Im inside the large hit box
				if(ob instanceof Obstacle) {//I know it is an obstacle
					Obstacle obs = (Obstacle) ob;
					//do advanced collision detection
					return advancedDetect(rex.hitBoxes, obs.hitBoxes);
				}
			}		
		}
		return false;
	}
	

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			//move all moveables one step forward
			for(GameObj mov : moveables) {
			    //if the obstacle is offscreen	
				if(mov.offScreen()) {
			    		mov.wrap(); //wrap it around
			    	}
			    	mov.move();//and then move it
			    }
			    
			//move all obstacles one step forward   
			for(GameObj ob : obstacles) {
				if(ob instanceof Obstacle) {
	    			Obstacle o = (Obstacle) ob;
			    	if(ob.offScreen()) {
				    		o.wrap(obstacles);
			    	}
			    	o.move();
			    }
			}
			    //update the score
			    score.setText("Score: " + gameScore++);
			    //if rex is jumping, update the jump
			    if(jumping) {
			    	rex.jump(); 
			    }
			    
			    //if he is not jumping, reset
				if(rex.pos_y > rex.INIT_Y) {
					rex.pos_y = rex.INIT_Y;
					for(HitBox b : rex.hitBoxes) {
						b.x = b.x_0;
						b.y = b.y_0;
					}
					rex.v_y = 0;

					jumping = false;
				}
				
				//check for collisions, and perform endgame actions
				if(collision(obstacles)) {
			    	playing = false;
			    	status.setText("Game Over!");
			    	gameOver = true;
			    	start.setText("Start Over");	
			    }
				
		
            //update display
			repaint();
			
			//update high scores, and prompt user
		    if(gameOver) {
		    	if(lead.isHighScore(gameScore)) {
		    		lead.addScore(gameScore, username);
		    		JOptionPane.showMessageDialog(gameFrame, "Congratulatons, you got a high " +
		    				"Score", "High Score", JOptionPane.PLAIN_MESSAGE);
		    	}
		    	//write out scores to file
		    	lead.writeScores();
		    }
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 500, 300); // white background
		g.setColor(Color.RED);
		rex.draw(g);
		
		//draw all moveables
		for(GameObj mov : moveables) {
			mov.draw(g);
		}
		//draw all objects
		for(GameObj ob : obstacles) {
			ob.draw(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
