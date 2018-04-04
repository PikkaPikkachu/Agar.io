import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Prakriti Bansal
 * Date Created: 3/12/16.
 */
public class Ball {

    public static double x, y;

    public boolean collided;

    public boolean score25, score55;

    public boolean eats;

    public double speedY, speedX;

    public double distance;

    private Color c;

    public int r;

    public int score;

    public Ball()
    {
        Initialize();
    }

    private void Initialize()
    {
        ResetPlayer();
    }

    public void ResetPlayer()
    {
        collided = false;
        eats =false;
        score25=false;
        score55=false;
        score=0;
        x = (Framework.frameWidth/2);
        y = (Framework.frameHeight/2);
        speedY=0;
        speedY=0;
        c= new Color(135, 35, 235);
        r=15;
    }

    public void Update()
    {

        // Calculating speed for moving up or down.
        if(Canvas.keyboardKeyState(KeyEvent.VK_UP)){
            speedY -= 0.1;
        } else if(Canvas.keyboardKeyState(KeyEvent.VK_DOWN)){
            speedY += 0.1;
        }

        // Calculating speed for moving or stopping to the left.
        if(Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) {
            speedX -= 0.1;
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) {
            speedX += 0.1;
        }

        // Make the ball stop.
        if(Canvas.keyboardKeyState(KeyEvent.VK_SPACE)) {
            speedX = 0;
            speedY = 0;
        }

        // Make the ball rebound.
        if(y < 0 || y > Framework.frameHeight ){
            speedY=speedY*-1;
        }
        if(x < 0 || x > Framework.frameWidth) {
            speedX=speedX*-1;
        }

        x +=speedX;
        y +=speedY;
    }

    public boolean Eaten(FoodBalls f){
        distance = Math.sqrt((x-f.x)*(x-f.x)+ (y-f.y)*(y-f.y));
        if(distance < (r+f.d)) {
            r+=1;
            score+=1;
            f.Remove();
            return true;
        }else {
            return false;
        }
    }

    public boolean Win(){
        if (score==100){
            return true;
        }else{
            return false;
        }
    }

    public boolean Collision(EnemyBalls e){
        distance =Math.sqrt(((x-e.x)*(x-e.x))+((y-e.y)*(y-e.y)));
        if ((distance < (r+e.d)) && (r<e.d)) {
            collided = true;
            return true;
        }else {
            collided = false;
            return false;
        }
    }

    public boolean EatEnemy(EnemyBalls e){
        distance =Math.sqrt(((x-e.x)*(x-e.x))+((y-e.y)*(y-e.y)));
        if(distance<(r+e.d) && score>=55 && r> e.d){
            r+=5;
            score+=15;
            e.RemoveEnemy();
            return true;
        } else{
            return false;
        }
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x -=(r/2);
        y -=(r/2);
        g.fillOval(x,y,r,r);
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.setFont(new Font("Calibri", Font.BOLD, 24));
        if(collided) {
            speedY=0;
            speedX=0;
        } else if (score25){
            g2d.setColor(Color.MAGENTA);
            drawCenteredCircle(g2d,(int) x ,(int) y, r*2);
            g2d.setColor(Color.white);
            g2d.drawString("ME", (int) x-15, (int) y+3);
        } else if (score55){
            g2d.setColor(Color.BLUE);
            drawCenteredCircle(g2d,(int) x ,(int) y, r*2);
            g2d.setColor(Color.white);
            g2d.drawString("ME", (int) x-15, (int) y+3);
        }
        else{
            g2d.setColor(c);
            drawCenteredCircle(g2d,(int) x ,(int) y, r*2);
            g2d.setColor(Color.white);
            g2d.drawString("ME", (int) x-15, (int) y+3);
        }
    }
}
