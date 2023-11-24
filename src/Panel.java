import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Creates the canvas, draws objects and controls all game functions.
 *
 * @author Farhan Syed
 * @author Isac Hassle
 * @version 2022-05-19
 *
 */
public class Panel extends JPanel implements ActionListener, KeyListener {

    int screenWidthAndHeight;
    Timer timer;

    Jet blackJet;
    Jet greyJet;

    ArrayList<Bullet> bullets;

    int blackBulletCounter;
    int greyBulletCounter;

    Score score;

    ArrayList<Asteroid> asteroids;

    public Panel(int screenWidthAndHeight) {
        this.screenWidthAndHeight = screenWidthAndHeight;
        this.setPreferredSize(new Dimension(this.screenWidthAndHeight,this.screenWidthAndHeight));

        ImageIcon backgroundImage = new ImageIcon("sky2.jpg");
        JLabel backgroundImageLabel = new JLabel(backgroundImage);
        this.setLayout(null);
        backgroundImageLabel.setBounds(0,0,1000,1000);

        score = new Score(screenWidthAndHeight);

        blackJet = new Jet("blackJet.png");
        greyJet = new Jet("greyJet.png");

        // Setting the starting positions
        blackJet.setPosition(200,500);
        greyJet.setPosition(600,500);

        bullets = new ArrayList();
        asteroids = new ArrayList();
        spawnAsteroids(0);

        timer = new Timer(0, this);
        timer.start();
        this.add(backgroundImageLabel);
    }

    /**
     * Draws the jets, asteroids and bullets.
     *
     * @param g Graphics object
     */
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paint(g);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        blackJet.drawJet(g);
        greyJet.drawJet(g);

        blackJet.wrap(screenWidthAndHeight,screenWidthAndHeight);
        greyJet.wrap(screenWidthAndHeight,screenWidthAndHeight);

        score.draw(g);

        // Removing asteroids after some time
        for (int i = this.asteroids.size() - 1; i >= 0 ; i--) {
            this.asteroids.get(i).moveForward();
            this.asteroids.get(i).drawAsteroid(g);

            if (this.asteroids.get(i).timeAlive > 2000) {
                this.asteroids.remove(i);
            }
        }
        // Removing bullets after some time
        for (int i = this.bullets.size() - 1; i >= 0 ; i--) {
            this.bullets.get(i).moveForward();
            this.bullets.get(i).drawBullet(g);

            if (this.bullets.get(i).timeAlive > 400) {
                if(this.bullets.get(i).isBlack){
                    blackBulletCounter--;
                }
                else{
                    greyBulletCounter--;
                }
                this.bullets.remove(i);
            }
        }
    }

    public Bullet createBullet(double angle, double xPosition, double yPosition, boolean isBlack){
        return new Bullet( angle, xPosition, yPosition, isBlack, screenWidthAndHeight);
    }

    public Asteroid createAsteroid(double angle, double xPosition, double yPosition){
        return new Asteroid( angle, xPosition, yPosition, screenWidthAndHeight);

    }

    /**
     * Rotates and moves jets, checks for collisions, and shoots bullets.
     *
     * @param e ActionEvent object
     */
    public void actionPerformed(ActionEvent e) {
        // Rotation of jets
        if(blackJet.rotatingLeft){
            blackJet.angle -= 2;
        }
        if(blackJet.rotatingRight){
            blackJet.angle += 2;
        }
        if(greyJet.rotatingLeft){
            greyJet.angle -= 2;
        }
        if(greyJet.rotatingRight){
            greyJet.angle += 2;
        }

        // Movement of jets
        blackJet.moveForward(blackJet.angle);
        greyJet.moveForward(greyJet.angle);

        // Check collisions
        this.checkAllOverlaps();

        // Spawn asteroids
        this.spawnAsteroids(1);

        // Shooting max 5 bullets at a time
        if(blackJet.shooting) {
            if(blackBulletCounter < 5){
                Bullet bullet = createBullet(blackJet.angle, blackJet.xPosition, blackJet.yPosition, true);
                bullets.add(bullet);
                blackBulletCounter++;
                blackJet.shooting = false;
            }

        }
        if(greyJet.shooting) {
            if(greyBulletCounter < 5){
                Bullet bullet = createBullet(greyJet.angle, greyJet.xPosition, greyJet.yPosition, false);
                bullets.add(bullet);
                greyBulletCounter++;
                greyJet.shooting = false;
            }
        }
        repaint();
    }

    public void spawnAsteroids(int typeOfSpawn){
        Random random = new Random();
        int randomAngle = random.nextInt(360);
        int randomX = random.nextInt((900-100)+1) + 100;
        int randomY = random.nextInt((900-100)+1) + 100;

        // Prevents spawning 50 pixels next to jets
        if(randomY <= blackJet.yPosition + 50 && randomY >= blackJet.yPosition -50
                || randomY <= greyJet.yPosition + 50 && randomY >= greyJet.yPosition -50) {
            randomY += 50;
        }
        if(randomX <= blackJet.xPosition + 50 && randomX >= blackJet.xPosition -50
                || randomX <= greyJet.xPosition + 50 && randomX >= greyJet.xPosition -50) {
            randomX += 50;
        }

        //Initial spawn
        if(typeOfSpawn == 0){
            for (int i = 1; i <= 4; i++) {
                Asteroid asteroid = createAsteroid(randomAngle*i,randomX*i,randomY*i);
                asteroids.add(asteroid);
            }
        }
        // Spawning asteroid every 400 ticks
        if(blackJet.timeAlive % 400 == 0){
            Asteroid asteroid = createAsteroid(randomAngle,randomX,randomY);
            asteroids.add(asteroid);
        }
    }

    private void checkAllOverlaps(){
        // Jets collide into each other
        if(blackJet.timeAlive > 100){
            if(blackJet.checkOverlapWithJet(greyJet)){
                this.setBackground(Color.orange);
                reset();
            }
        }
        // Jets collide into asteroids
        for (Asteroid asteroid: asteroids) {
            if(asteroid.timeAlive > 100){
                if(asteroid.checkOverlapWithJet(greyJet)){
                    this.setBackground(Color.green);
                    score.blackJetScore++;
                    reset();
                }
                if(asteroid.checkOverlapWithJet(blackJet)){
                    this.setBackground(Color.blue);
                    score.greyJetScore++;
                    reset();
                }
            }

        }
        // Jets hit by bullet
        for (Bullet bullet: bullets) {
            if(bullet.timeAlive > 50){
                if(bullet.checkOverlapWithJet(greyJet)){
                    score.blackJetScore++;
                    reset();
                }
                if(bullet.checkOverlapWithJet(blackJet)){
                    score.greyJetScore++;
                    reset();
                }

            }
            // Bullets hit asteroid
            for (int i = this.asteroids.size() - 1; i >= 0 ; i--) {
                if(bullet.checkOverlapWithAsteroid(asteroids.get(i))){
                    this.asteroids.remove(i);
                }

            }

        }
    }

    private void reset(){
        asteroids.clear();
        bullets.clear();
        timer.stop();

        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        // Freezes screen when a jet dies
        while (elapsedTime < 200) {
            elapsedTime = (new Date()).getTime() - startTime;
        }

        // Resets everything
        blackJet = new Jet("blackJet.png");
        greyJet = new Jet("greyJet.png");

        blackJet.setPosition(200,500);
        greyJet.setPosition(600,500);
        blackJet.timeAlive = 0;
        greyJet.timeAlive = 0;
        blackBulletCounter = 0;
        greyBulletCounter = 0;

        spawnAsteroids(0);

        timer = new Timer(0, this);
        timer.start();
    }

    /**
     * Checks if a key is typed
     * @param e the key that is typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        keyTypedOrPressedHelper(e);
    }

    /**
     * Checks if a key is pressed
     * @param e the key that is pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keyTypedOrPressedHelper(e);
    }

    public void keyTypedOrPressedHelper(KeyEvent e){
        switch (e.getKeyChar()) {
            case 'a':
                blackJet.rotatingRight = false;
                blackJet.rotatingLeft = true;
                break;
            case 'd':
                blackJet.rotatingRight = true;
                blackJet.rotatingLeft = false;
                break;
        }
        switch (e.getKeyChar()){
            case 'j':
                greyJet.rotatingRight = false;
                greyJet.rotatingLeft = true;
                break;
            case 'l':
                greyJet.rotatingRight = true;
                greyJet.rotatingLeft = false;
                break;
        }
        switch (e.getKeyChar()) {
            case 's':
                blackJet.shooting = true;
                break;
        }
        switch (e.getKeyChar()) {
            case 'k':
                greyJet.shooting = true;
                break;
        }
    }

    /**
     * Checks if a key is released
     * @param e the key that is released
     */
    @Override
    public void keyReleased(KeyEvent e){
        switch (e.getKeyChar()) {
            case 'a':
                blackJet.rotatingLeft = false;
                break;
            case 'd':
                blackJet.rotatingRight = false;
                break;
            case 's':
                blackJet.shooting = false;
                break;
        }
        switch (e.getKeyChar()){
            case 'j':
                greyJet.rotatingLeft = false;
                break;
            case 'l':
                greyJet.rotatingRight = false;
                break;
            case 'k':
                greyJet.shooting = false;
                break;
        }
    }

}