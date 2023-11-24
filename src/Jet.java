import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 * A Jet that is created during the game. Extends the Sprite class.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */
public class Jet extends Sprite {

    boolean rotatingRight;
    boolean rotatingLeft;
    boolean shooting;

    Polygon verticalBox;
    Polygon horizontalBox;

    int[] vertXPoints;
    int[] vertYPoints;

    int[] horXPoints;
    int[] horYPoints;

    int timeAlive;

    Jet(String filename){
        this.setImage(filename);
        imageWidth = imageOfSprite.getWidth(null);
        imageHeight = imageOfSprite.getHeight(null);

        angle = 0;
        speed = 2;

        rotatingLeft = false;
        rotatingRight = false;

        createPolygons();
    }

    /**
     * Makes the Jet move forward by using trigonometric functions on the angle.
     * @param angle given from which direction the jet is facing
     */
    public void moveForward(double angle) {
        this.setPosition(this.xPosition + this.speed * Math.sin(Math.toRadians(angle)), this.yPosition + -1 * this.speed * Math.cos(Math.toRadians(angle)));
        this.timeAlive++;
    }

    /**
     * Draws the moving image of the Jet.
     * Moves the polygon collision box together with the image of the jet.
     * @param g Graphics object
     */
    public void drawJet(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        // Rotate and move the jet
        AffineTransform oldTransformation = g2D.getTransform();
        g2D.translate(xPosition,yPosition);

        AffineTransform transformation = new AffineTransform();
        transformation.rotate(Math.toRadians(this.angle),this.imageWidth /2,this.imageHeight /2);

        // Rotate the polygons
        verticalBox = rotatePolygons(vertXPoints,vertYPoints);
        horizontalBox = rotatePolygons(horXPoints,horYPoints);

        // Move the polygons
        verticalBox.translate((int)this.xPosition,(int)this.yPosition);
        horizontalBox.translate((int)this.xPosition,(int)this.yPosition);

        g2D.drawImage(this.getImage(),transformation,null);
        g2D.setTransform(oldTransformation);    //setting canvas origin to original location
    }

    //Methods for polygons
    private void createPolygons(){
        int vertWidth = 60;     //vert is actually hor - change
        int vertHeight = 20;

        int vertXConst = 70;
        int vertYConst = 50;

        int horWidth = 15;
        int horHeight = 70;

        int horXConst = 47;
        int horYConst = 0;

        vertXPoints = new int[]{vertXConst,vertXConst,vertXConst-vertWidth,vertXConst-vertWidth};
        vertYPoints = new int[]{vertYConst,vertYConst+vertHeight,vertYConst+vertHeight,vertYConst};

        horXPoints = new int[]{horXConst,horXConst,horXConst-horWidth,horXConst-horWidth};
        horYPoints = new int[]{horYConst,horYConst+horHeight,horYConst+horHeight,horYConst};

        verticalBox = new Polygon(vertXPoints,vertYPoints,4);
        horizontalBox = new Polygon(horXPoints,horYPoints,4);
    }

    private Polygon rotatePolygons(int[] xPoints, int[] yPoints){
        double[] currentPoints = new double[]{
                xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2],
                xPoints[3], yPoints[3]};

        double[] newPoints = new double[8];

        AffineTransform polyTransformation = AffineTransform.getRotateInstance(Math.toRadians(this.angle), this.imageWidth /2, this.imageHeight /2);
        polyTransformation.transform(currentPoints, 0, newPoints, 0, 4);

        int[] xTrans = new int[]{(int)newPoints[0], (int)newPoints[2],(int)newPoints[4],(int)newPoints[6]};
        int[] yTrans = new int[]{(int)newPoints[1], (int)newPoints[3],(int)newPoints[5],(int)newPoints[7]};

        Polygon transformedPolygon = new Polygon(xTrans, yTrans, 4);

        return transformedPolygon;
    }

    /**
     * Getter method for the Jet's polygon
     * @return the vertical collision box for jet
     */
    public Polygon getVerticalBox(){
        return this.verticalBox;
    }

    /**
     * Getter method for the Jet's polygon
     * @return the horizontal collision box for jet
     */
    public Polygon getHorizontalBox(){
        return this.horizontalBox;
    }

    /**
     * Checks if a jet collides with another jet
     *
     * @param otherJet the jet that is being checked for
     * @return true if there is a collision, otherwise false
     */
    public boolean checkOverlapWithJet(Jet otherJet){
        Area[] thisJetArea = new Area[]{new Area(this.getVerticalBox()),new Area(this.getHorizontalBox()) };
        Area[] otherJetArea = new Area[]{new Area(otherJet.getVerticalBox()),new Area(otherJet.getHorizontalBox())};

        for (int i = 0; i < thisJetArea.length; i++) {
            for (int j = 0; j < otherJetArea.length; j++) {
                thisJetArea[i].intersect(otherJetArea[j]);
                if(!thisJetArea[i].isEmpty()) {
                    return true;
                }
                thisJetArea = new Area[]{new Area(this.getVerticalBox()),new Area(this.getHorizontalBox()) };
                otherJetArea = new Area[]{new Area(otherJet.getVerticalBox()),new Area(otherJet.getHorizontalBox())};
            }
        }
        return false;
    }

}