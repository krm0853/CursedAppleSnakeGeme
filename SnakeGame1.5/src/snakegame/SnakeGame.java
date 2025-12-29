  
package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(new GamePanel());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

// ================= Game Panel =================
class GamePanel extends JPanel implements ActionListener {

    int SCREEN_WIDTH = 600;
    int SCREEN_HEIGHT = 600;
    int UNIT_SIZE = 25;
    int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    int level = 1;
    int applesToWin = 5;
    int applesCounter = 0;
    boolean gameOver = false;
    final int baseParts = 3;
    int bodyParts = baseParts;
    int appleX;
    int appleY;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    char direction = 'R';
    boolean running = false;
    boolean win = false;

    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(120, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

         
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Level: " + level,
                (SCREEN_WIDTH - metrics.stringWidth("Level: " + level)) / 2,
                g.getFont().getSize());

        if (running) {

             
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

             
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

        } else {
            
            if (win) {
                Win(g);
            } else {
                gameOver(g);
            }
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {

            bodyParts--;    

          
            if (bodyParts <= 0) {
                running = false;
                win = true;    
                timer.stop();
                return;
            }

            newApple();
        }
    }

    public void checkCollisions() {

         
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                gameOver = true;   
            }
        }

          
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH
                || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
            gameOver = true;   
        }

        if (!running) {
            timer.stop();
        }
    }

     
    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2);

        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        g.drawString("Press R to Restart",
                (SCREEN_WIDTH - metrics.stringWidth("Press R to Restart")) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }

     
    public void Win(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Congratulations!",
                (SCREEN_WIDTH - metrics.stringWidth("Congratulations!")) / 2,
                SCREEN_HEIGHT / 2);

        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        g.drawString("Press ENTER for next level",
                (SCREEN_WIDTH - metrics.stringWidth("Press ENTER for next level")) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    
    public void startNextLevel() {

        level++;
        bodyParts = baseParts + level;   
        direction = 'R';

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        gameOver = false;
        win = false;       
        running = true;

        newApple();

        timer = new Timer(120, this);
        timer.start();
    }

     
    public void restartGame() {
        level = 1;
        bodyParts = baseParts;
        direction = 'R';

        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        gameOver = false;
        win = false;
        running = true;

        newApple();

        timer = new Timer(120, this);
        timer.start();
    }

     
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_ENTER:
                    
                    if (win) {
                        startNextLevel();
                    }
                    break;
                case KeyEvent.VK_R:
                  
                    if (gameOver) {
                        restartGame();
                    }
                    break;
            }
        }
    }
}