import java.awt.Polygon;

public final class Asteroid extends VectorSprite 
{
    int size;
    Asteroids main = new Asteroids();
        
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
        xposition = Math.cos(a)*h + main.scrWidth/2;
        yposition = Math.sin(a)*h + main.scrHeight/2;
        
        ROTATION = (Math.random() / 2 - 0.25) / size;
        
        active = true;
    }
    
    @Override
    public void updatePosition()
    {
        angle += ROTATION;
        super.updatePosition();
    }
}
