package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * A small Swing snake game.
 */
public class App extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int TOTAL_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int STARTING_BODY_PARTS = 5;
    private static final int STARTING_SPEED = 120;
    private static final int MIN_SPEED = 40;
    private static final int SPEED_STEP = 5;

    private final int[] x = new int[TOTAL_UNITS];
    private final int[] y = new int[TOTAL_UNITS];
    private final Random random = new Random();

    private int bodyParts;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private int speed;

    private char direction;
    private boolean running;
    private Timer timer;

    public App() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        resetGame();
    }

    public void startGame() {
        initializeSnake();
        newApple();
        running = true;

        timer = new Timer(speed, this);
        timer.start();
    }

    public void newApple() {
        do {
            appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        } while (isOnSnake(appleX, appleY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(45, Math.min(255, 130 + i * 8), 80));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());

            g.drawString("Score: " + applesEaten,
                    (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void move() {

        int lastIndex = Math.min(bodyParts, TOTAL_UNITS - 1);
        for (int i = lastIndex; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {

            if (bodyParts < TOTAL_UNITS) {
                bodyParts++;
            }
            applesEaten++;

            if (speed > MIN_SPEED) {
                speed -= SPEED_STEP;
                timer.setDelay(speed);
            }

            newApple();
        }
    }

    public void checkCollisions() {

        for (int i = bodyParts - 1; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running && timer != null) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());

        g.drawString("GAME OVER",
                (WIDTH - metrics1.stringWidth("GAME OVER")) / 2,
                HEIGHT / 2);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());

        g.drawString("Final Score: " + applesEaten,
                (WIDTH - metrics2.stringWidth("Final Score: " + applesEaten)) / 2,
                HEIGHT / 2 + 50);

        g.drawString("Press SPACE to Restart",
                (WIDTH - metrics2.stringWidth("Press SPACE to Restart")) / 2,
                HEIGHT / 2 + 100);
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

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;

            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;

            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;

            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;

            case KeyEvent.VK_SPACE:
                if (!running) {
                    resetGame();
                }
                break;
            default:
                break;
        }
    }

    public void resetGame() {
        bodyParts = STARTING_BODY_PARTS;
        applesEaten = 0;
        direction = 'R';
        speed = STARTING_SPEED;
        running = false;

        for (int i = 0; i < TOTAL_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        if (timer != null) {
            timer.stop();
        }

        startGame();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            App gamePanel = new App();

            frame.add(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void initializeSnake() {
        int startX = WIDTH / 2;
        int startY = HEIGHT / 2;

        for (int i = 0; i < bodyParts; i++) {
            x[i] = startX - (i * UNIT_SIZE);
            y[i] = startY;
        }
    }

    private boolean isOnSnake(int positionX, int positionY) {
        for (int i = 0; i < bodyParts; i++) {
            if (x[i] == positionX && y[i] == positionY) {
                return true;
            }
        }
        return false;
    }
}
