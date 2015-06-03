package Main;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import Utilities.Draw;
import Utilities.Functions;


public class FightingGame {
	
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
	public boolean endGame = false;
	
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
			if (Keyboard.isKeyDown(Keyboard.KEY_Y))state = State.MAIN_MENU;
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
					drawShip(0,1,0);
				} else if (ships.indexOf(ship)==1){
					drawShip(0,0,1);
				} else {
					drawShip(1,0,0);
				}
				
				GL11.glPopMatrix();
				
			if (ship.getRotateAngle()>=180){
				ship.setRotateAngle(-179.99f);
			}else if (ship.getRotateAngle()<=-180)ship.setRotateAngle(179.99f);
			
			}//for loop through ships array

			updateHealth();
			
			switch(playerNum){
			case 1:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(Projectile.projType.BULLET,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_RMENU) && autoFire) shoot(Projectile.projType.BOMB,0);
				
				break;
			case 2:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(Projectile.projType.BULLET,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_RMENU) && autoFire) shoot(Projectile.projType.BOMB,0);

				////^^^^^PLAYER 1^^^^^^^/////vvvvvPlayer 2vvvvvv//////
				if (Keyboard.isKeyDown(Keyboard.KEY_W))drive(12,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_S))drive(-8,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_A))ships.get(1).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_D))ships.get(1).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_F) && autoFire) shoot(Projectile.projType.BULLET,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_G) && autoFire) shoot(Projectile.projType.BOMB,1);

				break;
			case 3:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(Projectile.projType.BULLET,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_RMENU) && autoFire) shoot(Projectile.projType.BOMB,0);
				////^^^^^PLAYER 1^^^^^^^///
				if (Keyboard.isKeyDown(Keyboard.KEY_U))drive(12,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_J))drive(-8,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_H))ships.get(1).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_K))ships.get(1).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_P) && autoFire) shoot(Projectile.projType.BULLET,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_LBRACKET) && autoFire) shoot(Projectile.projType.BOMB,1);
				//^^^^^^Player 2^^^^^^//////
				if (Keyboard.isKeyDown(Keyboard.KEY_W))drive(12,2);
				if (Keyboard.isKeyDown(Keyboard.KEY_S))drive(-8,2);
				if (Keyboard.isKeyDown(Keyboard.KEY_A))ships.get(2).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_D))ships.get(2).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_F) && autoFire) shoot(Projectile.projType.BULLET,2);
				if (Keyboard.isKeyDown(Keyboard.KEY_G) && autoFire) shoot(Projectile.projType.BOMB,2);
				//^^^^^Player 3^^^^^^///////
				
				break;
			default:
				System.exit(0);
				break;
			}
			
			while(Keyboard.next()){
				if (!autoFire){
					if (Keyboard.getEventKeyState()) {
						if (playerNum==1){ 
							if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(Projectile.projType.BULLET,0);
							if (Keyboard.getEventKey() == Keyboard.KEY_RMENU)shoot(Projectile.projType.BOMB,0);
						}
						if (playerNum==2){
							if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(Projectile.projType.BULLET,0);
							if (Keyboard.getEventKey() == Keyboard.KEY_RMENU)shoot(Projectile.projType.BOMB,0);
							
							if (Keyboard.getEventKey() == Keyboard.KEY_F)shoot(Projectile.projType.BULLET,1);
							if (Keyboard.getEventKey() == Keyboard.KEY_G)shoot(Projectile.projType.BOMB,1);
						}	
						if (playerNum==3){
							if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(Projectile.projType.BULLET,0);
							if (Keyboard.getEventKey() == Keyboard.KEY_RMENU)shoot(Projectile.projType.BOMB,0);
							
							if (Keyboard.getEventKey() == Keyboard.KEY_SEMICOLON)shoot(Projectile.projType.BULLET,1);
							if (Keyboard.getEventKey() == Keyboard.KEY_APOSTROPHE)shoot(Projectile.projType.BOMB,1);

							if (Keyboard.getEventKey() == Keyboard.KEY_F)shoot(Projectile.projType.BULLET,2);
							if (Keyboard.getEventKey() == Keyboard.KEY_G)shoot(Projectile.projType.BOMB,2);

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
								if (playerNum == 2){
									ship.addToHealth(-1);

									updateHealth();
								
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									winningShip = ship;
									endGame = true;
								} else if (playerNum == 3){
									playerNum = 2;
									
								}
							} else{
							ship.addToHealth(-1);
							}
						}
					
					}//for loop of ships
					
					if (removeProj == null)Draw.drawProj(p);
				
				}//else statement
			}//for loop of projectiles
			
			if (removeProj!=null){
				projs.remove(removeProj);
				removeProj = null;
			}
			if (endGame){
				state = State.END_GAME;
				endGame();
			}
			
			
			
//			displayText = 
//					"RotateAngle: " + rotateAngle +
//					"ProjNum: " + projs.size() +
//					"" +
//					"";
//			Draw.drawString(font, displayText, WIDTH/2, HEIGHT/2, Color.magenta);
	} //game method
	public void endGame(){
		endGame = false;
		String winningSide = (ships.indexOf(winningShip)==0 ? "LEFT":"RIGHT");
		String winner = "Player on the "+winningSide+" wins!!!";
		Draw.drawString(titleFont, winner, WIDTH/2, HEIGHT/3, Color.red);
		Draw.drawString(font, "Press [r] to play again", WIDTH/2, HEIGHT/2, Color.darkGray);
		
		heal();
	}//endGame method
	public void heal(){
		for (FighterShip ship:ships){
			ship.setHealth(ship.MAXHEALTH);
			if (playerNum == 1){
				ship.setX(WIDTH/3);
			} else if (playerNum == 2){
				ship.setX(WIDTH-(500+(920*ships.indexOf(ship))));
			} else if (playerNum == 3){
				ship.setX(WIDTH-((WIDTH/3)*ships.indexOf(ship)));
			}
			ship.setRotateAngle(-90);
			ship.setY(400);
		}
		projs.clear();
	}//heal method
	
	
	public void shoot(Projectile.projType type,int shipNum){
		double evenX = Functions.circleValue(ships.get(shipNum).getRotateAngle(), true);
		double evenY = Functions.circleValue(ships.get(shipNum).getRotateAngle(), false);
		
		switch (type){
		case BULLET:
			int bulletSpeed = 12;
			projs.add(new Projectile(ships.get(shipNum).getX(),ships.get(shipNum).getY(),evenX,evenY,bulletSpeed,shipNum));
			break;
		case BOMB:
			int bombSpeed = 6;
			float bombX = (float) evenX/4;
			float bombY = .8f;
			projs.add(new Projectile(ships.get(shipNum).getX(),ships.get(shipNum).getY(),bombX,bombY,bombSpeed,shipNum));
			break;
			
		}
//		projs.add(new Projectile(x,y,evenX,evenY,speed,ships.));
		
	}//shoot method
	public void drive(int mag,int shipNum){
//		float percentX = valueX(ships.get(shipNum).getRotateAngle());
//		float percentY = valueY(ships.get(shipNum).getRotateAngle());
//		float evenX = (float) (percentX/Math.hypot(percentX,percentY));
//		float evenY = (float) (percentY/Math.hypot(percentX, percentY));
		double evenX = Functions.circleValue(ships.get(shipNum).getRotateAngle(), true);
		double evenY = Functions.circleValue(ships.get(shipNum).getRotateAngle(), false);
		ships.get(shipNum).addToX((float)evenX*mag);
		ships.get(shipNum).addToY((float)evenY*mag);
		
//		System.out.println("EvenX: "+evenX + " EvenY: "+evenY);
		
	}//drive method
	
	public void updateHealth(){
		GL11.glEnable(GL_BLEND);
		for (int i=0;i<ships.size();i++){
			Draw.drawString(font, ""+ships.get(i).getHealth(), (int)ships.get(i).getX(), (int)ships.get(i).getY(), Color.darkGray);
		}
	}//updateHealth method
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

	public void initGL(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Rotating Ship ["+WIDTH+", "+HEIGHT+"]");
			Display.setInitialBackground(00, 255, 255);
			
			Display.create();
			Keyboard.create();
			Keyboard.poll();
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
		FightingGame main = new FightingGame();
		main.start();
	}
	
}
