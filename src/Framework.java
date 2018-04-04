import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net (edited by Prakriti Bansal)
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, HELP, LEVEL1, LEVEL2, PROGRESS, GAMEOVER}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;

    private Random r;

    private BufferedImage background;
    
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {

    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
        try
        {
            background = ImageIO.read(this.getClass().getResource("/resources/images/background.jpg"));

        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            switch (gameState)
            {
                case LEVEL1:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case HELP:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }

    public void button(){

    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case LEVEL1:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Calibri", Font.PLAIN, 18));
                g2d.drawRect(frameWidth/2-117, frameHeight/2, 140, 50);
                g2d.drawRect(frameWidth/2-117, frameHeight/2+100, 140, 50);
                g2d.drawRect(frameWidth/2-117, frameHeight/2+200, 140, 50);
                g2d.drawString("TRY AGAIN", frameWidth/2-95, frameHeight/2+30);
                g2d.drawString("MAIN MENU", frameWidth/2-95, frameHeight/2+130);
                g2d.drawString("EXIT", frameWidth/2-70, frameHeight/2+230);
            break;
            case MAIN_MENU:
                g2d.drawImage(background, 0, 0, frameWidth, frameHeight, null);
                g2d.setFont(new Font("Calibri", Font.PLAIN, 18));
                g2d.drawRect(frameWidth/2-117, frameHeight/2, 140, 50);
                g2d.drawRect(frameWidth/2-117, frameHeight/2+100, 140, 50);
                g2d.drawRect(frameWidth/2-117, frameHeight/2+200, 140, 50);
                g2d.drawString("PLAY", frameWidth/2-70, frameHeight/2+30);
                g2d.drawString("HELP", frameWidth/2-70, frameHeight/2+130);
                g2d.drawString("EXIT", frameWidth/2-70, frameHeight/2+230);
                //g2d.drawString("Use arrow keys and space bar to controle the cell.", frameWidth / 2 - 117, frameHeight / 2);
                //g2d.drawString("Press any key to start the game.", frameWidth / 2 - 100, frameHeight / 2 + 30);
                g2d.drawString("A PRAKRITI BANSAL GAME", 7, frameHeight - 5);
            break;
            case HELP:
                g2d.drawImage(background, 0, 0, frameWidth, frameHeight, null);
                g2d.setFont(new Font("Calibri", Font.PLAIN, 75));
                g2d.drawString("HELP", frameWidth/2-70, frameHeight/2+100);
                g2d.setFont(new Font("Calibri", Font.PLAIN, 18));
                g2d.drawString("Use arrow keys to move the cell. Press space to stop the cell.", 100, frameHeight/2+130);
                g2d.drawString("Avoid getting eaten by bigger cells. Collect as many food balls as possible.", 100, frameHeight/2+155);
                g2d.drawRect(frameWidth/2-117, frameHeight/2+200, 140, 50);
                g2d.drawString("BACK", frameWidth/2-70, frameHeight/2+230);
                g2d.drawString("A PRAKRITI BANSAL GAME", 7, frameHeight - 5);
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.LEVEL1;
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     *
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        /**switch (gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;
        }*/
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
        if (mx > x && mx < x + width){
            if(my > y &&  my < y + height){
                return true;
            } else return false;
        }else return false;
    }

    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        r = new Random();

        if (gameState == GameState.MAIN_MENU) {
            //play button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2, 140, 50)) {
                newGame();
            }
            //help button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2+100, 140, 50)) {
                gameState = GameState.HELP;
            }

            //quit button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2+200, 140, 50)) {
                System.exit(1);
            }
        }

        //back button for help
        if(gameState == GameState.HELP){
            if(mouseOver(mx, my, frameWidth/2-117, frameHeight/2+200, 140, 50)){
                gameState = GameState.MAIN_MENU;
            }
        }

        if(gameState == GameState.GAMEOVER){
            //try again button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2, 140, 50)) {
                newGame();
            }
            //main menu button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2+100, 140, 50)) {
                gameState = GameState.MAIN_MENU;
            }

            //quit button
            if (mouseOver(mx, my, frameWidth/2-117, frameHeight/2+200, 140, 50)) {
                System.exit(1);
            }
        }
    }
}
