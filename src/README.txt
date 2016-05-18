=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: onoratod
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. You may copy and paste from your proposal
  document if you did not change the features you are implementing.

  1. I/O - I will use I/O to keep track of high scores and the game leaderboard. I will read in information from
  the user, specifically his username. I will have to check the user's score against the total high scores.
  These scores will be kept track of in a file called scores, that I will write over to update. I will track the 
  three highest scores, if a new score is detected, I will get the users name and write his name and score the the file
  The LeaderBoard class will implement this I/O code.

  2. OO Inheritance - My game has obstacles and moveables, which share many functions. To be more efficient with code, and consistent
  across my game these objects will extend a super class called GameObj and add to and override its functions if necessary. This will allow me
  to easily call functions across different types of game objects without have to check what time of object they are.

  3. Collections - I will need to keep track of many game objects: obstacles, hit boxes, moveables, the ground etc.
  To make this tracking and updating easier, I will store these objects in collections that I can simply iterate over, and 
  since they extend a super class I can call a single function on each type of object like move() that will update all of these objects
  and keep my code consistent in a very concise fashion. I use Sets to keep track of obstacles, moveables, and hit boxes. I also use a Map and a Set to 
  keep track of leader board data. The Map allows me to easily associate a users score to their name. 

  4. Advanced Collision Detection - Since my game is based around hit detection it is important to have precise hit detection.
  My hit detection builds on the bounding box idea by using objects called hit boxes. Essentially each obstacle and the t-rex have a large
  box which bounds them that I use for initial hit detection, so I don't have to do a ton of checks every tick. These hit boxes, cover the obstacles and
  t-rex and allow for more accurate hit detection. I looked at each image file, and modeled the image files with a bunch of small boxes
  that covered the image and none of the white space. So, when two bounding boxes intersect, each hit box is checked against the other objects hit 
  boxes to see if an actual collision has occured, not just white space against white space. Essentially, these smaller boxes, allow me to detect collisions
  on a more accurate scale.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Cloud - a class which is used to model cloud objects in the background. They implement the moveable 
  interface. All they do is move, and wrap around.
  
  Game - this class connects users interactions with the game state.
  
  GameCourt - this keeps track of and models the game state. It has collections that store all of the obstacles
  and moving objects. It also holds a reference to the game's leader board. Each game tick, this class updats the 
  position of each moving object by iterating over these collections. It also checks for game ending conditions, and 
  wraps all of the objects around to create a seemingly infinite terrain. It keeps track of the user information and score,
  and prompts the user if they have achieved a high score, if so it interacts with leader board to update the high scores.
  
  GameObj - this is an encompassing class that models any object in the game. Cloud, T-Rex, Obstacle, Moveable extend this
  class. Game objects must be able to move, check for intersection, check if the object is off screen,
  wrap the object back around, keep track of its own position, and velocity. 
  
  GroundOne - This class represents the ground of the game. The ground must move to 
   simulate the idea that the t-rex is moving forwards. To avoid a large
   continuous ground, I have two parts that simply continuously wrap around. This extend Game Object
   and has a special wrap function so that the ground wraps properly.
   
   HitBox - This class allows for greater hit detection precision. The main idea 
   is that each obstacle and the main T-Rex have a bounding box, which surrounds them.
   When these bounding boxes intersect, these hit boxes cover the T-rex and obstacles
   with greater precision, by creating a number of small hit boxes that cover
   the obstacles and T-Rex in a manner that avoids white space.
   When the large bounding boxes collide, the program checks if these smaller
   hit boxes are colliding, and uses that to determine if there has been a 
   collision. These hit boxes were created by looking at the image files 
   pixel by pixel, and determining appropriate box dimensions to cover
   the obtsacles and T-Rex.
   
   LeaderBoard - This class simulates a leader board. The leader board is a 
   property of the game, not each instance. So, each game instance reads in from the 
   same file to keep the leader board constant throughout games. To do this, 
   the leader board writes out to the same file, 'scores', and updates it. It keeps track of the 
   three highest scores and their users with a scores Set and a Scores -> User map. The leader board writes out to the scores file in a specific
   format which makes them easy to read. Each game instance, reads in from thsi file to populate its leaderboard
   data structures.
   
   The format is: 
   
   username,score
   
   This way the reader knows where to search for the appropriate
   game data. Spaces are allowed but no commas! This is why usernames are not
   allowed to contain commas or be blank.
   
   username   ,     score
  
   is fine, since the reader trims after splitting the comma
   
   However,
   
   jackson,   , score is not a valid name as it will confuse the reader
   
   These checks are done in the validName function
   
   Moveable - Interface that enforces that game objects that move must have a move() function
   . This allows me to just call a move() function on every GameObj instead 
   of being confused about each objects path of movement or function name. It allows
   for consistency over the game state. All moveables must also have a compareTo method
   since I want to store moveables in collections so that I can easily iterate over the
   collection, calling move() on each object. This move function varies depending
   on what type of object it is, and how its move function is implemented, but this interface allows
   me to easily update every objects position by easily calling move. This allows for better
   game state consistency, and less and more efficient code.
   
  Obstacle - This class represents any game object which can end the
   game by colliding with the T-Rex. They move so they implement moveable, and
   they are stored in collections that I want to compare, so they implement comparable.
   Each obstacle also contains a collection of hit boxes that cover the obstacle
   in a precise manner for greater hit detection accuracy. 
   
   T-Rex - the main character of the game.  This class represents the T-Rex in the game.
    He extends the GameObj class and inherits a few functions from that class
    to be more efficient with code. He also has his own functions, like jump which use simple physics
    to create a parabolic jump. He never moves, the world around him simply moves and wraps around to
    simulate running. He also implements the Moveable interface, which requires that objects that move
    have a move() function. This just allows for consistency over the game. He also
    has a collection of hit boxes, which are used for greater precision during 
    collision detection.


- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?
  
  I kept OO inheritance. I/O system for keeping track of high scores. Collections for 
  modeling and keeping track of the game state. I changed the JUnit component because it did not seem
  feasible. I added advance collision detection to make my game more advanced and seem more like the game that
  it is attempting to emulate. 


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Implementing the I/O system took some consideration about how to handle user input, how to model the file 
  so that it would be easy to read. How to make sure that the user conformed to the file reader format. Also, ran into trouble 
  with the hit box implementation for greater hit detection accuracy. I ran into some issues with updating the positions of my hit
  boxes with their associated object.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  The functionality is separated between a User interface which takes in input, a game state, which uses this
  input to update itself. And then objects which the game state controls to manipulate the visual component of the game/
  The user cannot change the private state in any way directly, so it is protected from user manipulation. I would probably model my 
  collections and object inheritance in a better way if I had the chance.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I used Google's open source library for its t-rex game to get the images for my 
  game. I also consulted Java docs and tutorials for how to create dialogues boxes and
  manipulate user input. 


