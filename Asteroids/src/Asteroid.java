import java.awt.Polygon;
/**
 * Java Retro Asteroids - Lesson 11
 * @author Adrian Balogh
 */
public class Asteroid extends VectorSprite 
{
    int size;
    
    
    public Asteroid()
    {
        size = 3;
        initializeAsteroid();
    }
    
    
    public Asteroid(double x, double y, int s)
    {
        size = s;
        initializeAsteroid();
        xposition = x;
        yposition = y;
    }
    
    
    public void initializeAsteroid()
    {
        shape = new Polygon();
        shape.addPoint(15*size, 6*size);
        shape.addPoint(7*size, 17*size);
        shape.addPoint(-13*size, 8*size);
        shape.addPoint(-11*size, -10*size);
        shape.addPoint(12*size, -16*size);
        
        drawShape = new Polygon();
        drawShape.addPoint(15*size, 6*size);
        drawShape.addPoint(7*size, 17*size);
        drawShape.addPoint(-13*size, 8*size);
        drawShape.addPoint(-11*size, -10*size);
        drawShape.addPoint(12*size, -16*size);
        
        double h, a;
        h = (Math.random() + 0.5) / size;
        a = Math.random()* 2*Math.PI;
        xspeed = Math.cos(a)*h;
        yspeed = Math.sin(a)*h;
        
        h = Math.random() * 400 + 100;
        a = Math.random()* 2*Math.PI;
        xposition = Math.cos(a)*h + 450;
        yposition = Math.sin(a)*h + 300;
        
        ROTATION = (Math.random() / 2 - 0.25) / size;
        
        active = true;
    }
    
    
    public void updatePosition()
    {
        angle += ROTATION;
        super.updatePosition();
    }
}
