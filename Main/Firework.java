package Main;

public class Firework {
	
	private int x;
	private int y;
	private float dX;
	private float dY;
	private int speed;
	private int power;
	private int time;
	private int delay;
	
	public Firework(int x,int y,float dX,float dY,int speed,int power,int time,int delay){
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
		this.speed = speed;
		this.power = power;
		this.time = time;
		this.delay = delay;
	}//Firework constructor
	public void setX(int x){this.x = x;}
	public void setY(int y){this.y = y;}
	
	public int getX(){return this.x;}
	public int getY(){return this.y;}
	
	public float getDX(){return this.dX;}
	public float getDY(){return this.dY;}
	
	public int getSpeed(){return this.speed;}
	public int getPower(){return this.power;}
	
	public int getTime(){return this.time;}
	public int getDelay(){return this.delay;}
	
	
	
}