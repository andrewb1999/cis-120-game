=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: butta
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1.    Collections:  The CannonBalls fired from the cannon are modelled using a LinkedList.
  When a new cannonBall is fired it is added to the LinkedList.  When the cannonBall touches the
  edge of the GameCourt it is removed from the LinkedList.  When the cannon is drawn it also
  iterates through all of the cannonBalls to draw them.  A LinkedList is good to model
  cannonBalls because it is possible to have more than one cannonBall fired at the same angle
  in the List at the same time.

  2.    I/O:  The game uses file I/O to save the top 5 high scores of the game.  The score
  is calculated by the number of coins collected before the player is hit by a cannonball.
  If the player’s new score is in the top 5 scores they will be given an option to enter
  their name to save the score.  The high scores are displayed next to the game and updated
  when a new high score is achieved.  The player's name can be made up of alphabetic characters
  and numbers and can be 15 characters long.  The players name is written to the file surrounded
  in quotes to prevent confusion between the name that possibly contains spaces and the score
  which is written on the same line as the name.

  3. Inheritance/Subtyping for Dynamic Dispatch:  Each coin/power-up extends the Coin abstract
  class.  The coin abstract class extends CircleObj and implements CollectibleObject.
  CollectibleObject has a method called modifyState.  Each type of coin overrides this method.
  The method is then called when the Coin is collected by the player.  The ScoreCoin increments
  the score, the InvincibilityCoin makes the player invincible for 5 seconds, and the
  DoubleCoinsCoin gives 2 points for every ScoreCoin collected for 5 seconds.

  4. Testable Component:  I created the CoinRing data structure which is described in detail in the
  interface.  In general, the CoinRing is a data structure that models the placement of coins for the
  play to retrieve.  The CoinRing can contain a max of 36 coins, representing each of the 10 degree
  sections in a circle (0 degrees - 350 degrees).  I then implemented the CoinRing using HashSets
  because the logic of compareTo does not fit well with the way the different coins extend the
  Coin abstract class.  I then wrote tests to test this implementation.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Swing
  -----
  -Game: Provides the main functionality to create the game in swing.
  Has the main method with Swing.InvokeLater()

  Main Logic
  ----------
  -GameCourt: The main logic of the game.  This is where timing is done.
  -Drawable: Interface that implements the draw method.
  -GameObj: Logic for an object that is on the screen and has a position and can
  move.  Object is moved by setting the x and y position of the top left corner.
  -CircleObj: Extends GameObj and wraps its methods for a circular object that has
  a radius and x and y based off the circle center.

  Ship
  ----
  -Ship: Defines a ship object that can move in a circular path.
  -OrbitDirection: An enum that defines the direction the ship orbits CW or CCW.

  Coins
  -----
  -CollectibleObject: Interface for an object that the ship can collect. Has modifyState
  method that allows coins to modify the state of the game court.
  -Coin: Abstract class that extends CircleObj and implements CollectibleObject. Provides ability
  to set the angle of the Coin.
  -ScoreCoin: Coin that modifies the state by incrementing the score.
  -InvincibilityCoin: Coin that modifies the state by telling the game court to make the ship
  invincible for 5 seconds.
  -DoubleCoinsCoin: Coin that modifies the state by telling the game court to give double coins
  for 5 seconds.
  -CoinRing: Interface for a coin ring that stores the locations of the coins.
  -HashSetCoinRing: Implementation of a coin ring that uses a hash set.
  -Coins: Has a CoinRing and provides the ability to add random coins to the CoinRing and
  draw the coins.

  Cannon
  ------
  -CannonBall: Defines a cannon ball that moves at an angle relative to the center.
  -Cannon: Defines a cannon and has a list of cannon balls that have been fired.
  Can fire a new cannon ball.

  High Scores:
  -HighScores: Stores the current high scores in a list can has methods to interact with the
  high scores including writing them to a file.
  -ScoreScanner: Reads in the HighScores from highScores.txt



- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    The biggest issue I ran into was inconsistent timing with the java swing timer.
    Because my game is very much based on reaction time having a consistent timer
    is very important for the game play to be good.  The java swing timer will wait for
    java swing to not be busy before calling the tick method, which builds up over time
    throwing off movements.  This also caused the size of the window to affect the speed
    of objects on the screen as a larger window takes longer to repaint.  I ended up using
    the java util Timer instead which times consistently but introduces
    ConcurrentModificationExceptions.  To prevent this I synchronized some methods that
    interact with the CannonBall list and the CoinRing.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    I think in general my design is good.  The functionality is separated relatively well.
    Most importantly in this, the GameCourt mostly just contains the main logic.
    Private state is reasonably well encapsulated.  I can't really think of anything I would want
    to refactor right now.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

  J. (2017, May 08). Implementing Iterable interface in Java to enable for-each loop based iteration.
  Retrieved December 10, 2017, from https://www.javabrahman.com/corejava/implementing-iterable-interface-
  in-java-to-enable-for-each-loop-based-iteration/

  Coin clipart animated gif. (n.d.). Retrieved December 10, 2017, from http://moziru.com/explore/Coin
  %20clipart%20animated%20gif/#gal_post_9026_coin-clipart-animated-gif-4.gif

  How to Use Swing Timers. (n.d.). Retrieved December 10, 2017, from https://docs.oracle.com/javase
  /tutorial/uiswing/misc/timer.html

  Java screen size - How to determine the screen/display size. (n.d.). Retrieved December 10, 2017,
  from https://alvinalexander.com/blog/post/jfc-swing/how-determine-get-screen-size-java-swing-app

  Thread: How to write multiline String in a JLabel. (n.d.). Retrieved December 10, 2017, from
  https://www.java-forums.org/awt-swing/654-how-write-multiline-string-jlabel.html
