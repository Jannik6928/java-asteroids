
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Asteroids extends JPanel implements KeyListener{
	
	static double targetFPS = 60;
	
	public void keyTyped(KeyEvent e) {
		
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) wPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_S) sPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_A) aPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_D) dPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = true;
    }
    public void keyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_W) wPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_S) sPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_A) aPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_D) dPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = false;
    }

    static boolean wPressed = false;
    static boolean sPressed = false;
    static boolean aPressed = false;
    static boolean dPressed = false;
    static boolean spacePressed = false;
    
    

    
    public static Vector2 bounds = new Vector2(1000, 1000);
    public static void wrapPosition(Object obj) {
    	if (obj.pos.x > bounds.x) obj.pos.x = 0;
    	if (obj.pos.x < 0) obj.pos.x = bounds.x;
    	if (obj.pos.y > bounds.y) obj.pos.y = 0;
    	if (obj.pos.y < 0) obj.pos.y = bounds.y;
    }
	
    static int score = 0;
    static double rockFreq = 0.3;
    
    static Ship ship = new Ship();    
    static ArrayList<Rock> rocks = new ArrayList<Rock>();
    public static void spawnRocks(double dt) {
    	double threat = 0;
    	for (Rock rock : rocks) {
    		threat += rock.getThreat();
    	}
    	if (threat < 27 && Math.random() < rockFreq * dt) {
    		for (int i = 0; i < 1; i++) {
    			Rock rock = new Rock();
    			rock.randomizeShape();
    			rock.randomizeMovement();
    			rock.pos = new Vector2(100,100);
    			if (Math.random() > 0.5) {
    				if (Math.random() > 0.5) {
        				rock.pos = new Vector2(0, Math.random() * bounds.y);
        			}
        			else {
        				rock.pos = new Vector2(bounds.x, Math.random() * bounds.y);
        			}
    			}
    			else {
    				if (Math.random() > 0.5) {
        				rock.pos = new Vector2(Math.random() * bounds.x, 0);
        			}
        			else {
        				rock.pos = new Vector2(Math.random() * bounds.x, bounds.y);
        			}
    			}
    			
    	        rocks.add(rock);
    		}
    		
    	}
    }
    
    static ArrayList<Shrapnel> shrapnel = new ArrayList<Shrapnel>();
    public static void spawnShrapnel(int count, Vector2 pos) {
    	for (int i = 0; i < count; i++) {
    		Shrapnel shrap = new Shrapnel(3, 4, 1, 2, 20, 70, -Math.PI * 2, Math.PI * 2, 0.5, 1.5);
    		shrap.pos = pos;
    		shrapnel.add(shrap);
    	}
    }
    
    
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(new Color(0,0,0));
		g2d.fillRect(0, 0, (int)bounds.x, (int)bounds.y);
		
		g2d.setColor(new Color(255,255,255));
		g2d.setStroke(new BasicStroke(2));
		
		AffineTransform shipTransform = ship.getAffineTransform();
		g2d.setTransform(shipTransform);
		
		if (ship.isImmune()) {
			if (Math.random() < 0.5) g2d.draw(ship.getShape());
		}
		else {
			g2d.draw(ship.getShape());
		}
		
		if (wPressed && Math.random() < 0.8) g2d.draw(ship.getExhaustShape());
		
		for (Bullet bullet : ship.getBullets()) {
			AffineTransform bulletTransform = bullet.getAffineTransform();
			g2d.setTransform(bulletTransform);
			g2d.draw(bullet.getShape());
		}
		
		for (Rock rock : rocks) {
			AffineTransform rockTransform = rock.getAffineTransform();
			g2d.setTransform(rockTransform);
			g2d.draw(rock.getShape());
		}
		
		for (Shrapnel s : shrapnel) {
			AffineTransform shrapnelTransform = s.getAffineTransform();
			g2d.setTransform(shrapnelTransform);
			g2d.draw(s.getShape());
		}
		
		g2d.setTransform(new AffineTransform());
		g2d.setFont(new Font("Monospaced", Font.PLAIN, 30)); 
		g2d.drawString("Score: " + Integer.toString(score), 50, (int)bounds.y - 100);
		g2d.drawString("Lives: " + ship.getLives(), (int)bounds.x - 200, (int)bounds.y - 100);
		
		if (ship.getLives() <= 0) {
			g2d.setTransform(new AffineTransform());
			g2d.setFont(new Font("Monospaced", Font.PLAIN, 60)); 
			g2d.drawString("Game Over!", (int)bounds.x / 2 - 180, (int)bounds.y / 2 - 50);
		}
    }
    
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("AP CSAsteroids!");

        frame.setSize((int)bounds.x, (int)bounds.y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);   
        
        Asteroids asteroids = new Asteroids();
        frame.addKeyListener(asteroids);
        
        frame.getContentPane().add(asteroids);

        ship.setPosition(bounds.scale(0.5));
        
        long lastTime = System.nanoTime();
        while(true) {
        	
        	long newTime = System.nanoTime();
			double dt = (double)(newTime - lastTime) / 1000000000;
			lastTime = newTime;
        	
        	ship.updateAcceleration(wPressed);
        	ship.updateAngularVelocity(aPressed, dPressed, dt);
        	ship.update(dt);
        	
        	wrapPosition(ship);
        	for (Bullet bullet : ship.getBullets()) {
        		wrapPosition(bullet);
        	}
        	for (Rock rock : rocks) {
        		rock.update(dt);
        		wrapPosition(rock);
        	}
        	for (Shrapnel shrap : shrapnel) {
        		shrap.update(dt);
        	}
        	
        	Iterator<Rock> i = rocks.iterator();
        	ArrayList<Rock> newRocks = new ArrayList<Rock>();
    		while (i.hasNext()) {
    			Rock rock = i.next();
    			Shape transformedRock = rock.getAffineTransform().createTransformedShape(rock.getShape());
    			
    			for (int a = 0; a < ship.getShape().npoints; a++) {
    				if (transformedRock.contains(ship.pos.x, ship.pos.y) && !ship.isImmune()) {
    					
    					spawnShrapnel(12, ship.pos);
    					
    					ship.setPosition(bounds.scale(0.5));
    					ship.setVelocity(new Vector2(0,0));
    					ship.rotation = -Math.PI/2;
    					
    					ship.setTimeSinceDeath(0);
    					ship.setLives(ship.getLives() - 1);
   
    				}
    			}
    			
    			
    			Iterator<Bullet> j = ship.getBullets().iterator();
    			while(j.hasNext()) {
    				Bullet bullet = j.next();

    				if (transformedRock.contains(bullet.pos.x, bullet.pos.y)) {
    					j.remove();
    					
    					newRocks.addAll(rock.split());
    					score += 100 * rock.lives;
    					
    					spawnShrapnel((int)Math.pow((rock.maxChildren + rock.minChildren) / 2, rock.lives), rock.pos);
    					
    					i.remove();
    					break;
    				}
    			}
    		}
    		
        	rocks.addAll(newRocks);
            spawnRocks(dt);
        	
        	if (spacePressed) ship.shoot();

        	Iterator<Shrapnel> shrapIt = shrapnel.iterator();
        	while(shrapIt.hasNext()) {
        		Shrapnel shrap = shrapIt.next();
        		if (shrap.lifespan < shrap.timeAlive) shrapIt.remove();
        	}
        	
        	frame.repaint();
        	
        	if (ship.getLives() <= 0) {
        		try {
    				Thread.sleep((long)(5000));    				
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}

				ship = new Ship();
        		ship.setPosition(bounds.scale(0.5));
        		score = 0;
        		rocks.clear();
				ship.setTimeSinceDeath(-5.0);
				
        	}

        	try {
				Thread.sleep((long)(1 / targetFPS * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
