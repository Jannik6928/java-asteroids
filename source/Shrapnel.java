import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Shrapnel extends Object {
	protected double timeAlive = 0.0;
	protected double lifespan = 0.0;
	
	private Polygon shape = new Polygon();
	
	Shrapnel(int minVertices, int maxVertices, double minRadius, double maxRadius, double minSpeed, double maxSpeed,
			double minRotationVel, double maxRotationVel, double minLife, double maxLife) {
		shape.reset();
		ArrayList<Double> angles = new ArrayList<Double>();
		int vertices = ThreadLocalRandom.current().nextInt(minVertices, maxVertices + 1);
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
		
		this.vel.x = Math.cos(Math.random() * 2 * Math.PI) * (minSpeed + Math.random() * (maxSpeed - minSpeed));
		this.vel.y = Math.sin(Math.random() * 2 * Math.PI) * (minSpeed + Math.random() * (maxSpeed - minSpeed));
		this.rotationVel = Math.random() * (maxRotationVel - minRotationVel) + minRotationVel;
		
		
		this.lifespan = Math.random() * (maxLife - minLife) + minLife;
	}
	
	public Polygon getShape() { return shape; }
	public AffineTransform getAffineTransform() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(rotation - Math.PI / 2);
		return at;
	}
	
	
	public void update(double dt) {
		updatePosition(dt);
		updateVelocity(dt);
		updateRotation(dt);
		timeAlive += dt;
	}
	
}
