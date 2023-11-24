import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * An asteroid that is created during the game. Extends the Sprite class.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */
public class Asteroid extends Sprite {

    double oldX;
    double oldY;
    double currentX;
    double currentY;

    Polygon asteroidBox;
    int timeAlive;
    int screenWidthAndHeight;

    public Asteroid(double angle, double xPosition, double yPosition, int screenWidthAndHeight){

        this.xPosition = xPosition;
        this.yPosition = yPosition;

        this.angle = angle;
        this.speed = 2;
        
        this.setImage("asteroid1.png");
        this.screenWidthAndHeight = screenWidthAndHeight;
        createPolygon();
    }

    /**
     * Makes the asteroid move forward by using trigonometric functions on the angle.
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
     * Draws the moving image of the asteroid.
     * Moves the polygon collision box together with the image.
     *
     * @param g Graphics object
     */
    public void drawAsteroid(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        // Moving asteroid image
        AffineTransform oldTransformation = g2D.getTransform();
        g2D.translate(xPosition,yPosition);
        AffineTransform transformation = new AffineTransform();
        g2D.drawImage(this.getImage(),transformation,null);
        g2D.setTransform(oldTransformation);

        // Moving the polygon
        currentX = this.xPosition+37;
        currentY = this.yPosition+18;
        asteroidBox.translate((int)(currentX-oldX),(int)(currentY-oldY));
        oldX = asteroidBox.getBounds2D().getX();
        oldY = asteroidBox.getBounds2D().getY();
    }

    private void createPolygon(){
        int xConstant = 4;
        int yConstant = 0;
        int boxWidth = 47;
        int boxHeight = 45;

        int[] xPoints = new int []{xConstant,xConstant,xConstant-boxWidth,xConstant-boxWidth};
        int[] yPoints = new int[]{yConstant,yConstant+boxHeight,yConstant+boxHeight,yConstant};

        asteroidBox = new Polygon(xPoints,yPoints,4);
    }

    /**
     * Getter method for the asteroid's polygon
     *
     * @return the asteroid's polygon
     */
    public Polygon getBox(){
        return this.asteroidBox;
    }

    /**
     * Checks if an asteroid collides with a jet
     *
     * @param jet the jet that is being checked for
     * @return true if there is a collision, otherwise false
     */
    public boolean checkOverlapWithJet(Jet jet){
        // Converts the polygons to areas
        Area asteroidArea = new Area(this.getBox());
        Area[] jetArea = new Area[]{new Area(jet.getVerticalBox()),new Area(jet.getHorizontalBox())};

        for (int i = 0; i < jetArea.length; i++) {
            asteroidArea.intersect(jetArea[i]);
            if(!asteroidArea.isEmpty()) {
                return true;
            }
            asteroidArea = new Area(this.getBox());
            jetArea = new Area[]{new Area(jet.getVerticalBox()),new Area(jet.getHorizontalBox())};
        }
        return false;
    }

}
