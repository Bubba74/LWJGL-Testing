
public class Powerup {
	
	private int x;
	private int y;
	public int width = 40;
	public int height = 60;
	private float dX;
	private float dY;
	public enum type {
		HEALTH,LASER,FIREWORK,CLEAR,
	};
	private type Powerup;
	
	
	public Powerup (int x,int y,float dX,float dY,type t){
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
		this.Powerup = t;
	}//Powerup constructor

	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	
	public float getDX(){return this.dX;}
	public float getDY(){return this.dY;}
	
	public Powerup.type getType(){return this.Powerup;}
	
}//Powerups class