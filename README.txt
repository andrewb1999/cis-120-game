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

  1.    Collections:  The circular path of the ship is divided up into spaces that can
  either contain a score coin or a power up (invincibility or double coins for a few
  seconds). Each space will be represented by an object that includes its position
  on the circle stored in a Set.  When these spaces are contacted by the ship a
  method is called that says what to do when that space is contacted.
  The collectible object is then deleted from the Set and removed from the screen.
  Overtime new coins and power ups will be added to the Set and drawn on screen
  in pseudo-random locations. The game will not add more items to the Set than
  there are positions on the circle; positions will not overlap.

  2.    I/O:  The game uses file I/O to save the top 5 high scores of the game.  The score
  is calculated by the number of coins collected before the player is hit by a cannonball.
  If the player’s new score is in the top 5 scores they will be given an option to enter
  their name to save the score.  The high scores are displayed next to the game and updated
  when a new high score is achieved.  If two players share an identical #5 place score, then
  the most recent player will be the one to stay on the list.

  3. Inheritance/Subtyping for Dynamic Dispatch:  Each coin/power-up extends the Coin abstract
  class.  The coin abstract class extends CircleObj and implements CollectibleObject.
  CollectibleObject has a method called modifyState.  Each type of coin overrides this method.
  The method is then called when the Coin is collected by the player.

  4. Testable Component:


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.


