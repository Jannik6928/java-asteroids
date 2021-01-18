import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Rock extends Object {
	protected double maxRadius = 100;
	protected double minRadius = 80;
	protected int vertices = 12;
	
	protected int lives = 3;
	protected int minChildren = 2;
	protected int maxChildren = 4;
	
	protected double maxSpeed = 150;
	protected double minSpeed = 30;
	
	private Polygon shape = new Polygon();
	
	protected double maxRotationVel = Math.PI / 2;
	protected double minRotationVel = -Math.PI / 2;
	
	
	Rock() {
		shape = new Polygon();
	}
	
	public Polygon getShape() { return shape; }
	public AffineTransform getAffineTransform() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(rotation - Math.PI / 2);
		return at;
	}
	
	public void randomizeShape() {
		shape.reset();
		ArrayList<Double> angles = new ArrayList<Double>();
		for (int i = 0; i < vertices; i++) {
			angles.add(Math.random() * Math.PI * 2);
		}
		Collections.sort(angles);
		for (int i = 0; i < angles.size(); i++) {
			shape.addPoint((int)(Math.cos(angles.get(i)) * (minRadius + Math.random() + (maxRadius - minRadius))), 
					(int)(Math.sin(angles.get(i)) * (minRadius + Math.random() * (maxRadius - minRadius))));
		}
		double sx = 0, sy = 0;
		for (int i = 0; i < shape.npoints; i++) {
			sx += shape.xpoints[i];
			sy += shape.ypoints[i];
		}
		sx /= shape.npoints;
		sy /= shape.npoints;
		shape.translate((int)-sx, (int)-sy);
	}
	public void randomizeMovement() {
		this.vel.x = Math.cos(Math.random() * 2 * Math.PI) * (minSpeed + Math.random() * (maxSpeed - minSpeed));
		this.vel.y = Math.sin(Math.random() * 2 * Math.PI) * (minSpeed + Math.random() * (maxSpeed - minSpeed));
		this.rotationVel = Math.random() * (maxRotationVel - minRotationVel) + minRotationVel;
	}
	
	double getThreat() {
		return Math.pow((minChildren + maxChildren) / 2, (double)lives - 1);
	}
	
	public ArrayList<Rock> split() {
		ArrayList<Rock> children = new ArrayList<Rock>();
		
		if (lives - 1 <= 0) return children;
		
		int randomNum = ThreadLocalRandom.current().nextInt(minChildren, maxChildren + 1);
		for (int i = 0; i < randomNum; i++) {
			Rock child = new Rock();
			
			child.lives = this.lives - 1;
			child.maxRadius = this.maxRadius / 2;
			child.minRadius = this.minRadius / 2;
			
			child.randomizeShape();
			child.randomizeMovement();
			child.pos = this.pos;

			children.add(child);
		}
		return children;
	}

	public void update(double dt) {
		updatePosition(dt);
		updateVelocity(dt);
		
		rotation += rotationVel * dt;
		if (rotation < 0) rotation += Math.PI * 2;
		else if (rotation >=  Math.PI * 2) rotation -=  Math.PI * 2;
	}
}
