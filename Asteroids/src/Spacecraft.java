import java.awt.Polygon;
/**
 * Java Retro Asteroids - Lesson 11
 * @author Adrian Balogh
 */
public class Spacecraft extends VectorSprite
{
    int lives;
    
    
    public Spacecraft()
    {
        shape = new Polygon();
        shape.addPoint(15, 0);
        shape.addPoint(-10, 10);
        shape.addPoint(-10, -10);
        drawShape = new Polygon();
        drawShape.addPoint(15, 0);
        drawShape.addPoint(-10, 10);
        drawShape.addPoint(-10, -10);
        xposition = 450;
        yposition = 300;
        ROTATION = 0.15;
        THRUST = 0.5;
        active = true;
        angle = -Math.PI/2;
        lives = 3;
    }
    
    
    public void accelerate()
    {
        xspeed += Math.cos(angle)*THRUST;
        yspeed += Math.sin(angle)*THRUST;
    }
    
    
    public void rotateLeft()
    {
        angle -= ROTATION;
    }
    
    
    public void rotateRight()
    {
        angle += ROTATION;
    }
    
    
    public void hit()
    {
        lives--;
        active = false;
        counter = 0;
    }
    
    
    public void reset()
    {
        xspeed = 0;
        yspeed = 0;
        xposition = 450;
        yposition = 300;
        angle = -Math.PI/2;
        active = true;
    }
}
