import java.awt.*;
import java.util.Random;

/**
 * Author: Prakriti Bansal
 * Date Created: 3/15/16.
 */
public class FoodBalls {
    public int x, y;

    public int d;

    private int r, g, b;

    public Color c;

    private Random num;


    public FoodBalls()
    {
        Initialize();
    }

    private void Initialize()
    {
        num = new Random();
        ResetFood();
    }

    public void ResetFood()
    {
        d = 15;
        x = num.nextInt(Framework.frameWidth);
        y = num.nextInt(Framework.frameHeight);

        //randomly colored balls!
        r = num.nextInt(255)+1;
        g = num.nextInt(255)+1;
        b = num.nextInt(255)+1;
        c = new Color(r, g, b);
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x -=(r/2);
        y -=(r/2);
        g.fillOval(x,y,r,r);
    }

    public void Remove(){
        r=0;
        x=-100;
    }

    public void paint(Graphics2D g2d)
    {
        g2d.setColor(c);
        drawCenteredCircle(g2d, x, y, d);
    }
}
