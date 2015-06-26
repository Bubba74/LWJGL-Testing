package MeleeGame;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Main.Entity;
import Main.Projectile;
import Utilities.Draw;
import Utilities.Notify;

public class MeleeGame {

//	public static final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//	public static final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 800;

	private final int BGW = 20000;
	private final int BGH = 15000;
	
	private Texture background;
	private List<Mob> mobs = new ArrayList<Mob>();
	
	public static int cameraX = 200;
	public static int cameraY = 200;
	private Notify output;
	
	public enum dir {
		UP,DOWN,RIGHT,LEFT
	};
	private dir direction = dir.DOWN;
	
	private final float MAXHEALTH = 200;
	private Texture character;
	private float health = MAXHEALTH;
	private int damage = 4;
	private int knockback = 30;
	private int exp = 0;
	private int slotNum = 0;
	private String items[] = {"sword","gun"};
	private String currentItem = "sword";
	private List<Entity> arrows = new ArrayList<Entity>();
	private boolean walking = false;
	
	private int hitTime = 0;
	private boolean effect = false;
	private Random rn = new Random();
	
	
	public void render(){
		glPushMatrix();
			glTranslatef(-cameraX,-cameraY,0);
			
			glEnable(GL_TEXTURE_2D);
			drawBackground();
			glDisable(GL_TEXTURE_2D);
			
			mobs();
			drawChar();
			displayArrows();

		glPopMatrix();
		
		walking = false;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && (cameraY>0)){
			cameraY -= 7;
			direction = dir.UP;
			walking = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && (cameraY+HEIGHT<BGH)){
			cameraY += 7;
			direction = dir.DOWN;
			walking = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && (cameraX+WIDTH<BGW)){
			cameraX += 7;
			direction = dir.RIGHT;
			walking = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && (cameraX>0)){
			cameraX -= 7;
			direction = dir.LEFT;
			walking = true;
		}
		
		while(Keyboard.next()){
			if (Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey() == Keyboard.KEY_N)spawnMob();
				if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL) changeSlot(1);
				if (Keyboard.getEventKey() == Keyboard.KEY_RMENU) changeSlot(-1);
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE){
					if (hitTime == 0){
						if (currentItem.equals("sword")) hit(cameraX+WIDTH/2,cameraY+HEIGHT/2,100,true);
					}
					if (currentItem.equals("gun")){
						float dX = 0;
						float dY = 0;
						if (direction == dir.UP){
							dX = 0;
							dY = -2;
						} else if (direction == dir.DOWN){
							dX = 0;
							dY = 2;
						} else if (direction == dir.RIGHT){
							dX = 2;
							dY = 0;
						} else if (direction == dir.LEFT){
							dX = -2;
							dY = 0;
						}
						
						arrows.add(new Entity(new Projectile(cameraX+WIDTH/2,cameraY+HEIGHT/2,dX,dY,4,-1)));
						System.out.printf("X: %s, Y: %s, DX: %s, DY: %s\n",cameraX+WIDTH/2,cameraY+HEIGHT/2,dX,dY);
						
					}
				}
			}
		}
		if (rn.nextInt(200)==0){
			spawnMob();
		}
		if (hitTime != 0){
			if (currentItem.equals("sword"))swing();
		}
		if (currentItem.equals("gun"))checkArrows();
		
		output.addOutput("Exp: "+exp, 1);
		output.display();
//		output.clear();
	}//render method
	
	public void swing(){
		int baseDegree;
		int spacer = 0;
		int rD = 1;
		if (direction == dir.RIGHT){
			baseDegree = -90;
			spacer = 10;
			rD = -1;
		} else if (direction == dir.DOWN){
			baseDegree = 180;
		} else if (direction == dir.LEFT){
			baseDegree = -90;
			spacer = -10;
		} else {
			baseDegree = 0;
		}

		boolean run = true;
		int i = 0;

		int frames = 20;
		int frameRate = 10;
		for (int f=0;f<frames;f++){
			if (frameRate*f <= (int)System.currentTimeMillis()-hitTime &&
					frameRate*(f+1) <= (int)System.currentTimeMillis()-hitTime){
				i = f+1;
//				System.out.println(f);
			}
			if (frameRate*frames <= (int)System.currentTimeMillis()-hitTime &&
					frameRate*(frames+1) <= (int)System.currentTimeMillis()-hitTime){
				hitTime = 0;
				run = false;
			}
		}
		
		i *= rD;
//		System.out.println((int)System.currentTimeMillis());
//		System.out.println(hitTime);
//		System.out.println((int)System.currentTimeMillis()-hitTime);
//		System.out.println(i);
		int instances = 1;
		if (effect)instances = Math.abs(i);
		for (int z=0;z<instances;z++){
			int a=z;
			if (i<0)a*=-1;
			if (run){
				glPushMatrix();
					glTranslatef(spacer+WIDTH/2,HEIGHT/2,0);
					glRotatef(baseDegree+(-(effect?a:i)*(180/frames)),0,0,1);
						glBegin(GL_QUADS);
							glColor3f(1,0,0);
							glVertex2i(10,0);
							glVertex2i(80,0);
							glColor3f(0,1,0);
							glVertex2i(20,5);
							glVertex2i(10,5);
						glEnd();
				glPopMatrix();
			}
		}
	}//swing method
	public boolean hit(int cX,int cY,int range, boolean enableSplash){
		if (currentItem.equals("sword"))hitTime = (int)System.currentTimeMillis();
		
		boolean splash = true;
		for (Mob m:mobs){
			if(splash){
				float dX = m.getX()-(cX);
				float dY = m.getY()-(cY);
				if (Math.hypot(dX, dY)<range){
					if ((dX<0 && direction == dir.LEFT) || (dX>0 && direction == dir.RIGHT) 
						|| (dY<0 && direction == dir.UP) || (dY>0 && direction == dir.DOWN)){
						m.hit(damage, cameraX, cameraY, knockback);
						if (!enableSplash){
							splash = false;
						}
					}
				}
			}
		}
		return !splash;
	}//hit method
	public void checkArrows(){
		List<Entity> dump = new ArrayList<Entity>();
//		System.out.println("Checking Arrows...");
		for (Entity e:arrows){
			if (Math.hypot(e.getX()-(cameraX+WIDTH/2), e.getY()-(cameraY+HEIGHT/2))>900){
				dump.add(e);
//				System.out.println("Arrow dumped");
			} else {
				e.addToX(e.getDX()*e.getSpeed());
				e.addToY(e.getDY()*e.getSpeed());
				if (hit((int) e.getX(),(int)e.getY(),20,false))dump.add(e);
				
//				System.out.printf("DX: %s, DY: %s\n",e.getDX(),e.getDY());
			}
		}
		for (Entity e:dump){
			arrows.remove(e);
		}
		
	}//checkArrows method
	public void displayArrows(){
		glColor3f(1,0,0);
		for (Entity e:arrows){
			int tX = (int) (e.getX()-(cameraX));
			int tY = (int) (e.getY()-(cameraY));
			glColor3f(1,0,0);
			Draw.drawCircle(tX, tY, 3, "fill");
		}
		
	}//displayArrows method
	
	public void changeSlot(int value){
		if (value < 0){
			if (slotNum >= 1) slotNum -= 1;
		} else if (value > 0){
			if (slotNum <= items.length-2) slotNum += 1;
		}
		currentItem = items[slotNum];
	}//changeSlot
	

	public void spawnMob(){
		mobs.add(new Mob(cameraX+rn.nextInt(WIDTH),cameraY+rn.nextInt(HEIGHT),Mob.mobType.ZOMBIE));
	}//spawnMob method
	public void mobs(){
		List<Mob> deadMobs = new ArrayList<Mob>();
		for (Mob m:mobs){
			if (m.getHealth()<=0){
				deadMobs.add(m);
				exp += (m.getAttack()+m.getDefense()+m.getSpeed());
			} else {
				m.update(cameraX+WIDTH/2,cameraY+HEIGHT/2);
				m.draw();
				if (m.contact(cameraX+WIDTH/2,cameraY+HEIGHT/2)){
					
					health -= m.getAttack();
					cameraX += m.getDX()/2;
					cameraY += m.getDY()/2;
					System.out.println("HEALTH: " + health);
				}
				
			}
		}
		for (Mob m:deadMobs){
			mobs.remove(m);
		}
	}//drawMobs method


	public void drawChar(){
		int baseDegree;
		int spacer = 0;
		if (direction == dir.RIGHT){
			baseDegree = -90;
			spacer = 10;
		} else if (direction == dir.DOWN){
			baseDegree = 180;
		} else if (direction == dir.LEFT){
			baseDegree = -90;
			spacer = -10;
		} else {
			baseDegree = 0;
		}
		
		glPushMatrix();
		glTranslatef(cameraX+WIDTH/2,cameraY+HEIGHT/2,0);
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glColor3f(1,1,1);
		float charValueX = charTexFloat("X");
		float charValueY = charTexFloat("Y");
		character.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(charValueX,charValueY);
			glVertex2f(-15,-42.5f);
			glTexCoord2f(charValueX+.3f,charValueY);
			glVertex2f(15,-42.5f);
			glTexCoord2f(charValueX+.3f,charValueY+.25f);
			glVertex2f(15,42.5f);
			glTexCoord2f(charValueX,charValueY+.25f);
			glVertex2f(-15,42.5f);
		glEnd();
//		System.out.println("X: "+charValueX + " Y: "+charValueY);
//		character.release();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		displayHealth();

		if (currentItem.equals("sword") && hitTime==0){//If you are NOT swinging
			glRotatef(baseDegree,0,0,1);
			glTranslatef(0,spacer,0);
				glBegin(GL_QUADS);
					glColor3f(1,0,0);
					glVertex2i(10,0);
					glVertex2i(80,0);
					glColor3f(0,1,0);
					glVertex2i(20,5);
					glVertex2i(10,5);
				glEnd();
		}
		if (currentItem.equals("gun")){
			int degree = 0;
			if (direction == dir.UP){
				degree = -90;
			} else if (direction == dir.DOWN){
				degree = 90;
			} else if (direction == dir.RIGHT){
				degree = 0;
			} else if (direction == dir.LEFT){
				degree = 180;
			}
			glRotatef(degree,0,0,1);
			glColor3f(0,0,0);
			glBegin(GL_QUADS);
				glVertex2i(15,8);
				glVertex2i(40,8);
				glVertex2i(40,-8);
				glVertex2i(15,-8);
			glEnd();
			
			glColor3f(.3f,.3f,.3f);
			glBegin(GL_QUADS);
				glVertex2i(40,4);
				glVertex2i(70,4);
				glVertex2i(70,-4);
				glVertex2i(40,-4);
			glEnd();
		}
		
		glPopMatrix();
	}//drawChar method
	public void displayHealth(){
		int w = WIDTH/2;
		int h = (HEIGHT<1080?HEIGHT/2:HEIGHT/2-50);
		glColor3f(1,0,0);
		glBegin(GL_QUADS);
			glVertex2i(-w,h-20);
			glVertex2i(w,h-20);
			glVertex2i(w,h);
			glVertex2i(-w,h);
		glEnd();
		int lostHealth = (int) (2*w * (health/MAXHEALTH)-w);
//		System.out.println(lostHealth);
		glColor3f(0,1,0);
		glBegin(GL_QUADS);
			glVertex2i(-w,h-20);
			glVertex2i(lostHealth,h-20);
			glVertex2i(lostHealth,h);
			glVertex2i(-w,h);
		glEnd();
	}//displayHealth method
	
	public float charTexFloat(String coord){
		float charStart = 0;
		if (coord.equals("X")){
			if (walking)charStart += 1/3;
		} else {
			if (direction == dir.UP){
				charStart = .5f;
			} else if (direction == dir.DOWN){
				charStart = 0;
			} else if (direction == dir.RIGHT){
				charStart = .75f;
			} else if (direction == dir.LEFT){
				charStart = .25f;
			}
		}
		return charStart;
	}//charTexFloat method
	
	public void drawBackground(){
		background.bind();
		GL11.glColor3f(1f, 1f, 1f);
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2i(WIDTH/2,HEIGHT/2);
			glTexCoord2f(1,0);
			glVertex2i(BGW-WIDTH/2,HEIGHT/2);
			glTexCoord2f(1,1);
			glVertex2i(BGW-WIDTH/2,BGH-HEIGHT/2);
			glTexCoord2f(0,1);
			glVertex2i(WIDTH/2,BGH-HEIGHT/2);
		glEnd();
	}//drawBackground method	
	public void loadBackground(){
		try{
			background = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/PokemonMap.png")));
		} catch(IOException e){
			e.printStackTrace();
			background = null;
//			System.out.println("WHOOPS");
		}
	}//loadBackground method
	public void loadCharacter(){
		try{
			character = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/Character.png")));
		} catch(IOException e){
			e.printStackTrace();
			System.exit(0);
			character = null;
		}
	}//loadChar method
	
	public void start(){
		initGL();
		loadBackground();
		loadCharacter();
		Mob.loadTextures();
		output = new Notify();
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			glClear(GL_COLOR_BUFFER_BIT);

			render();
			
			
			Display.update();
			Display.sync(40);
		}
		Display.destroy();
		System.exit(0);
		
	}//start method
	public void initGL(){
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("LWJGL APPLICATION ["+WIDTH+", "+HEIGHT+"]");
			Display.setInitialBackground(0, 0, 0);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
		}
			GL11.glEnable(GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL_BLEND);
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0,WIDTH,HEIGHT,0,1,-1);
			glMatrixMode(GL_MODELVIEW);
	}//initGL method
	public static void main(String Args[]){
		MeleeGame app = new MeleeGame();
		app.start();
	}//main method
	
}