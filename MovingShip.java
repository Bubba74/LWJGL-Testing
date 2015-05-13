import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class MovingShip {
	
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int WIDTH = (int)d.getWidth();
	public int HEIGHT = (int)d.getHeight();
	public boolean isPaused = true;
	
	public enum State {
		MAIN_MENU, CONTROLS, GAME, END_GAME;
	}
	public State state = State.MAIN_MENU;
	
	public float r = 1f;
	public float g = 0f;
	public float b = 0f;
	
	public String displayText = "";
	
	public List<Projectile> projs = new ArrayList<Projectile>();
	public int projNum = 0;
	public List<FighterShip> ships = new ArrayList<FighterShip>();
	public int playerNum = 2;
	public FighterShip winningShip;
	
	public boolean autoFire = false;
	
	public TrueTypeFont font;
	public int fontHeight;
	public TrueTypeFont titleFont;
	public int titleFontHeight;
	
	
	public void start(){
		initGL();
		
		Font awtFont = new Font("Times new Roman",Font.BOLD,36);
		font = new TrueTypeFont(awtFont,false);
		fontHeight = font.getHeight("T");
		
		Font awtFont2 = new Font("Times new Roman",Font.BOLD,48);
		titleFont = new TrueTypeFont(awtFont2,false);
		titleFontHeight = titleFont.getHeight("T");
		
		for (int i=0;i<playerNum;i++){
			ships.add(new FighterShip());
		}
		
		heal();
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			
			checkInput();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			
			Display.update();
			Display.sync(60);
		}//while loop
		Display.destroy();
	}//start method
	public void checkInput(){
		switch(state){
		
		case MAIN_MENU:
			if (Keyboard.isKeyDown(Keyboard.KEY_C))state = State.CONTROLS;
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN))state = State.GAME;
//			System.out.println("Checking... "+state);
			break;
		case CONTROLS:
			if (Keyboard.isKeyDown(Keyboard.KEY_M))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
			break;
		case GAME:
			if (Keyboard.isKeyDown(Keyboard.KEY_M))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
			break;
		case END_GAME:
			if (Keyboard.isKeyDown(Keyboard.KEY_R))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
		}
//		System.out.println("Switch");

	}//checkInput method
	public void render(){
		switch(state){
			case MAIN_MENU:
//				System.out.println(""+state);
				mainMenu();
				break;
			case CONTROLS:
//				System.out.println(""+state);
				controls();
				break;
			case GAME:
				game();
//				System.out.println(""+state);
				break;
			case END_GAME:
				endGame();
//				System.out.println(""+state);
				break;
		}
		
	}//render method
	
	public void mainMenu(){
		Draw.drawString(titleFont, "BATTLE", WIDTH/2, HEIGHT/3, Color.magenta);
		Draw.drawString(font, "by Henry Loh", WIDTH/2, HEIGHT/3 +10+ titleFontHeight, Color.magenta);
		Draw.drawString(font, "Press: ", WIDTH/2, HEIGHT/2, Color.blue);
		Draw.drawString(font, "[c] for controls", WIDTH/2, HEIGHT/2+fontHeight, Color.gray);
		Draw.drawString(font, "enter to continue", WIDTH/2, HEIGHT/2+2*(fontHeight), Color.gray);
	}//mainMenu method
	public void controls(){
		Draw.drawString(font, "Press [m] to return to main menu", WIDTH/2, HEIGHT/3+fontHeight, Color.gray);
	}//controls method
	
	public void game(){

			GL11.glDisable(GL_BLEND);
			for (FighterShip ship:ships){
				GL11.glColor3f(1, 0, 0);
				
				GL11.glPushMatrix();
					GL11.glLoadIdentity();
					GL11.glTranslatef(ship.getX(), ship.getY(), 0);
					GL11.glRotatef(ship.getRotateAngle(), 0.0f, 0.0f, 1.0f);
					
					if (ships.indexOf(ship)==0){
					drawShip(1,1,0);
					} else {
						drawShip(1,0,1);
					}
					
				GL11.glPopMatrix();
				
			if (ship.getRotateAngle()>=180){
				ship.setRotateAngle(-179.99f);
			}else if (ship.getRotateAngle()<=-180)ship.setRotateAngle(179.99f);
			
			}//for loop through ships array

			GL11.glEnable(GL_BLEND);
			for (int i=0;i<ships.size();i++){
				Draw.drawString(font, ""+ships.get(i).getHealth(), (int)ships.get(i).getX(), (int)ships.get(i).getY(), Color.green);
			}
			
			switch(playerNum){
			case 1:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(0);
				
				break;
			case 2:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(0);
				
				////^^^^^PLAYER 1^^^^^^^/////vvvvvPlayer 2vvvvvv//////
				if (Keyboard.isKeyDown(Keyboard.KEY_W))drive(12,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_S))drive(-8,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_A))ships.get(1).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_D))ships.get(1).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_F) && autoFire) shoot(1);
				
				break;
			default:
				System.exit(0);
				break;
			}
			
			while(Keyboard.next()){
				if (!autoFire){
					if (Keyboard.getEventKeyState()) {
						if (playerNum>0){ 
							if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(0);
						}
						if (playerNum>1){
							if (Keyboard.getEventKey() == Keyboard.KEY_F)shoot(1);
						}	
					}
				}
			}//while loop
			
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			if (Mouse.isButtonDown(0) && HEIGHT-Mouse.getY()>120) drawLine();
			
			Projectile removeProj = null;
			
			for(Projectile p:projs){
				if (p.getX()<0 || 
					p.getX()>WIDTH ||
					p.getY()<0 ||
					p.getY()>HEIGHT
					){
					removeProj = p;
				} else {
					
					
					for (FighterShip ship:ships){
						
						if (p.getShipNum() != ships.indexOf(ship)&&
								Math.abs(ship.getX()-p.getX())<40 &&
								Math.abs(ship.getY()-p.getY())<40
							){
							removeProj = p;
							if (ship.getHealth()==1){
								winningShip = ship;
								state = State.END_GAME;
							} else{
							ship.addToHealth(-1);
							}
						}
					
					}//for loop of ships
					
					if (removeProj == null)drawProj(p);
				
				}//else statement
			}//for loop of projectiles
			
			if (removeProj!=null){
				projs.remove(removeProj);
				removeProj = null;
			}
			
			
//			displayText = 
//					"RotateAngle: " + rotateAngle +
//					"ProjNum: " + projs.size() +
//					"" +
//					"";
//			Draw.drawString(font, displayText, WIDTH/2, HEIGHT/2, Color.magenta);
	} //game method
	public void endGame(){
		String winningSide = (ships.indexOf(winningShip)==0 ? "LEFT":"RIGHT");
		String winner = "Player on the "+winningSide+" wins!!!";
		Draw.drawString(titleFont, winner, WIDTH/2, HEIGHT/3, Color.red);
		Draw.drawString(font, "Press [r] to play again", WIDTH/2, HEIGHT/2, Color.darkGray);
		heal();
	}//endGame method
	public void heal(){
		for (FighterShip ship:ships){
			ship.setHealth(ship.MAXHEALTH);
			ship.setX(WIDTH-(500+(920*ships.indexOf(ship))));
			ship.setRotateAngle(-90);
			ship.setY(400);
		}
		projs.clear();
	}//heal method
	
	
	public void shoot(int shipNum){
		int speed = 12;
		
		float ratioX = valueX(ships.get(shipNum).getRotateAngle());
		float ratioY = valueY(ships.get(shipNum).getRotateAngle());
		float evenX =(float) (ratioX/Math.hypot(ratioX,ratioY));
		float evenY = (float) (ratioY/Math.hypot(ratioX, ratioY));
		
		projs.add(new Projectile(ships.get(shipNum).getX(),ships.get(shipNum).getY(),evenX,evenY,speed,shipNum));
//		projs.add(new Projectile(x,y,evenX,evenY,speed,ships.));
		
	}//shoot method
	public void drive(int mag,int shipNum){
		float percentX = valueX(ships.get(shipNum).getRotateAngle());
		float percentY = valueY(ships.get(shipNum).getRotateAngle());
		float evenX = (float) (percentX/Math.hypot(percentX,percentY));
		float evenY = (float) (percentY/Math.hypot(percentX, percentY));
		ships.get(shipNum).addToX(evenX*mag);
		ships.get(shipNum).addToY(evenY*mag);
		
//		System.out.println("EvenX: "+evenX + " EvenY: "+evenY);
		
	}//drive method
	
	public void drawProj(Projectile p){
		p.setX((float)(p.getX()+p.getDeltaX()*p.getSpeed()));
		p.setY((float)(p.getY()+p.getDeltaY()*p.getSpeed()));
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glColor3f(1, 1, 0);
		GL11.glTranslated(p.getX(), p.getY(), 0);
			
		GL11.glBegin(GL11.GL_POLYGON);
			GL11.glVertex2f(-10, 0);
			GL11.glVertex2f(-5, 5);
			GL11.glVertex2f(0, 10);
			GL11.glVertex2f(5, 5);
			GL11.glColor3f(0, 0, 0);
		
			GL11.glVertex2f(10, 0);
			GL11.glVertex2f(5, -5);
			GL11.glVertex2f(0, -10);
			GL11.glVertex2f(-5, -5);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}//drawProj method
	public void drawShip(float r, float g, float b){
		
		GL11.glColor3f(r, g, b);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2f(-50,0);
			GL11.glVertex2f(0,50);//Top-left
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(50,0);
			GL11.glVertex2f(0,50);//Top-right
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(50,0);
			GL11.glVertex2f(0,-50);//Bottom-right
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(-50,0);
			GL11.glVertex2f(0,-50);//Bottom-left
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(0, 50);
			GL11.glVertex2f(80, 0);//Top-nose
			GL11.glVertex2f(60, 0);
		
			GL11.glVertex2f(0, -50);
			GL11.glVertex2f(80, 0);//Bottom-nose
			GL11.glVertex2f(60, 0);
		
		GL11.glEnd();

	}//drawShip method
	public void drawLine(){
		int size = 2;
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glTranslatef(Mouse.getX(), HEIGHT-Mouse.getY(), 0);
		GL11.glColor3f(r, g, b);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
		
			GL11.glVertex2f(-5*size,0);
			GL11.glVertex2f(0,5*size);//Top-left
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(5*size,0);
			GL11.glVertex2f(0,5*size);//Top-right
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(5*size,0);
			GL11.glVertex2f(0,-5*size);//Bottom-right
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(-5*size,0);
			GL11.glVertex2f(0,-5*size);//Bottom-left
			GL11.glVertex2f(0,0);
			
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}//drawQuad method
	public void drawSettings(){
		GL11.glColor3f(1, 1, 0);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0,0);
			GL11.glVertex2f(WIDTH,0);
			GL11.glVertex2f(WIDTH,100);
			GL11.glVertex2f(0,100);
		GL11.glEnd();
		
		float colorChoices[][] = new float[2][3];
		
		colorChoices[0][0] =100;
		colorChoices[0][1] =0;
		colorChoices[0][2] =0;
		
		colorChoices[1][0] =0;
		colorChoices[1][1] =0;
		colorChoices[1][2] =100;
		
		for (int i=0;i<colorChoices.length;i++){
			drawBox(50*i+50,50,40,40,colorChoices[i][0],colorChoices[i][1],colorChoices[i][2]);
		}//for loop
		
		
		
	}//drawSettings method
	public void drawBox(float x, float y, float width, float height, float r, float g, float b){
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x+width, y);
			GL11.glVertex2f(x+width, y+height);
			GL11.glVertex2f(x, y+height);
		GL11.glEnd();
		
		
	}//drawBox method

	public float valueX(float angle){
		float angleX = angle;
		float percent;
		int mag = 1;
		
		if (angleX<0)angleX*=-1;
		if (angleX>90){
			angleX = (angleX - (2*(angleX-90)));
			mag *= -1;
		}
		percent = (90-angleX)/90;
		
		return percent*mag;
		
	}//valueX method
	public float valueY(float angle){
		float angleY = angle;
		if (angleY<-90){
			angleY = angleY + (2*(-90-angleY));
		}	
		if (angleY>90){
			angleY = angleY - (2*(angleY-90));
		}
		float percent = angleY/90;
		
		return percent;
	}//valueY method
	public void initGL(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Rotating Ship ["+WIDTH+", "+HEIGHT+"]");
			Display.setInitialBackground(00, 255, 255);
			
			Display.create();
	
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glEnable(GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
	}//initGL method
	public static void main(String Args[]){
		MovingShip main = new MovingShip();
		main.start();
	}
	
}
