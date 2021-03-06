import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class BowMaster {
	
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int WIDTH = (int)d.getWidth();
	public int HEIGHT = (int)d.getHeight();
	private Random rn = new Random();
	
	private boolean isPaused = false;
	private TrueTypeFont print;
	private int printHeight;
	private Notify display;
	
	private int x = 200;
	private int y = 750;
	private double angle;
	
	private List<Powerup> powerups = new ArrayList<Powerup>();
	private List<Projectile> projs = new ArrayList<Projectile>();
	private List<Fighter> fighters = new ArrayList<Fighter>();
	
	private final int MAXHEALTH = 203;
	private int health = MAXHEALTH;

	private int kills = 0;
	private double shots = 0;
	private double hits = 0;
	private boolean autoFire = true;
	
	public void pauseMenu(){
		while(true){
//			System.out.println("P");
			if(Keyboard.isKeyDown(Keyboard.KEY_U)){
				isPaused = false;
				break;
			}
			Display.update();
			Display.sync(20);
		}
		
		
	}//pauseMenu method
	public void render(){
		if(isPaused){
			pauseMenu();
		}
		GL11.glDisable(GL11.GL_BLEND);
		drawCastle();
		
//		display.addOutput("'BowMaster'", 100000);
		
		if(Mouse.getX() != x){
			angle = Math.atan2((HEIGHT - Mouse.getY())-y,Mouse.getX()-x) * 18 * 3.14159;
		}
		String displayText = "";
		displayText += "X: "+Mouse.getX()+" Y: "+(HEIGHT-Mouse.getY());
		displayText += " Angle: "+angle;
		displayText += " Projs: "+projs.size();
		displayText += " Enemies: "+fighters.size();
		displayText += " Kills: "+kills;
		displayText += " Hits: "+hits;
		displayText += " Shots: "+shots;
		if (shots ==0){
//			displayText += " Acc: 0";
		} else {
			displayText += " Acc: "+(hits/shots);
		}
		displayText += " Health: "+(100*health/MAXHEALTH)+"%";
//		displayText += " Health: "+20;
//		displayText += " Powerups: "+powerups.size();
		
		///////////////SHOOTING/////////////////////////
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				if (Mouse.getEventButton()==0)shoot();
				
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && autoFire && Mouse.isButtonDown(0)){
			for (int i=0;i<10;i++){
				shoot();
			}
			display.addOutput("WOAH, SPECIAL SHOT",2);
			autoFire = false;
		}
		
		if (rn.nextInt(200)==0) autoFire = true;
		if(autoFire){
			drawSpecial();
		}
		////////////^^^^^^^^^^^^^^^^^^^^^///////////////
		
		updateProjectilesAndFighters();
		
		if (rn.nextInt((int) (200 / (1+(kills*.01))))==0){
			spawnFighter();
//			System.out.println("ENEMY "+fighters.size()+" SPAWNED");
		}
		if (rn.nextInt(50)==0){
			spawnPowerup();
		}
		
		
		////////////////////////////////////////////////////////////
//		GL11.glEnable(GL11.GL_BLEND);
		Draw.drawString(print, displayText, WIDTH/2, printHeight/2, Color.black);
		display.display();
		GL11.glDisable(GL11.GL_BLEND);
		////////////////Keyboard Events//////////////////////////////
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey()==Keyboard.KEY_P)isPaused = true;
				
			}
		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_P))isPaused = true;
	}//render method
	
	public void drawSpecial(){
		GL11.glBegin(GL_QUADS);
			GL11.glColor3f(1, 0, 1);

			GL11.glVertex2i(x-20, y-20);
			GL11.glVertex2i(x+20, y-20);
			GL11.glVertex2i(x+20, y+20);
			GL11.glVertex2i(x-20, y+20);
		GL11.glEnd();
		
		
	}//drawSpecial
	public void drawCastle(){
		GL11.glColor3f(0,0,0);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x-5, y-5);
			GL11.glVertex2f(x+5, y-5);
			GL11.glVertex2f(x+5, y+5);
			GL11.glVertex2f(x-5, y+5);
		GL11.glEnd();
		/////////HEALTH//////////////////
		int hi = HEIGHT-50;
		int MH = MAXHEALTH-3;
		int hl = health-3;
		int newY = ((MH-hl)*hi)/MH;
	
		GL11.glColor3f(1, 0, 0);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(0, 0);
			GL11.glVertex2i(50, 0);
			GL11.glVertex2i(50, newY);
			GL11.glVertex2i(0, newY);
		GL11.glEnd();
		
		GL11.glColor3f(0, 1, 0);
		
		GL11.glBegin(GL_QUADS);
		GL11.glColor3f(0, 1, 0);
			glVertex2i(0,newY);
			glVertex2i(50,newY);
			glVertex2i(50,newY+hi-newY);
			glVertex2i(0,newY+hi-newY);
		GL11.glEnd();
		
	}//drawCastle method
	public void spawnFighter(){
		fighters.add(new Fighter(WIDTH+50,HEIGHT-200,-0.3f,0f,60,80,5));
	}//spawnFighter method
	public void spawnPowerup(){
		int type = rn.nextInt(100);
		Powerup.type t = Powerup.type.HEALTH;
		if (type<=20)t = Powerup.type.HEALTH;
		if (type>20 && type<=50)t = Powerup.type.LASER;
		if (type>50 && type<=90)t = Powerup.type.FIREWORK;
		if (type>90 && type<=100)t = Powerup.type.CLEAR;
		
		int x = rn.nextInt(WIDTH/2)+WIDTH/4;
		int y = rn.nextInt(HEIGHT/3)+HEIGHT/3;
		float dX = rn.nextFloat();
		float dY = rn.nextFloat();
		
		powerups.add(new Powerup(x,y,dX,dY,t));
		
	}//spawnPowerup method
	
	public void updateProjectilesAndFighters(){
		Projectile removeProj = null;
		Fighter deadFighter = null;
		Powerup removePow = null;
		Powerup activatePow = null;
		for (Projectile p:projs){
			if (p.getX()<0 || 
				p.getX()>WIDTH ||
				p.getY()<0 ||
				p.getY()>HEIGHT){
				removeProj = p;
			}else {
				for(Fighter f:fighters){
					if (f.getX()<p.getX() && p.getX()<f.getX()+f.getWidth() &&
						f.getY()<p.getY() && p.getY()<f.getY()+f.getHeight()){
						removeProj = p;
						hits++;
						f.hit(1);
						if (f.getHealth()<=0) deadFighter = f;
					}
				}
				for (Powerup pow:powerups){
					if (pow.getX()<0 || 
						pow.getX()>WIDTH ||
						pow.getY()<0 ||
						pow.getY()>HEIGHT){
						System.out.println("POW REMOVED");
						removePow = pow;
					} else {
						if (pow.getX()<p.getX() && p.getX()<pow.getX()+pow.width &&
								pow.getY()<p.getY() && p.getY()<pow.getY()+pow.height){
							removeProj = p;
							activatePow = pow;
							removePow = pow;
						}
					}
				}
				if (removeProj == null)Draw.drawProj(p);
				p.addToDeltaY(+.04 * p.getShipNum()*-1);
			}
		}
		if (removeProj!=null){
			projs.remove(removeProj);
			removeProj = null;
		}
		if (deadFighter != null){
			fighters.remove(deadFighter);
			kills+=1;
			deadFighter = null;
		}
		if (activatePow != null){
			activatePowerup(activatePow);
			activatePow = null;
		}
		if (removePow != null){
			powerups.remove(removePow);
			removePow = null;
		}
		
		for (Powerup p: powerups){
			Draw.drawPowerup(p);
		}
		for (int i=0;i<fighters.size();i++){
			if (fighters.get(i).getX()<0){
				deadFighter = fighters.get(i);
				health -= 10;
			} else {Draw.drawFighter(fighters.get(i));}
		}
		if (deadFighter != null){
			fighters.remove(deadFighter);
			deadFighter = null;
		}
	}//updateProjectilesAndFighters method
	public void activatePowerup(Powerup pow){
//		System.out.println(t + " POWERUP ACTIVATED");
		Powerup.type t = pow.getType();
		if (t == Powerup.type.HEALTH){
			health+=50;
			if (health>MAXHEALTH)health = MAXHEALTH;
		} else if (t == Powerup.type.LASER){
			laser();
		} else if (t == Powerup.type.FIREWORK){
			firework(pow.getX(),pow.getY());
		} else if (t == Powerup.type.CLEAR){
			fighters.clear();
		}
		
	}//activatePowerup method
	public void laser(){
		int pX = 0;
		int pY = HEIGHT - 100;
		int speed = 12;
		for (int i=0;i<10;i++){
			projs.add(new Projectile(pX+10*i,pY-10*i,.8f,0,speed,0));
		}
	}//laser method
	public void firework(int x, int y){
		int speed = 4;
		int numProj = 20;
		for (int i=0;i<numProj;i++){
			int dA = 360/numProj;
			double fAngle = dA * i;
			if (fAngle >180){
				fAngle -= 360;
			}
			double evenX = Utilities.circleValue((float)angle,true);
			double evenY = Utilities.circleValue((float)angle,false);

			projs.add(new Projectile(x,y,evenX,evenY,speed,0));
		}
		
		
	}//firework method
	
	public void shoot(){
		double evenX = Utilities.circleValue((float)angle,true);
		double evenY = Utilities.circleValue((float)angle,false);
		
		int mouseX = Mouse.getX();
		int mouseY = HEIGHT-Mouse.getY();
		int speed = (int)Math.hypot(mouseX-x, mouseY-y)/18;
		if (speed>10)speed =40;
		
		shots++;
		projs.add(new Projectile(x,y,evenX,evenY,speed,-1));
		
	}//shoot method
	
	public void start(){
		initGL();
		Font awtFont = new Font("Times new Roman",Font.BOLD,18);
		print = new TrueTypeFont(awtFont,false);
		printHeight = print.getHeight("T");	
		
		display = new Notify();
		
		while (!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
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
			Display.setTitle("BowMaster ["+WIDTH+","+HEIGHT+"]");
			Display.setInitialBackground(255, 255, 00);
			Display.create();
			///////^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^/////////////
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);


		
	}//initGL method
	public static void main(String Args[]){
		BowMaster game = new BowMaster();
		game.start();
	}//main method
}//Main class
