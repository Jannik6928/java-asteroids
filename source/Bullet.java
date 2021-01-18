import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class Bullet extends Object {
	protected double timeAlive = 0.0;
	protected double lifespan = 0.0;
	
	private static Polygon shape = new Polygon();
	
	Bullet() {
		shape.reset();
		shape.addPoint(3, 3);
		shape.addPoint(-3, 3);
		shape.addPoint(-3, -3);
		shape.addPoint(3, -3);
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
