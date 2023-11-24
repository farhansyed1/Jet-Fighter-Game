import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * A bullet that is created when a player presses the shoot key. Extends the Sprite class.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */
public class Bullet extends Sprite {

    double oldX;
    double oldY;
    double currentX;
    double currentY;

    boolean isBlack;
    int timeAlive;
    int screenWidthAndHeight;
    Polygon bulletBox;

    public Bullet(double angle, double xPosition, double yPosition, boolean isBlack, int screenWidthAndHeight){

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        this.angle = angle;
        this.speed = 3.5;

        this.isBlack = isBlack;
        this.setImage("whiteBall.png");
        this.screenWidthAndHeight = screenWidthAndHeight;
        createPolygon();
    }

    /**
     * Makes the bullet move forward by using trigonometric functions on the angle.
     *
     */
    public void moveForward() {
        this.xPosition += this.speed * cos(Math.toRadians(this.angle-90));
        this.yPosition += this.speed * sin(Math.toRadians(this.angle-90));
        this.wrap(screenWidthAndHeight,screenWidthAndHeight);

        this.timeAlive++;

        setPosition(this.xPosition,this.yPosition);
    }

    /**
     * Draws the moving image of the bullet.
     * Moves the polygon collision box together with the image.
     *
     * @param g Graphics object
     */
    public void drawBullet(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        // Moving the bullet image
        AffineTransform oldTransformation = g2D.getTransform();
        g2D.translate(xPosition+20,yPosition+8);

        AffineTransform transformation = new AffineTransform();
        g2D.drawImage(this.getImage(),transformation,null);
        g2D.setTransform(oldTransformation);                        // resetting the origin

        // Moving the polygon
        currentX = this.xPosition+29;
        currentY = this.yPosition+15;
        bulletBox.translate((int)(currentX-oldX),(int)(currentY-oldY));
        oldX = bulletBox.getBounds2D().getX();
        oldY = bulletBox.getBounds2D().getY();
    }

    private void createPolygon(){
        int xConstant = 50;
        int yConstant = 0;
        int boxWidth = 16;
        int boxHeight = 16;

        int[] xPoints = new int []{xConstant,xConstant,xConstant-boxWidth,xConstant-boxWidth};
        int[] yPoints = new int[]{yConstant,yConstant+boxHeight,yConstant+boxHeight,yConstant};

        bulletBox = new Polygon(xPoints,yPoints,4);
    }

    /**
     * Getter method for the bullet's polygon
     *
     * @return the bullet's polygon
     */
    public Polygon getBox(){
        return this.bulletBox;
    }

    /**
     * Checks if a bullet collides with a jet
     *
     * @param jet the jet that is being checked for
     * @return true if there is a collision, otherwise false
     */
    public boolean checkOverlapWithJet(Jet jet){
        // Converts the polygons to areas
        Area bulletArea = new Area(this.getBox());
        Area[] jetArea = new Area[]{new Area(jet.getVerticalBox()),new Area(jet.getHorizontalBox())};

        for (int i = 0; i < jetArea.length; i++) {
            bulletArea.intersect(jetArea[i]);
            if(!bulletArea.isEmpty()) {
                return true;
            }
            bulletArea = new Area(this.getBox());
            jetArea = new Area[]{new Area(jet.getVerticalBox()),new Area(jet.getHorizontalBox())};
        }
        return false;
    }

    /**
     * Checks if a bullet collides with an asteroid
     *
     * @param asteroid the asteroid that is being checked for
     * @return true if there is a collision, otherwise false
     */
    public boolean checkOverlapWithAsteroid(Asteroid asteroid){
        // Converts the polygons to areas
        Area bulletArea = new Area(this.getBox());
        Area asteroidArea = new Area(asteroid.getBox());

        bulletArea.intersect(asteroidArea);
        return !bulletArea.isEmpty();
    }

}