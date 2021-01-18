
public class Vector2 {
	public double x;
	public double y;
	
	Vector2() {
		
	}
	Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	double size() {
		return Math.sqrt(x * x + y * y);
	}
	
	Vector2 resize(double s) {
		double sz = size();
		return new Vector2(s * this.x / sz, s * this.y / sz);
	}
	
	Vector2 add(Vector2 vec) {
		return new Vector2(x + vec.x, y + vec.y);
	}
	Vector2 subtract(Vector2 vec) {
		return new Vector2(this.x - vec.x, this.y - vec.y);
	}
	Vector2 scale(double s) {
		return new Vector2(s * this.x, s * this.y);
	}
}
