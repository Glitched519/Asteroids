import java.awt.*;
/**
 * Java Retro Asteroids - Lesson 11
 * @author Adrian Balogh
 */
public class VectorSprite
{
    
    double xposition;
    double yposition;
    double xspeed;
    double yspeed;
    double angle;
    Polygon shape, drawShape;
    double ROTATION;
    double THRUST;
    boolean active;
    int counter;
    
    
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
        if (xposition > 900)
        {
            xposition = 0;
        }
        
        if (xposition < 0)
        {
            xposition = 900;
        }
        
        if (yposition > 600)
        {
            yposition = 0;
        }
        
        if (yposition < 0)
        {
            yposition = 600;
        }
    }
    
}
