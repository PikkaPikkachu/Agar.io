import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
/**
 * Actual game.
 * 
 * @author Prakriti Bansal (framework from www.gametutorial.net)
 */

public class Game {

    private Ball ball;

    private MovingBackground movingBackground;

    private BufferedImage backgroundImg;

    private FoodBalls[] foodBalls= new FoodBalls[50];

    private EnemyBalls[] enemyBalls = new EnemyBalls[3];

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.LEVEL1;
            }
        };
        threadForInitGame.start();
    }

   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        ball = new Ball();
        movingBackground = new MovingBackground();

        for(int i=0; i< foodBalls.length;i++ ) {
            foodBalls[i] = new FoodBalls();
        }

        for(int i=0; i< enemyBalls.length;i++ ) {
            enemyBalls[i] = new EnemyBalls();
        }

    }

    private void LoadContent()
    {
        try
        {
            backgroundImg = ImageIO.read(this.getClass().getResource("/resources/images/back.jpg"));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        movingBackground.Initialize(backgroundImg, -2, 0);
    }
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        ball.ResetPlayer();

        for(int i=0; i< foodBalls.length;i++ ) {
            foodBalls[i].ResetFood();
        }
        for(int i=0; i< enemyBalls.length;i++ ) {
            enemyBalls[i].ResetEnemy();
        }
    }

    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        ball.Update();

        for(int i=0; i< enemyBalls.length;i++ ) {
            enemyBalls[i].Update();

            if (ball.Collision(enemyBalls[i])) {
                Framework.gameState = Framework.GameState.GAMEOVER;
            }

            ball.EatEnemy(enemyBalls[i]);


        }

        for(int i=0; i< foodBalls.length;i++ ) {
           ball.Eaten(foodBalls[i]);
        }

        if(ball.score==25){
            for(int i=0; i<enemyBalls.length; i++){
                enemyBalls[i].dX+=5;
                enemyBalls[i].dY+=5;
                enemyBalls[i].d+=5;
            }
            ball.score+=5;
        }

        if(ball.score>=30 && ball.score<55){
            ball.score25=true;
        }else if (ball.score>=55){
            ball.score25=false;
            ball.score55 = true;
        }

        if(ball.Win()){
            Framework.gameState = Framework.GameState.GAMEOVER;
        }


    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        movingBackground.Draw(g2d);

        g2d.setColor(Color.cyan);
        g2d.setFont(new Font("Calibri", Font.PLAIN, 40));
        g2d.drawString("Score: " + ball.score, 20, 35);

        for(int i=0; i< foodBalls.length;i++ ) {
            foodBalls[i].paint(g2d);
        }

        if (ball.score25 && !ball.score55){
            g2d.drawString("Well Done! You're on level 2! " , 450, 35);
        }
        if (ball.score55){
            g2d.drawString("Well Done! You're on level 3! Eat all the Enemies!" , 250, 35);
        }
        ball.Draw(g2d);

        for(int i=0; i< enemyBalls.length;i++ ) {
            enemyBalls[i].Draw(g2d);
        }
    }

    /**
     * Draw the game over screen.
     *
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
        Draw(g2d, mousePosition);
        g2d.setFont(new Font("Calibri", Font.BOLD, 48));

        //g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 300, Framework.frameHeight / 3 + 70);
        g2d.setColor(Color.white);
        g2d.drawString("Your score is "+ball.score, Framework.frameWidth / 2 - 250, Framework.frameHeight / 3-70);

        for(int i=0; i< enemyBalls.length;i++ ) {

            if (ball.Collision(enemyBalls[i])) {
                g2d.setColor(Color.red);
                g2d.drawString("Ooops, one of the big cells ate you!", Framework.frameWidth / 2 - 400, Framework.frameHeight / 3);
            }
        }
        if(ball.Win())  {
            g2d.setColor(Color.white);
            g2d.drawString("CONGRATULATIONS! YOU HAVE EATEN ALL THE CELLS!", 150, Framework.frameHeight / 3);
        }
    }
}
