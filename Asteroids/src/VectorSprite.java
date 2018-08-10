import java.awt.*;

public class VectorSprite
{
    Asteroids main = new Asteroids();
    double xposition, yposition, xspeed, yspeed, angle, ROTATION, THRUST;
    boolean active;
    int counter;
    Polygon shape, drawShape;
    
    public void paint(Graphics g)
    {
        g.drawPolygon(drawShape);
    }
    
    
    public void updatePosition()
    {
        counter++;
        
        xposition += xspeed;
        yposition += yspeed;
        
        wraparound();
        
        int x,y;
        
        for (int i = 0; i < shape.npoints; i++)
        {
            x = (int)Math.round(shape.xpoints[i]*Math.cos(angle) - shape.ypoints[i]*Math.sin(angle));
            y = (int)Math.round(shape.xpoints[i]*Math.sin(angle) + shape.ypoints[i]*Math.cos(angle));
            drawShape.xpoints[i] = x;
            drawShape.ypoints[i] = y;
        }
        
        drawShape.invalidate();
        drawShape.translate((int)Math.round(xposition), (int)Math.round(yposition));
    }
    
    
    private void wraparound()
    {
        if (xposition > main.scrWidth)
        {
            xposition = 0;
        }
        
        if (xposition < 0)
        {
            xposition = main.scrWidth;
        }
        
        if (yposition > main.scrHeight)
        {
            yposition = 0;
        }
        
        if (yposition < 0)
        {
            yposition = main.scrHeight;
        }
    }
    
}
