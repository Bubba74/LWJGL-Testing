
public class Obstacle {
	
	private int x;
	private int y;
	private int width = 180;
	private int height = 70;
	
	int level;
	public static final int PRIMARY = 0;
	public static final int SECONDARY = 1;
	public static final int TERTIARY = 2;
	
	boolean canMove = false;
	float r = 0;
	float g = 0;
	float b = 0;

	public Obstacle(){
		this.x = 50;
		this.y = 50;
	}
	public Obstacle(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x){this.x = x;}
	public int getX(){return this.x;}
	
	public void setY(int y){this.y = y;}
	public int getY(){return this.y;}
	
	public void setWidth(int width){this.width = width;}
	public int getWidth(){return this.width;}
	
	public void setHeight(int height){this.height = height;}
	public int getHeight(){return this.height;}
	
	public void setColor(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public float getR(){return this.r;}
	public float getG(){return this.g;}
	public float getB(){return this.b;}
	
	public void setControlled(boolean canMove, int up, int down, int right, int left){
		this.canMove = canMove;
		if (canMove){
			
		}
	}
	
	
	
	
}
