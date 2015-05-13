
public class FighterShip {
	public final int MAXHEALTH = 15;
	
	private float x = 500;
	private float y = 400;
	private float rotateAngle = 0.01f;
	private int health = MAXHEALTH;
	
	public void setX(float x){this.x = x;}
	public void addToX(float x){this.x += x;}
	public float getX(){return this.x;}
	
	public void setY(float y){this.y = y;}
	public void addToY(float y){this.y += y;}
	public float getY(){return this.y;}
	
	public void setRotateAngle(float rotateAngle){this.rotateAngle = rotateAngle;}
	public void addToRA(float rA){ this.rotateAngle += rA;}
	public float getRotateAngle(){return this.rotateAngle;}

	public void setHealth(int health){this.health = health;}
	public void addToHealth(int health){this.health += health;}
	public int getHealth(){return health;}
	
	
	
	
}
