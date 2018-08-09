import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.ArrayList;
/**
 * Java Retro Asteroids - Lesson 11
 * @author Adrian Balogh
 */
public class Asteroids extends Applet implements KeyListener, ActionListener
{
    Image offscreen;
    Graphics offg;
    Spacecraft ship;
    ArrayList<Asteroid> asteroidList;
    ArrayList<Bullet> bulletList;
    ArrayList<Debris> explosionList;
    Timer timer;
    boolean upKey, leftKey, rightKey, spaceKey;
    int score;
    AudioClip laser, thruster, shipHit, asteroidHit;
    
    
    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init()
    {
        this.setSize(900, 600);
        this.addKeyListener(this);
        ship = new Spacecraft();
        asteroidList = new ArrayList();
        bulletList = new ArrayList();
        explosionList = new ArrayList();
        timer = new Timer(20, this);
        offscreen = createImage(this.getWidth(), this.getHeight());
        offg = offscreen.getGraphics();
        
        for (int i = 0; i < 6; i++)
        {
            asteroidList.add(new Asteroid());
        }
        
        laser = getAudioClip(getCodeBase(), "laser80.wav");
        thruster = getAudioClip(getCodeBase(), "thruster.wav");
        shipHit = getAudioClip(getCodeBase(), "explode1.wav");
        asteroidHit = getAudioClip(getCodeBase(), "explode0.wav");
    }
    
    
    public void start()
    {
        timer.start();
    }
    
    
    public void stop()
    {
        timer.stop();
    }
    
    
    public void actionPerformed(ActionEvent e)
    {
        respawnShip();
        keyCheck();
        ship.updatePosition();
     
        checkAsteroidDestruction();
        for (int i = 0; i < asteroidList.size(); i++)
        {
            asteroidList.get(i).updatePosition();
        }
        
        for (int i = 0; i < bulletList.size(); i++)
        {
            bulletList.get(i).updatePosition();
            if (bulletList.get(i).counter == 50 || bulletList.get(i).active == false)
            {
                bulletList.remove(i);
            }
        }
        
        for (int i = 0; i < explosionList.size(); i++)
        {
            explosionList.get(i).updatePosition();
            if (explosionList.get(i).counter == 20)
            {
                explosionList.remove(i);
            }
        }
        
        checkCollisions();
    }
    
    
    public void checkAsteroidDestruction()
    {
        for (int i = 0; i < asteroidList.size(); i++)
        {
            if (asteroidList.get(i).active == false)
            {
                if (asteroidList.get(i).size > 1)
                {
                    asteroidList.add(new Asteroid(asteroidList.get(i).xposition + 5,
                                                  asteroidList.get(i).yposition - 5,
                                                  asteroidList.get(i).size - 1));
                    asteroidList.add(new Asteroid(asteroidList.get(i).xposition - 5,
                                                  asteroidList.get(i).yposition + 5,
                                                  asteroidList.get(i).size - 1));
                }
                asteroidList.remove(i);
            }
        }
    }
    
    
    public boolean collision(VectorSprite thing1, VectorSprite thing2)
    {
        int x, y;
        
        for (int i = 0; i < thing1.drawShape.npoints; i++)
        {
            x = thing1.drawShape.xpoints[i];
            y = thing1.drawShape.ypoints[i];

            if ( thing2.drawShape.contains(x, y) )
            {
                return true;
            }
        }

        for (int i = 0; i < thing2.drawShape.npoints; i++)
        {
            x = thing2.drawShape.xpoints[i];
            y = thing2.drawShape.ypoints[i];

            if ( thing1.drawShape.contains(x, y) )
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public void checkCollisions()
    {
        for (int i = 0; i < asteroidList.size(); i++)
        {
            double rnd;
            if ( collision(ship, asteroidList.get(i)) && ship.active )
            {
                ship.hit();
                score -= 20;
                rnd = Math.random() * 5 + 5;
                for (int k = 0; k < rnd; k++)
                {
                    explosionList.add(new Debris(ship.xposition, ship.yposition));
                }
                shipHit.play();
            }
            
            for (int j = 0; j < bulletList.size(); j++)
            {
                if ( collision(bulletList.get(j), asteroidList.get(i)) )
                {
                    bulletList.get(j).active = false;
                    asteroidList.get(i).active = false;
                    score += 10;
                    rnd = Math.random() * 5 + 5;
                    for (int k = 0; k < rnd; k++)
                    {
                        explosionList.add(new Debris(asteroidList.get(i).xposition,
                                                    asteroidList.get(i).yposition));
                    }
                    asteroidHit.play();
                }
            }
        }
    }
    
    
    public void respawnShip()
    {
        if ( ship.active == false && ship.counter > 50 && isRespawnSafe() 
                && ship.lives > 0 )
        {
            ship.reset();
        }
    }
    
    
    public boolean isRespawnSafe()
    {
        double x, y, h;
        
        for (int i = 0; i < asteroidList.size(); i++)
        {
            x = asteroidList.get(i).xposition - 450;
            y = asteroidList.get(i).yposition - 300;
            h = Math.sqrt(x*x + y*y);
            
            if (h < 100)
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    public void fireBullet()
    {
        if ( ship.counter > 5 && ship.active )
        {
            bulletList.add(new Bullet(ship.drawShape.xpoints[0],
                                        ship.drawShape.ypoints[0],
                                        ship.angle));
            ship.counter = 0;
            laser.play();
        }
    }
    
    
    public void paint(Graphics g)
    {
        offg.setColor(Color.BLACK);
        offg.fillRect(0, 0, 900, 600);
        offg.setColor(Color.GREEN);
        
        if ( ship.active )
        {
            ship.paint(offg);
        }
        
        for (int i = 0; i < asteroidList.size(); i++)
        {
            asteroidList.get(i).paint(offg);
        }
        
        for (int i = 0; i < bulletList.size(); i++)
        {
            bulletList.get(i).paint(offg);
        }
        
        for (int i = 0; i < explosionList.size(); i++)
        {
            explosionList.get(i).paint(offg);
        }
        
        offg.drawString("Lives: " + ship.lives, 5, 15);
        offg.drawString("Score: " + score, 830, 15);
        
        if (ship.lives == 0)
        {
            offg.drawString("Game Over - You Lose!  Good day, sir!", 380, 300);
        }
        else if (asteroidList.isEmpty())
        {
            offg.drawString("Game Over - You Win!", 400, 300);
        }
        
        g.drawImage(offscreen, 0, 0, this);
        repaint();
    }
    
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    
    public void keyCheck()
    {
        if (upKey)
        {
            ship.accelerate();
        }
        
        if (leftKey)
        {
            ship.rotateLeft();
        }
        
        if (rightKey)
        {
            ship.rotateRight();
        }
        
        if (spaceKey)
        {
           fireBullet();
        }
    }
    
    
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            rightKey = true;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            leftKey = true;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            upKey = true;
            thruster.loop();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            spaceKey = true;
        }
    }
    
    
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            rightKey = false;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            leftKey = false;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            upKey = false;
            thruster.stop();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            spaceKey = false;
        }
    }
    
    
    public void keyTyped(KeyEvent e) {}
}
