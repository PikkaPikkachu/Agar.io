import java.awt.*;
import java.util.Random;
import java.lang.Math;

/**
 * Created by prakriti.bansal on 3/20/16.
 */
public class EnemyBalls {
    public int x, y;

    public int dY, dX;

    public int d;

    private int r, g, b;

    public Color c;

    private Random num;


    public EnemyBalls()
    {
        Initialize();
    }

    private void Initialize()
    {
        num = new Random();
        ResetEnemy();

    }

    public void ResetEnemy()
    {
        d = num.nextInt(60-40)+40;
        x = num.nextInt(Framework.frameWidth);
        y = num.nextInt(Framework.frameHeight);
        dX=num.nextInt(2)-4;
        dY=num.nextInt(2)-4;

        //randomly colored balls!
        r = num.nextInt(255)+1;
        g = num.nextInt(255)+1;
        b = num.nextInt(255)+1;
        c = new Color(r, g, b);
    }

    public void Update() {

        if (y < 0 || y > Framework.frameHeight) {
            dY = dY * -1;
        }
        if (x < 0 || x > Framework.frameWidth) {
            dX = dX * -1;
        }
        x += dX;
        y += dY;
    }

    public void RemoveEnemy(){
        dY=0;
        x=-1000;
        dX=0;
    }

    public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
        x -= (r/2);
        y -= (r/2);
        g.fillOval(x,y,r,r);
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(c);
        drawCenteredCircle(g2d, x, y, d*2);
        g2d.setColor(Color.white);
        g2d.drawString("ENEMY", (int) x-33, (int) y+10);
    }
}
