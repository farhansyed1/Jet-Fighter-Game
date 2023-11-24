import javax.swing.*;
import java.awt.*;

/**
 * A parent class for all moving objects in the game.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 */
public class Sprite{

    double xPosition;
    double yPosition;

    Image imageOfSprite;
    int imageWidth;
    int imageHeight;

    double speed;
    double angle;

    /**
     *  Sets the image for objects in the game. For instance, the jets or asteroids.
     * @param filename
     */
    public void setImage(String filename){
        this.imageOfSprite = new ImageIcon(filename).getImage();
        imageWidth = imageOfSprite.getWidth(null);
        imageHeight = imageOfSprite.getWidth(null);
    }

    /**
     *  Getter method that returns the image used in the game.
     * @return imageOfSprite
     */
    public Image getImage(){
        return this.imageOfSprite;
    }

    /**
     * To set the position for moving objects in the game
     * @param xPosition, the objects position on the x-axis
     * @param yPosition, the objects position on the y-axis
     */
    public void setPosition(double xPosition, double yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     *  Wraps object from one side of the game to the other when the object come in contact with the game border.
     * @param screenWidth, screen width
     * @param screenHeight, screen height
     */
    public void wrap(double screenWidth, double screenHeight){

        if(this.xPosition + this.imageWidth /2 < 0){
            this.xPosition = screenWidth + this.imageWidth /2;
        }
        if(this.xPosition > screenWidth + this.imageWidth /2){
            this.xPosition = -this.imageWidth /2;
        }
        if(this.yPosition + this.imageHeight /2 < 0){
            this.yPosition = screenHeight + this.imageHeight /2;
        }
        if(this.yPosition > screenHeight + this.imageHeight /2 ){
            this.yPosition = -this.imageHeight /2;
        }
    }

}