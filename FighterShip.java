
public class FighterShip {
	private float x = 500;
	private float y = 400;
	private float rotateAngle = 0.01f;
	
	
	public void setX(float x){this.x = x;}
	public void addToX(float x){this.x += x;}
	public float getX(){return this.x;}
	
	public void setY(float y){this.y = y;}
	public void addToY(float y){this.y += y;}
	public float getY(){return this.y;}
	
	public void setRotateAngle(float rotateAngle){this.rotateAngle = rotateAngle;}
	public void addToRA(float rA){ this.rotateAngle += rA;}
	public float getRotateAngle(){return this.rotateAngle;}

	
	
	
	
	
}
