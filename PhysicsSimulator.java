import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class PhysicsSimulator {
	
	public static Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = (int)(dm.getWidth());
	public static final int HEIGHT = (int)(dm.getHeight());
	public static final int GROUND = (int)(HEIGHT-(HEIGHT*.14));

	public static double x = 100;
	public static int width = 60;
	public static double y = 500;
	public static int height = 100;
	
	
	public static double u=12000; //(+) = right
	public static double v=12000;//(-) = up
	public static double d=0.001;
	public static double g=10000;
	public static double h=0.7;
	
	
	
	public static Obstacle objects[];
	
	public static final int X_INC = 5;
	public static final int sf1 = 1;
	public static final int sf2 = 2;
	public static int sf = sf1;
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;

	public static final int jumpTime = 2000;
	public static boolean jumped = false;
	
	
	public static void main (String Args[]){
		
		try {
			//initiate display
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Henry's Game ["+WIDTH+", "+HEIGHT+"]");
//			Display.setLocation(500, 300);
			Display.setInitialBackground(00, 255, 200);
			
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		
		objects = new Obstacle[4];
		objects[0] = new Obstacle(500,200);
		objects[1] = new Obstacle(50,120);
		objects[2] = new Obstacle(800,500);	
		objects[3] = new Obstacle(900,600);
		
		while (!Display.isCloseRequested()){
			//render
			//https://www.opengl.org/sdk/docs/man2/
			//^^^^good information^^^^//
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Display.destroy();
				System.exit(0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
				sf = sf2;
			} else {sf = sf1;}
			
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) && checkDirection(UP)){
				/*
				new Thread(){
					public void run(){
						if (!jumped){
							jumped=true;
							
							y-=50;
							try {
								Thread.sleep(20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							y-=50;
							
							try {
								Thread.sleep(jumpTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							jumped=false;
						}//if statement
					}//run()
				}.start();
				*/
				y-=5*sf;
				
				
			} if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && checkDirection(DOWN)){
				
				if (y<GROUND-height)y+=6*sf;
				
			} if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && checkDirection(RIGHT)){
				
				x+=X_INC*sf;
				
			} if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && checkDirection(LEFT)){
				
				x-=X_INC*sf;
				
			}

//			if (y<GROUND-height && checkDirection(DOWN)) y+=5;

			Draw.drawImage((int)x, (int)y, 100, "GingerbreadMan");//Character
			Draw.drawRect(0,WIDTH,GROUND,HEIGHT-GROUND,.5f,.8f,0);//Ground
			
			for (int i = 0; i<objects.length;i++){
				Draw.drawObstacle(objects[i]);
			}
			
			x = x + (d*u);
			y = y + (d*v);
//			v = v + (d*g);
			
			if (v<0 && !checkDirection(UP)){
				v = -1*v*h;
			} else if (v>0 && !checkDirection(DOWN)){
				v = -1*v*h;
			}

			if (u>0 && !checkDirection(RIGHT)){
				u = -1*u*h;
			} else if (u<0 && !checkDirection(LEFT)){
				u = -1*u*h;
			}
			
//			if (Mouse.isButtonDown(button))
			
			
			
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		
		
	}//main method
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
				if (y >= GROUND-height) areaIsOpen = false;
				break;
			case 2: //If RIGHT is being checked
				if ( isBetween((int)y,centerY,halfHeight) || isBetween((int)y+height,centerY,halfHeight) || isBetween(locY,((int)y+height/2),height/2) || isBetween(locY+objHeight,((int)y+height/2),height/2)){
					if (isBetween((int)x+width,locX,lz)) areaIsOpen = false;
				}
				if (x >= WIDTH-60) areaIsOpen = false;
				break;
			case 3: //If LEFT is being checked
				if ( isBetween((int)y,centerY,halfHeight) || isBetween((int)y+height,centerY,halfHeight) || isBetween(locY,(int)(y+height/2),height/2) || isBetween(locY+objHeight,((int)y+height/2),height/2)){
					if (isBetween((int)x,locX+objWidth,lz)) areaIsOpen = false;
				}
				if (x<=0) areaIsOpen = false;
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
