import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame1 extends JPanel implements ActionListener {
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 140;
    private Node head; 
    private int foodEaten;
    private Node food; 
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    
    class Node {
        int x;
        int y;
        Node next;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.next = null;
        }
    }

    public SnakeGame1() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        // Initialize the snake's body with 3 segments
        head = new Node(UNIT_SIZE, UNIT_SIZE);
        Node second = new Node(UNIT_SIZE - UNIT_SIZE, UNIT_SIZE);
        Node third = new Node(UNIT_SIZE - 2 * UNIT_SIZE, UNIT_SIZE);
        head.next = second;
        second.next = third;

        foodEaten = 0;
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
           
            g.setColor(Color.red);
            g.fillOval(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            
            Node current = head;
            while (current != null) {
                if (current == head) {
                    g.setColor(Color.green); 
                } else {
                    g.setColor(new Color(45, 180, 0)); 
                }
                g.fillRect(current.x, current.y, UNIT_SIZE, UNIT_SIZE);
                current = current.next;
            }

        
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
      
        food = new Node(random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE,
                        random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE);
    }

    public void move() {
        
        Node current = head;
        int prevX = current.x;
        int prevY = current.y;

        switch (direction) {
            case 'U': current.y -= UNIT_SIZE; break;
            case 'D': current.y += UNIT_SIZE; break;
            case 'L': current.x -= UNIT_SIZE; break;
            case 'R': current.x += UNIT_SIZE; break;
        }

        
        while (current.next != null) {
            current = current.next;
            int tempX = current.x;
            int tempY = current.y;
            current.x = prevX;
            current.y = prevY;
            prevX = tempX;
            prevY = tempY;
        }
    }

    public void checkFood() {
        
        if (head.x == food.x && head.y == food.y) {
            foodEaten++;
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node(current.x, current.y); 
            newFood();
        }
    }

    public void checkCollisions() {
        Node current = head.next;
        while (current != null) {
            if (head.x == current.x && head.y == current.y) {
                running = false;
            }
            current = current.next;
        }

       
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + foodEaten, (WIDTH - metrics2.stringWidth("Score: " + foodEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
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
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.setTitle("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
