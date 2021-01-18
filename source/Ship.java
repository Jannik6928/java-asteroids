
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class Ship extends Object {
	
	private int lives = 3;
	
	private double magAccel = 750;
	private double drag = 0.650;
	
	private double magRotationVel = Math.PI;
	
	private double timeSinceShot = 0.0;
	private double shotCooldown = 0.3;
	private double bulletSpeed = 600;
	private double bulletRotationVel = Math.PI * 2.0;
	private double bulletLifespan = 0.8;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private double timeSinceDeath = 0.0;
	private double immunityTime = 4.0;
	
	private static Polygon shape = new Polygon();
	private static Polygon exhaustShape = new Polygon();
	
	Ship() {
		pos = new Vector2(0, 0);
		vel = new Vector2(0, 0);
		accel = new Vector2(0, 0);
		
		super.rotation = -Math.PI / 2.0;
		
		shape.reset();
		shape.addPoint(0, 20);
		shape.addPoint(-10, -10);
		shape.addPoint(10, -10);
		
		exhaustShape.reset();
		exhaustShape.addPoint(-5, -10);
		exhaustShape.addPoint(5, -10);
		exhaustShape.addPoint(0, -20);
	}
	
	public ArrayList<Bullet> getBullets() { return bullets; }
	public Polygon getShape() { return shape; }
	public Polygon getExhaustShape() { return exhaustShape; }
	public AffineTransform getAffineTransform() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(rotation - Math.PI / 2);
		return at;
	}
	
	
	
	void updateAcceleration(boolean isMoving) {
		if (isMoving) {
			super.accel.x = magAccel * Math.cos(rotation);
			super.accel.y = magAccel * Math.sin(rotation);
		}
		else {
			super.accel.x = 0;
			super.accel.y = 0;
		}
	}
	void updateAngularVelocity(boolean left, boolean right, double dt) {
		if (left) rotation -= magRotationVel * dt;
		if (right) rotation += magRotationVel * dt;
		if (rotation < 0) rotation += Math.PI * 2;
		else if (rotation >=  Math.PI * 2) rotation -=  Math.PI * 2;
	}
	
	public void update(double dt) {
		updatePosition(dt);
		updateVelocity(dt);
		vel = vel.scale(Math.pow(drag, dt));
		timeSinceShot += dt;
		timeSinceDeath += dt;
		
		Iterator<Bullet> i = bullets.iterator();
		while (i.hasNext()) {
			Bullet bullet = i.next();
			bullet.update(dt);
			if (bullet.timeAlive > bullet.lifespan) {
				bullet = null;
				i.remove();
			}
		}
	}
	

	public void shoot() {
		if (timeSinceShot < shotCooldown) return;
		
		Bullet bullet = new Bullet();
		
		bullet.vel = new Vector2(bulletSpeed * Math.cos(rotation), bulletSpeed * Math.sin(rotation));
		bullet.vel = bullet.vel.add(this.vel);
				
		bullet.pos = this.pos;
		bullet.rotationVel = bulletRotationVel;
		bullet.lifespan = bulletLifespan;
		timeSinceShot = 0.0;
		
		bullets.add(bullet);
	}
	
	public boolean isImmune() {
		return timeSinceDeath <= immunityTime;
	}
	
	public double getTimeSinceDeath() {return timeSinceDeath;}
	public double getImmunityTime() {return immunityTime;}
	public int getLives() {return lives;}
	
	public void setTimeSinceDeath(double t) {timeSinceDeath = t;}
	public void setLives(int l) {lives = l;}
}
