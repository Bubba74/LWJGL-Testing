package Main;
import org.lwjgl.opengl.Display;


public class Fighter {
	public final int MAXHEALTH = 5;
	private int x = Display.getWidth()+50;
	private int y = Display.getHeight()-60;
	private float dX;
	private float dY;
	private int width = 10;
	private int height = 10;
	private int health = MAXHEALTH;
	
	public Fighter(int x,int y,float dX,float dY,int width,int height,int health){
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
		this.width = width;
		this.height = height;
		this.health = health;
	}
	public void setX(int x){this.x = x;}
	public int getX(){return this.x;}
	public void setY(int y){this.y = y;}
	public int getY(){return this.y;}
	
	public int getWidth(){return this.width;}
	public int getHeight(){return this.height;}

	public float getDX(){return this.dX;}
	public float getDY(){return this.dY;}
	
	public void setHealth(int health){this.health = health;}
	public int getHealth(){return this.health;}
	
	public void hit(int damage){
		if(health>=damage){
			this.health -= damage;
		}
	}//hit method
	
	
}//Fighter class