package MeleeGame;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import Utilities.Functions;
import MeleeGame.MeleeGame.dir;

public class Mob {
	
	public enum mobType{
		ZOMBIE,SPIDER,BAT	
	};
	public enum dir{
		UP,DOWN,RIGHT,LEFT
	}
	
	private int x;
	private int y;
	private int dX;
	private int dY;
	private float angle;
	public final int WIDTH = 30;
	public final int HEIGHT = 40;
	private mobType type = mobType.ZOMBIE;
	private dir direction = dir.DOWN;
	
	private Texture skin;
	public static Texture textures[] = new Texture[4];
	
	private int attack;
	private int defense;
	private int hp;
	private int speed;
	
	private Random rn = new Random();

	public static void loadTextures(){
		try {
			textures[0] = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/zombieFront.png")));
			textures[1] = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/zombieBack.png")));
			textures[2] = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/zombieRight.png")));
			textures[3] = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/zombieLeft.png")));
		} catch (IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}//loadTextures method
	
	public Mob (int x,int y,mobType type){
		this.x = x;
		this.y = y;
		this.type = type;
		this.direction = dir.DOWN;
		
		if (type == mobType.ZOMBIE){
			this.attack = 4;
			this.defense = 5;
			this.hp = 20;
			this.speed = 2;
			this.skin = textures[0];
		} else if (type == mobType.SPIDER){
			this.attack = 5;
			this.defense = 4;
			this.hp = 16;
			this.speed = 6;
		} else if (type == mobType.BAT){
			this.attack = 6;
			this.defense = 3;
			this.hp = 12;
			this.speed = 5;
		}
	}//Mob constructor
	
	public void draw (){
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		skin.bind();
		
		GL11.glColor3f(1f, 1f, 1f);
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2i(x-WIDTH/2,y-HEIGHT/2);
			glTexCoord2f(1,0);
			glVertex2i(x+WIDTH/2,y-HEIGHT/2);
			glTexCoord2f(1,1);
			glVertex2i(x+WIDTH/2,y+HEIGHT/2);
			glTexCoord2f(0,1);
			glVertex2i(x-WIDTH/2,y+HEIGHT/2);
		glEnd();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		
	}//draw method
	public void update (int cX,int cY){
		dX = (cX)-x;
		dY = (cY)-y;
		if (dX==0)dX+=1;
		if (dY==0)dY+=1;
		
		if (15<=Math.hypot(dX,dY) && Math.hypot(dX,dY)<=600){
			float hypot = (float) Math.hypot(dX, dY);
			float angle = (float) (Math.atan2(dY,dX) * 18 * 3.14159);
			x += (dX/hypot)*speed;
			y += (dY/hypot)*speed;
//			System.out.printf("dX: %s , dY: %s , hypot: %s , angle: %s\n",dX,dY,hypot,angle);
			if (Math.abs(dX)>Math.abs(dY)){
				if (dX>0){
					direction = dir.RIGHT;
				} else{
					direction = dir.LEFT;
				}
			} else if (Math.abs(dX)<Math.abs(dY)){
				if (dY>0){
					direction = dir.DOWN;
				} else {
					direction = dir.UP;
				}
			}
		}
		
		updateTexture();
	}//update method
	public void hit(int dam,int screenX, int screenY,int kb){
		setHealth(getHealth()-dam);
		int iX = 0;
		int iY = 0;
		if (direction == dir.UP){
			iX = 0;
			iY = -1;
		} else if (direction == dir.DOWN){
			iX = 0;
			iY = 1;
		} else if (direction == dir.RIGHT){
			iX = 1;
			iY = 0;
		} else if (direction == dir.LEFT){
			iX = -1;
			iY = 0;
		}
		setX(x-(kb*iX));
		setY(y-(kb*iY));
//		System.out.println("HIT");
	
		int tempX = x-screenX;
		int tempY = y-screenY;
		int w = WIDTH;
		int h = HEIGHT;
	
		glBegin(GL_QUADS);
		glColor3f(1,0,0);
			glVertex2i(tempX-w/2,tempY-h/2);
			glVertex2i(tempX+w/2,tempY-h/2);
			glVertex2i(tempX+w/2,tempY+h/2);
			glVertex2i(tempX-w/2,tempY+h/2);
		glEnd();
	}//hit method
	public boolean contact(int x, int y){
		boolean contact = false;
		
		if (Math.hypot(this.x-x, this.y-y)<40){
			contact = true;
		}
		
		
		return contact;
	}//contact method
	
	public void updateTexture(){
		if (direction == dir.UP){
			skin = textures[1];
		} else if (direction == dir.DOWN){
			skin = textures[0];
		} else if (direction == dir.RIGHT){
			skin = textures[2];
		} else if (direction == dir.LEFT){
			skin = textures[3];
		}
	}//updateTexture method
	/////////////REGULAR SET GETS///////////////////
	public void setX(int x){this.x = x;}
	public int getX(){return x;}
	
	public void setY(int y){this.y = y;}
	public int getY(){return y;}

	public void setDX(int dX){this.dX = dX;}
	public int getDX(){return this.dX;}
	
	public void setDY(int dY){this.dY = dY;}
	public int getDY(){return this.dY;}
	
	public void setType(mobType type){this.type = type;}
	public mobType getType(){return type;}

	public void setHealth(int health){this.hp = health;}
	public int getHealth(){return hp;}

	public int getAttack(){return attack;}
	public int getDefense(){return defense;}
	public int getSpeed(){return speed;}
	
}//Mob class