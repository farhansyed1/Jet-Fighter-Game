import java.awt.*;

/**
 * The scoring system for the game.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */

public class Score {

    int width;
    int height;
    int blackJetScore;
    int greyJetScore;

    Score(int screenWidthAndHeight){
        this.width = screenWidthAndHeight;
        this.height = screenWidthAndHeight;
    }

    /**
     * Draws the current score on the canvas
     * @param g Graphics object
     */
    public void draw(Graphics g){
        g.setFont(new Font("Consolas",Font.PLAIN,60));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(blackJetScore), (width/2)-85,50);
        g.setColor(Color.GRAY);
        g.drawString(String.valueOf(greyJetScore), (height/2)+25,50);
    }

}