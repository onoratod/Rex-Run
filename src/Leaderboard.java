import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This the Leaderboard class, which simulates a leader board. The leader board is a 
 * property of the game, not each simulation. So, each game instance reads in from the 
 * same file to keep the leader board constant throughout games. To do this, 
 * the leader board writes out to the same file and updates it. Keeps track of the 
 * three highest scores. The leader board writes out to the scores file in a specific
 * format which makes them easy to read. 
 * 
 * The format is: 
 * 
 * username,score
 * 
 * This way the reader knows where to search for the appropriate
 * game data. Spaces are allowed but no commas! This is why usernames are not
 * allowed to contain commas or be blank.
 * 
 * username   ,     score
 * 
 * is fine, since the reader trims after splitting the comma
 * 
 * However,
 * 
 * jackson,   , score is not a valid name as it will confuse the reader
 * 
 * These checks are done in the validName function
 */

public class Leaderboard {
	    //collections for keeping track of player data
		public static Map<Integer, String> playerMap = new TreeMap<Integer, String>();
		public static TreeSet<Integer> highScores = new TreeSet<Integer>();
		//reader and writer
		private BufferedReader br;
		private BufferedWriter out;
		
		
		public Leaderboard(String filename) {
			//create a file
			File in = new File(filename);
			
			try {
			    //create a reader  
				br = new BufferedReader(new FileReader(in));
				//read in the first line
			    String line = br.readLine();
			    //read through the file
			    while(line != null) {
			    	//split the line at a comma, for easier identification
			    	//of data
			    	String[] info = line.split(",");
			    	int highSc = Integer.parseInt(info[1].trim());
			    	String player = info[0].trim();
			    	//populate the collections with past game data
			    	playerMap.put(highSc, player);
			    	highScores.add(highSc);
			    	line = br.readLine();
			    }
			    //close
			    br.close();
			     
			    } catch (FileNotFoundException e) {
			    	System.out.println("File not found");
		 	    } catch (IOException e) {
			      System.out.println("Please enter a valid filename");
			    } 
	}
	
   /**
    * Checks if a given int, is a new high score
    * @param s, integer of new high score
    * @return boolean
    */
   public boolean isHighScore(int s) {
	   //if the collections are empty or have less than three elements
	   //automatically a high score
	   if(highScores.isEmpty() || highScores.size() < 3) {
		   return true;
	   }
	   //loop through and check the new score against each old score
	   for(int sc : highScores) {
		   if(s > sc) {
			   return true;
		   }
	   }
	   return false;
   }
   
   /**
    * Adds a high score to the collections by finding the lowest score
    * that is smaller than the new high score and replacing it.
    * @param s, new high score
    * @param name, username
    * @return void
    */
   public void addScore(int s, String name) {
	   //if less than three scores, automatically update
	   if (highScores.size() < 3) {
		   highScores.add(s);
		   playerMap.put(s, name);
		   
	   } else {
		   
		   //create an iterator and lowScore tracker, make 
		   //a descending tracker so I know the last score I find
		   //that is lower than the new score, is indeed the lowest of
		   //all the lower scores
		   Iterator it = highScores.descendingIterator();
		   int lowScore = -1;
	   
	       //iterate through and check the scores
		   while(it.hasNext()) {
			   int sc = (int) it.next(); 
			   if(s > sc) {
			   lowScore = sc;
		   }
		   
	   }
	   
	//check if a lowScore was found, if so update the collections
	   if(lowScore != -1) {
		   playerMap.remove(lowScore);
		   playerMap.put(s, name);
		   highScores.remove(lowScore);
		   highScores.add(s);
	   }
	  }
   }
   
   /**
    * This function writes out the high scores to the 'scores' file
    * so that the leaderboard can persist through continuously from each game
    * instance. The high scores are not a property of the Game, they are 
    * a continuous property.
    */
   public void writeScores() {
	   try {  
		      out = new BufferedWriter(new FileWriter("scores"));
		      //write the scores to the file in the format
		      for(int sc : highScores) {
		    	  out.write(playerMap.get(sc) + "," + sc + '\n');
		    	  
		      }
		      //close and flush
		      out.flush();
		      out.close();
	        } catch (FileNotFoundException e) {
			    	System.out.println("File not found");
		    } catch (IOException e) {
		      System.out.println("error while writing document: " + e.getMessage());
		    } finally {
		    	
		    }
   }
   
   /**
    * This functions makes sure that usernames conform to the reader format
    * so that it is easier to handle reading the file. Since it uses a comma to parse
    * information there can be no commas in the username, all other characters are valid.
    * Lastly, the user must enter a name so the username cannot be blank.
    * @param s, username to check
    * @return boolean
    */
   public boolean validName(String s) {
	   //check empty
	   if(s.isEmpty()) {
		   return false;
	   }
	   //make name into char array and loop through
	   //to check for invalid characters
	   char[] name = s.toCharArray();
	   for(int i = 0; i < name.length; i++) {
		   if(name[i] == ',') {
			   return false;
		   }
	   }
	   return true;
   }
   
   
   /**
    * This function is used by the Game class to easily construct a 
    * string that represents the leader board information to be displayed
    * to the user.
    * @return
    */
   public String constructHighScoreTable() {
	   String table = "";
	   int place = 1;
	   Iterator it = highScores.descendingIterator();
	   while(it.hasNext()) {
		   int sc = (int) it.next();
		   String message = (place + ". User: " + playerMap.get(sc) + ", Score: " + sc + '\n');
	       table += message;
	       place++;
	   }
	   
	   return table;
   }
}
