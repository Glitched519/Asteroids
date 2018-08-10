import java.awt.Polygon;

public class Spacecraft extends VectorSprite
{
    int lives;
    Asteroids main = new Asteroids();
     
    public Spacecraft()
    {
        shape = new Polygon();
        shape.addPoint(15, 0);
        shape.addPoint(-10, 10);
        shape.addPoint(0, 0);
        shape.addPoint(-10, -10);
        drawShape = new Polygon();
        drawShape.addPoint(15, 0);
        drawShape.addPoint(-10, 10);
        drawShape.addPoint(0, 0);
        drawShape.addPoint(-10, -10);
        xposition = main.scrHeight/2;
        yposition = main.scrHeight/2;
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
        xposition = main.scrWidth/2;
        yposition = main.scrHeight/2;
        angle = -Math.PI/2;
        active = true;
    }

    public void decelerate() {
        xspeed -= Math.cos(angle)*THRUST;
        yspeed -= Math.sin(angle)*THRUST;
    }
}
