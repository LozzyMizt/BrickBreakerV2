package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private boolean nextLevel = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int barrier = 200;

    private int ballposX = 120;
    private int ballposY = 350;

    Random random = new Random();

    int n = random.nextInt(5-(-5)+1) +(-5);
    private int ballXdir = n;
    private int ballYdir = -2;

    private MapGenerator map;


    public Gameplay(){
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D) g);


        // borders
        g.setColor(Color.yellow);
        g.fillRect(0,0, 3, 592);
        g.fillRect(0,0, 692, 3);
        g.fillRect(681,0, 3, 592);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (nextLevel) {
            // Level Two Border
            g.fillRect(barrier, 300, 100, 8);
        }


        if(totalBricks <= 0){
            play = false;
            nextLevel = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 290, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press 'r' to restart or Enter to play next level", 170, 350);
        }
        if(ballposY > 570) {
            play = false;
            nextLevel = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " +score, 220, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press 'r' to restart", 270, 350);

        }
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play){
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(
                    new Rectangle(playerX, 550, 100, 8))
            ){
                ballYdir = -ballYdir;
            }
           A: for(int i = 0; i<map.map.length; i++){
                for(int j = 0; j<map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j* map.brickWidth + 80;
                        int brickY = i* map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        // Ball making contact with brick
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;

            // Ball bounce off area
            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if(ballposX > 660) {
                ballXdir = -ballXdir;
            }
        }

        if(nextLevel) {
            if(play){
                if(new Rectangle(ballposX, ballposY, 20, 20).intersects(
                        new Rectangle(playerX, 550, 100, 8))
            || new Rectangle(ballposX, ballposY, 20, 20).intersects(
                    new Rectangle(barrier, 300, 100, 8))
                ){
                    ballYdir = -ballYdir;
                }
                A: for(int i = 0; i<map.map.length; i++){
                    for(int j = 0; j<map.map[0].length; j++){
                        if(map.map[i][j] > 0){
                            int brickX = j* map.brickWidth + 80;
                            int brickY = i* map.brickHeight + 50;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                            Rectangle brickRect = rect;

                            // Ball making contact with brick
                            if(ballRect.intersects(brickRect)){
                                map.setBrickValue(0, i, j);
                                totalBricks--;
                                score += 5;

                                if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                    ballXdir = -ballXdir;
                                } else {
                                    ballYdir = -ballYdir;
                                }

                                break A;
                            }
                        }
                    }
                }
                ballposX += ballXdir;
                ballposY += ballYdir;

                // Ball bounce off area
                if(ballposX < 0) {
                    ballXdir = -ballXdir;
                }
                if(ballposY < 0) {
                    ballYdir = -ballYdir;
                }
                if(ballposX > 660) {
                    ballXdir = -ballXdir;
                }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Move right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >=600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        // Move left
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        // Restart game when 'r' key is pressed
        if(e.getKeyCode() == KeyEvent.VK_R){
            int n = random.nextInt(5-(-5)+1) +(-5);
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = n;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }

        // Play next level when 'ENTER' key is pressed
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            int n = random.nextInt(5-(-5)+1) +(-5);
            if(!play) {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = n;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                nextLevel = true;
                repaint();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void moveRight(){
        play = true;
        playerX+=20;
    }
    public void moveLeft(){
        play = true;
        playerX-=20;
    }

}
