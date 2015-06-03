package Main;

public class Entity {
	
	
	public enum specialType{
		BULLET,BOMB,HEALTH,LASER,FIREWORK,CLEAR
	};
	private specialType ability;
	private int shipNum;
	
	private int x;
	private int y;

	private float dX;
	private float dY;
	
	private int width;
	private int height;
	
	private int power;
	private int speed;
	
	private int health;
	private float rotateAngle;
	
	private int time;
	private int delay;

	//////////////CONSTRUCTORS////////////////////
	
	
	//////////////^^^^^^^^^^^^////////////////////
	
	public Projectile toProj(){
		
		return new Projectile(x,y,dX,dY,speed,shipNum);
		
	}//toProj method
	
	//////////////CHANGE METHODS/////////////////
	
	public void setShipNum(int shipNum){this.shipNum = shipNum;}
	public int getShipNum(){return this.shipNum;}
	
	public void setX(int x){this.x = x;}
	public int getX(){return x;}
	public void addToX(int newX){this.x += newX;}

	public void setY(int y){this.y = y;}	
	public int getY(){return y;}
	public void addToY(int newY){this.y += newY;}
	
	public void setDX(float newU){this.dX = newU;}
	public float getDX(){return dX;}
	public void addToDX(float newU){this.dX += newU;}
	
	public void setDY(float newV){this.dY = newV;}
	public float getDY(){return dY;}
	public void addToDY(float newV){this.dY += newV;}
	
	public void setWidth(int width){this.width = width;}	
	public int getWidth(){return width;}
	
	public void setHeight(int height){this.height = height;}	
	public int getHeight(){return height;}
	
	
	public void setPower(int power){this.power = power;}
	public int getPower(){return this.power;}
	
	public void setSpeed(int speed){this.speed = speed;}
	public int getSpeed(){return speed;}
	
	public void setRotateAngle(float rA){this.rotateAngle = rA;}
	public float getRotateAngle(){return this.rotateAngle;}
	public void addToRotateAngle(float newRA){this.rotateAngle += newRA;}
	
	
	public void setHealth(int health){this.health = health;}
	public int getHealth(){return this.health;}
	public void addToHealth(int newHealth){this.health += newHealth;}
	
	public void setAbility(specialType type){this.ability = type;}
	public specialType getAbility(){return this.ability;}
	
	public void setTime(int time){this.time = time;}
	public int getTime(){return this.time;}
	
	public void setDelay(int delay){this.delay = delay;}
	public int getDelay(){return this.delay;}
	
	
	/////////////^^^^^^^^^^^^^^^^^^^////////////////
	
	
}//Entity class