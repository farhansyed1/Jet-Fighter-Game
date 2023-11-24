# JetFighter

**Description**

JetFighter is a 2D PvP game, where each player controls their own jet fighter. The aim of the game is to eliminate the other player by shooting projectiles at the other jet fighter while dodging various obstacles. 


**How to play** <br>

Player 1 uses the keys *A* and *D* to rotate the black jet, and *S* to fire a bullet. <br>
Player 2 uses the keys *J* and *L* to rotate the grey jet, and *K* to fire a bullet.

Avoid the asteroids and try to eliminate each other!

**Classes** <br>

The program is written in Java and uses the Graphics swing class. 
The *Main* class is used to run the game. *Frame* creates a JFrame. It is extended by the *Panel* class that draws all the game objects using Graphics.
Sprite is a parent class for all moving objects in the game i.e. for *Jet*, *Bullet* and *Asteroid*, 
as they all share several attributes such as speed, angle and position. *Score* keeps track of the score.  