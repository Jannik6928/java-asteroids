
public class Object {
	protected Vector2 pos = new Vector2();
	protected Vector2 vel = new Vector2();
	protected Vector2 accel = new Vector2();
	
	protected double rotation = 0;
	protected double rotationVel = 0;
	
	public Vector2 getPosition() { return pos; }
	public Vector2 getVelocity() { return vel; }
	public Vector2 getAcceleration() { return accel; }
	
	public void setPosition(Vector2 position) { this.pos = position; }
	public void setVelocity(Vector2 velocity) { this.vel = velocity; }
	public void setAcceleration(Vector2 acceleration) { this.pos = acceleration; }
	
	public void updateVelocity(double dt) {
		vel = vel.add(accel.scale(dt));
	}
	public void updatePosition(double dt) {
		pos = pos.add(vel.scale(dt));
		pos = pos.add(accel.scale(dt * dt * 0.5));
	}
	public void updateRotation(double dt) {
		rotation += rotationVel * dt;
		if (rotation < 0) rotation += Math.PI * 2;
		else if (rotation >=  Math.PI * 2) rotation -=  Math.PI * 2;
	}

	public void update(double dt) {
		updatePosition(dt);
		updateVelocity(dt);
		updateRotation(dt);
	}

}
