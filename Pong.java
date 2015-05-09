import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


@SuppressWarnings("deprecation")
public class Pong {
	
	public static Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = (int)(dm.getWidth());
	public static final int HEIGHT = (int)(dm.getHeight());
	public static boolean isPaused = true;
	public static boolean debugMode = true;
	public static int debugX = 120;
	public static TrueTypeFont debugFont;
	public static int debugHeight;
	public static TrueTypeFont infoFont;
	public static int infoHeight;
	
	public static double x = 100;
	public static int width = 60;
	public static double y = 500;
	public static int height = 100;
	
	public static final double MAX_U = 30000;
	public static final double MAX_V = 11000;
	public static double u=10000; //(+) = right
	public static double v=000;//(+) = down
	public static double d=0.001;//diminishes uv effect
	public static double h=1.01;//u inc on hit
	
	public static int leftX = 0;
	public static int leftY = 50;
	
	public static int rightX = WIDTH-30;
	public static int rightY = 50;
	
	public static int speed = 15;
	public static int speed2 = 20;
	
	public static double rOldY = rightY;
	public static double lOldY = leftY;
	public static double rDeltaY = 0;
	public static double lDeltaY = 0;
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	
	
	public static List<Obstacle> objects;
	public static Random randomGen = new Random();
	
	public static final int X_INC = 5;
	
	public static boolean closeRequested = false;
	
	@SuppressWarnings("deprecation")
	public static void main (String Args[]){
		
		try {
			//initiate display
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Henry's Game ["+WIDTH+", "+HEIGHT+"]");
//			Display.setLocation(500, 300);
			Display.setInitialBackground(00, 255, 255);
			
			Display.create();
			GL11.glEnable(GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
//		GL11.glEnable(GL11.GL_TEXTURE_2D);

		
	    
	    glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		
		
		
		objects = new ArrayList<Obstacle>();
		objects.add(new Obstacle(leftX,leftY));
		objects.get(0).setWidth(30);
		objects.get(0).setHeight(HEIGHT/3);
		objects.add(new Obstacle(rightX,rightY));
		objects.get(1).setWidth(30);
		objects.get(1).setHeight(HEIGHT/3);
		
		int time = (int) System.currentTimeMillis();
		
		Font awtDebugFont = new Font("Times new Roman", Font.PLAIN,24);
		debugFont = new TrueTypeFont(awtDebugFont,false);
		debugHeight = debugFont.getHeight("T");
		
		Font awtInfoFont = new Font("Times new Roman", Font.BOLD,48);
		infoFont = new TrueTypeFont(awtInfoFont,false);
		infoHeight = infoFont.getHeight("T");
		
		while (!closeRequested){//-------------------------------------------------------------------------------
			//render
			//https://www.opengl.org/sdk/docs/man2/
			//^^^^good information^^^^//
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Display.destroy();
				System.exit(0);
			}
			
			
			int currentTime = (int)System.currentTimeMillis();
			
			
			/////////////////PADDLE MOVEMENT//////////////////////////////////////////
			
			rOldY = rightY;//PART OF PANEL
			lOldY = leftY;//VELOCITY!!!!

			if (currentTime-time>15000) speed = speed2;
			if (Keyboard.isKeyDown(Keyboard.KEY_Q) && leftY >= 0)leftY-=speed;	
			if (Keyboard.isKeyDown(Keyboard.KEY_A) && leftY+objects.get(0).getHeight() <= HEIGHT)leftY+=speed;
			if (Keyboard.isKeyDown(Keyboard.KEY_RBRACKET) && rightY >= 0)rightY-=speed;
			if (Keyboard.isKeyDown(Keyboard.KEY_APOSTROPHE) && rightY+objects.get(1).getHeight() <= HEIGHT)rightY+=speed; 

			objects.get(0).setY(leftY);
			objects.get(1).setY(rightY);
			
			//////////////////RANDOM GENERATION//////////////////////////////////////
			int randX = randomGen.nextInt(WIDTH);
			int randY = randomGen.nextInt(HEIGHT);
			
			if (randomGen.nextInt(800)==0){
				objects.add(new Obstacle(randX,randY));
				objects.get(objects.size()-1).setWidth(50);
				objects.get(objects.size()-1).setHeight(50);
			} else if (randomGen.nextInt(800)==0){
				objects.add(new Obstacle(randX,randY));
				objects.get(objects.size()-1).setWidth(20);
				objects.get(objects.size()-1).setHeight(200);
//				objects.get(objects.size()-1).setColor(0,1,1);
			}
			////////////////////RECTANGLE DRAWING//////////////////////////////////
			Draw.drawImage((int)x, (int)y, 100, "GingerbreadMan");//Character
			
			for (int i = 0; i<objects.size();i++){
				Draw.drawObstacle(objects.get(i));
			}
			//////////////////////PANEL VELOCITY////////////////////////////////////
			rDeltaY = rOldY-rightY;
//			print("rDeltaY="+(rOldY-rightY));
			lDeltaY = lOldY-leftY;
//			print("lDeltaY="+(lOldY-leftY));
			//////////////////////VELOCITY CONTROL///////////////////////////////////

//			print("U1: "+u);
			if (Math.abs(u)>MAX_U){
				if (u<0){
					u=-MAX_U;
				} else{u=MAX_U;}
			}
//			print("U2: "+u);
			if (Math.abs(v)>MAX_V){
				if (v<0){
					v=-MAX_V;
				} else{v=MAX_V;}
			}
			
			x = x + (d*u);
			y = y + (d*v);
			
			if (v<0 && !checkDirection(UP)){
				v = -1*v;
			} else if (v>0 && !checkDirection(DOWN)){
				v = -1*v;
			}

			if (u>0 && !checkDirection(RIGHT)){
				u = -1*u*h;
				v = v + (-100*rDeltaY);
//				print("rY = "+rDeltaY);
			} else if (u<0 && !checkDirection(LEFT)){
				u = -1*u*h;
				v = v + (-200*lDeltaY);
//				print("lY = "+lDeltaY);
			}
//			print("v = "+v);
			////////////////////////PAUSE MENU////////////////////////////////////
			
			if (Keyboard.isKeyDown(Keyboard.KEY_P)) isPaused=true;
			if (isPaused){
				while(true){
					if (Keyboard.isKeyDown(Keyboard.KEY_U)){
						isPaused=false;
						break;
					}
					GL11.glClear(GL_COLOR_BUFFER_BIT);
					
					Draw.drawString(debugFont, "Q=Up",WIDTH/5,HEIGHT/3,Color.black);
					Draw.drawString(debugFont, "A=Down",WIDTH/5,(HEIGHT/3)+debugHeight,Color.black);
					
					Draw.drawString(infoFont, "PAUSED", WIDTH/2, HEIGHT/2, Color.magenta);
					Draw.drawString(debugFont, "(Press [u] to resume)",WIDTH/2, HEIGHT/2 + infoHeight,Color.lightGray);

					Draw.drawString(debugFont, "]=Up",4*WIDTH/5,HEIGHT/3,Color.black);
					Draw.drawString(debugFont, "'=Down",4*WIDTH/5,(HEIGHT/3)+debugHeight,Color.black);
					
					Draw.drawString(infoFont, "'Pong' remake", WIDTH/2, HEIGHT/4, Color.red);
					Draw.drawString(debugFont, "by HenryLoh", WIDTH/2, HEIGHT/4+infoHeight, Color.pink);
					
					Display.update();
					Display.sync(60);
				}
			}

			
			///////////////////DEBUG MODE//////////////////////////////////
			if (debugMode){
				String debugText = "Debug Values: "
						+"u = "+u
						+" v = "+v
						+" DeltaY = "+lDeltaY+" : "+rDeltaY
						
						;			

				
				debugFont.drawString(debugX, 0,debugText,Color.magenta);

				debugX-=1;
				if (debugX+debugFont.getWidth(debugText)<0)debugX = WIDTH+50;
			}
			/////////////////////////END///////////////////////////////////
			if (x<=0){
				end("RIGHT",(int)(currentTime-time)/1000);
				closeRequested=true;
			} else if (x+width>=WIDTH){
				end("LEFT",(int)(currentTime-time)/1000);
				closeRequested=true;
			}else {
			Display.update();
			Display.sync(60);
			}
		}//render loop
		Display.destroy();
		
		
	}//main method
	public static void end(String winSide,int time){
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			try{
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Draw.drawString(infoFont,"GAME OVER",WIDTH/2,HEIGHT/2,Color.orange);
				Display.update();
				Thread.sleep(1500);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Draw.drawString(infoFont,"Player on the "+winSide+" wins!!!",WIDTH/2,HEIGHT/2,Color.pink);
				Display.update();
				Thread.sleep(1500);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Draw.drawString(infoFont,"The game took roughly "+time+" seconds.",WIDTH/2,HEIGHT/2,Color.darkGray);
				Display.update();
				Thread.sleep(1500);
			} catch(InterruptedException e){e.printStackTrace();}
			
		}//while loop
		
		
	}//end method
	public static boolean checkDirection(int direction){
		boolean areaIsOpen = true;
		
		int lz = 5; //leeway-zone
		
		for (Obstacle o:objects){
			int locX = o.getX();
			int locY = o.getY();
			int objWidth = o.getWidth();
			int objHeight = o.getHeight();
			int halfWidth = objWidth/2;
			int halfHeight = objHeight/2;
			int centerX = locX+(objWidth/2);
			int centerY = locY+(objHeight/2);
			
			switch (direction){
		
			case 0: //If UP is being checked
				if (isBetween((int)x,centerX,halfWidth+lz) || isBetween((int)x+width,centerX,halfWidth+lz)){
					if (isBetween((int)y,locY+objHeight,lz)) areaIsOpen = false;
				}
				if (y <= 0) areaIsOpen = false;
				break;
			case 1: //If DOWN is being checked
				if (isBetween((int)x,centerX,halfWidth+lz) || isBetween((int)x+width,centerX,halfWidth+lz)){
					if (isBetween((int)y+height,locY,2*lz)) areaIsOpen = false;	
				}
				if (y+height >= HEIGHT-30) areaIsOpen = false;
				break;
			case 2: //If RIGHT is being checked
				if ( isBetween((int)y,centerY,halfHeight) || isBetween((int)y+height,centerY,halfHeight) || isBetween(locY,((int)y+height/2),height/2) || isBetween(locY+objHeight,((int)y+height/2),height/2)){
					if (isBetween((int)x+width,locX+objWidth,objWidth)) areaIsOpen = false;
				}
				break;
			case 3: //If LEFT is being checked
				if ( isBetween((int)y,centerY,halfHeight) || isBetween((int)y+height,centerY,halfHeight) || isBetween(locY,(int)(y+height/2),height/2) || isBetween(locY+objHeight,((int)y+height/2),height/2)){
					if (isBetween((int)x,locX,objWidth)) areaIsOpen = false;
				}
				break;
			default:
				return true;
			}//switch statement
		}//for loop
		return areaIsOpen;
	}//checkDirection method
	public static boolean isBetween(int testLoc,int targetLoc,int variation){
		boolean isCloseTo = false;
		
		if (targetLoc-variation <= testLoc && testLoc <= targetLoc+variation){
			isCloseTo = true;
		}
		
		return isCloseTo;
	}//isCloseTo method
	
	public static void wait(int time){
		try{TimeUnit.MILLISECONDS.sleep(time);
		} catch(InterruptedException e){e.printStackTrace();}
	}//wait method
	public static void print(String text){
		System.out.println("[INFO]: "+text);
	}//print method
	
	
}//WindowTest class
