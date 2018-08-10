import java.awt.Polygon;

public class Bullet extends VectorSprite
{
    public Bullet(double x, double y, double a)
    {
        shape = new Polygon();
        shape.addPoint(0, 2);
        shape.addPoint(1, 0);
        shape.addPoint(0, -1);
        shape.addPoint(-1, 0);
        
        drawShape = new Polygon();
        drawShape.addPoint(0, 2);
        drawShape.addPoint(1, 0);
        drawShape.addPoint(0, -1);
        drawShape.addPoint(-1, 0);
        
        xposition = x;
        yposition = y;
        angle = a;
        THRUST = 10;
        
        xspeed = Math.cos(angle+Math.random()-0.5)*THRUST;
        yspeed = Math.sin(angle+Math.random()-0.5)*THRUST;
        
        active = true;
    }
    
}
