import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener
{
    private class Tile 
    {
        int x;
        int y;
        Tile(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
    }
    int tileSize = 25;
    int boardWidth;
    int boardHeight;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight)
    {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);

        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g)
    {
        // for(int i = 0; i < boardWidth / tileSize; i++)
        // {
        //     g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
        //     g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        // }

        g.setColor(Color.red);
        g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);


        g.setColor(Color.yellow);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);


        for(int i = 0; i < snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        if (gameOver)
        {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = g.getFontMetrics();
            String gameOverText = "Game Over";
            String scoreText = "Score : " + snakeBody.size();
            int xGameOver = (boardWidth - metrics.stringWidth(gameOverText)) / 2;
            int yGameOver = ((boardHeight - metrics.getHeight()) / 2) + metrics.getAscent();
            int xScore = (boardWidth - metrics.stringWidth(scoreText)) / 2;
            int yScore = yGameOver + metrics.getHeight() + 10; // Adding 10 for spacing between lines
            g.setColor(Color.red);
            g.drawString(gameOverText, xGameOver, yGameOver);
            g.drawString(scoreText, xScore, yScore);
        }
        else
        {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Score : " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }
    public void placeFood() 
    {
        boolean onSnake;
        do 
        {
            onSnake = false;
            food.x = random.nextInt(boardWidth / tileSize);
            food.y = random.nextInt(boardHeight / tileSize);
            for (Tile snakePart : snakeBody) 
            {
                if (collision(snakePart, food)) 
                {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake);
    }
    

    public boolean collision(Tile tile1, Tile tile2)
    {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move()
    {
        if(collision(snakeHead, food))
        {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i = snakeBody.size() - 1; i >= 0; i--)
        {
            Tile snakePart = snakeBody.get(i);
            if(i == 0)
            {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else
            {
                Tile previousSnakePart = snakeBody.get(i - 1);
                snakePart.x = previousSnakePart.x;
                snakePart.y = previousSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i = 0; i < snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart))
            {
                gameOver = true;
            }
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) 
        {
            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();
        repaint();
        if(gameOver)
        {
            gameLoop.stop(); 
        }
    }

  

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1)
        {
            velocityX = 0;
            velocityY = -1;
        }    
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1)
        {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1)
        {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1)
        {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
