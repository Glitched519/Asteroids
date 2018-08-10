import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.*;

public class Asteroids extends Applet implements KeyListener, ActionListener
{
    Image offscreen;
    Graphics offg;
    Spacecraft ship;
    Timer timer;
    ArrayList<Asteroid> asteroidList;
    ArrayList<Bullet> bulletList;
    ArrayList<Debris> explosionList;
    boolean upKey, leftKey, rightKey, downKey, spaceKey, gameStart = false, epilepticMode = false;
    int score, fontSize = 30;
    AudioClip laser, thruster, shipHit, asteroidHit, backgroundMusic;
    Font funFont;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int scrHeight = screenSize.height;
    int scrWidth = screenSize.width;
    float red, green, blue;
    Color flashyLights;
    
    @Override
    public void init()
    {
        this.setSize(scrWidth, scrHeight);
        this.addKeyListener(this);
        this.setFocusable(true);
        ship = new Spacecraft();
        asteroidList = new ArrayList();
        bulletList = new ArrayList();
        explosionList = new ArrayList();
        timer = new Timer(17, this);
        offscreen = createImage(this.getWidth(), this.getHeight());
        offg = offscreen.getGraphics();
        funFont = new Font("Arial", Font.BOLD, fontSize);
        for (int i = 0; i < 6; i++)
        {
            asteroidList.add(new Asteroid());
        }
        resetLevel();
        laser = getAudioClip(getCodeBase(), "laser80.wav");
        thruster = getAudioClip(getCodeBase(), "thruster.wav");
        shipHit = getAudioClip(getCodeBase(), "explode1.wav");
        asteroidHit = getAudioClip(getCodeBase(), "explode0.wav");
        backgroundMusic = getAudioClip(getCodeBase(), "Trap_Music.wav");
        backgroundMusic.loop();       
        
    }
      
    @Override
    public void start()
    {
        timer.start();
    }
    
    @Override
    public void stop()
    {
        timer.stop();
    }
    
    @Override
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
                score -= Math.PI;
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
                    score +=10*bulletList.size()*Math.PI;
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
            x = asteroidList.get(i).xposition - scrWidth/2;
            y = asteroidList.get(i).yposition - scrHeight/2;
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
    
    @Override
    public void paint(Graphics g)
    {   
        Random rdm = new Random();
        red = rdm.nextFloat();
        green = rdm.nextFloat();
        blue = rdm.nextFloat();
        flashyLights = new Color(red, green, blue);
        offg.setFont(funFont);
        if(!gameStart){
            offg.setColor(Color.BLACK);
            timer.stop();
            offg.fillRect(0, 0, scrWidth, scrHeight);
            offg.setColor(Color.MAGENTA);
            offg.drawString("Press any key to play.", scrWidth/3, scrHeight/2);
        }
        if(gameStart){
            offg.setColor(Color.BLACK);
            timer.start();
            offg.fillRect(0, 0, scrWidth, scrHeight);
            if(epilepticMode){
                offg.setColor(flashyLights);
            }else{
                offg.setColor(Color.GREEN);
            }
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

            offg.drawString("Lives: " + ship.lives, 5, fontSize);
            offg.drawString("Score: " + score, scrWidth-6*fontSize, fontSize);

            if (ship.lives == 0)
            {
                offg.drawString("You Died. Your final score is " + score, 380, scrHeight/2);
                ship.active = false;
                epilepticMode = true;
                
            }
            else if (asteroidList.isEmpty())
            {
                offg.drawString("You Win! Your final score is " + score, 400, scrHeight/2);
                offg.drawString("Press Ctrl to play again!", 400, scrHeight/2+40);
                epilepticMode = true;
                
            }
        }
        g.drawImage(offscreen, 0, 0, this);
        repaint();
    }
    
    @Override
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
        
        if (downKey)
        {
            ship.decelerate();
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
    public void resetLevel(){
        ship.reset();
        ship.lives = 3;
        score = 0;
        gameStart = false;
        epilepticMode = false;
        asteroidList.clear();
        for (int i = 0; i < 6; i++)
        {
            asteroidList.add(new Asteroid());
        }
        
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        gameStart = true;
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                rightKey = true;
                break;
            case KeyEvent.VK_LEFT:
                leftKey = true;
                break;
            case KeyEvent.VK_UP:
                upKey = true;
                thruster.loop();
                break;
            case KeyEvent.VK_DOWN:
                downKey = true;
                thruster.loop();
                break;
            case KeyEvent.VK_SPACE:
                spaceKey = true;
                break;
            case KeyEvent.VK_CONTROL:
                resetLevel();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                rightKey = false;
            case KeyEvent.VK_LEFT:
                leftKey = false;
            case KeyEvent.VK_UP:
                upKey = false;
                thruster.stop();
            case KeyEvent.VK_DOWN:
                downKey = false;
                thruster.stop();
            case KeyEvent.VK_SPACE:
                spaceKey = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
