
public class Projectile {
	
	private float x;
	private float y;
	private double deltaX;
	private double deltaY;
	private int speed = 5;
	
	public Projectile(float x, float y, double deltaX, double deltaY, int speed){
		this.x = x;
		this.y = y;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.speed = speed;
	}
	
	public void setX(float x){
		this.x = x;
	}
	public float getX(){
		return x;
	}
	
	public double getDeltaX(){

		return deltaX;
	}
	
	
	public void setY(float y){
		this.y = y;
	}	
	public float getY(){
	
		return y;
	}
	

	public double getDeltaY(){
		return deltaY;
	}
	

	public int getSpeed(){
		return speed;
	}
	
	
}
