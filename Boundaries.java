import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Boundaries {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;

	public static int x = 100;
	public static int width = 60;
	public static int y = 200;
	public static int height = 100;
	
	public static int x2 = 0;
	public static int y2 = 0;
	
	public static Obstacle objects[];
	
	public static final int X_INC = 5;
	public static final int GROUND = 430;
	
	public static boolean up = true;
	public static boolean down = true;
	public static boolean right = true;
	public static boolean left = true;

	
	public static void main (String Args[]){
		
		try {
			//initiate display
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Hello");
			Display.setLocation(500, 300);
			Display.setInitialBackground(00, 255, 200);
			
			Display.create();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,800,500,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		
		objects = new Obstacle[1];
		objects[0] = new Obstacle();
		objects[0].setX(500);
		objects[0].setY(200);
		objects[0].setWidth(200);
		objects[0].setHeight(70);
		
		
		while (!Display.isCloseRequested()){
			//render
			//https://www.opengl.org/sdk/docs/man2/
			//^^^^good information^^^^//
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Display.destroy();
				System.exit(0);
			}
			
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) && up){
				y-=8;
			} if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && down){
				y+=10;
			} if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && right){
				x+=X_INC;
			} if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && left){
				x-=X_INC;
			}
			/*
			if (Keyboard.isKeyDown(Keyboard.KEY_W)){
				if (checkPush("up",200,100)){
					if (up){
						y-=5;
						y2-=5;
					} else{
						y2+=5;
					}
				}
				y2-=5;
			} if (Keyboard.isKeyDown(Keyboard.KEY_S)){
				y2+=5;
			} if (Keyboard.isKeyDown(Keyboard.KEY_D)){
				if (checkPush("right",200,100)) x += X_INC;
				x2+=X_INC;
			} if (Keyboard.isKeyDown(Keyboard.KEY_A)){
				if (checkPush("left",200,100)) x -= X_INC;
				x2-=X_INC;
			}
			*/
			
			if (y<GROUND-height && down) y+=5;
			clearDirs();

			regularBounds();
//			Draw.gingerBreadMan(x, y, 100);
			Draw.drawImage(x, y, 100, "GingerbreadMan");//Character
			Draw.drawRect(0,WIDTH,HEIGHT-70,70,.5f,.8f,0);//Ground
			
//			Draw.drawRect(x2, 200, y2, 100, .8f, 0, 0);// Obstacle
//			checkBoundaries(x2,200,y2,100);
			
			for (int i = 0; i<objects.length;i++){
				Draw.drawObstacle(objects[i]);
			}
			
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		
		
	}//main method
	public static boolean checkPush(String dir,int objWidth, int objHeight){
		boolean objThere = false;
		
		int halfWidth = objWidth/2;
		int halfHeight = objHeight/2;
		int lz = 5;
		
		switch(dir){
		case "up":
			if (isBetween(x,x2+halfWidth,halfWidth+lz) || isBetween(x+width,x2+halfWidth,halfWidth+lz)){
				if (isBetween(y+height,y2,lz)){
					objThere = true;
				}
			}
			
			
			break;
		case "down":
			
			break;
		case "right":
			
			break;
		case "left":
			
			break;
		}//switch statement
		
		return objThere;
		
		
	}//checkPush method
	public static void regularBounds(){
		
		if (y <= 0) up = false;//Top of Display
		if (y >= GROUND-height) down = false;//Ground of Display
		if (x >= WIDTH-60) right = false;//Right side of Display
		if (x<=0) left = false; //Left side of Display
		
	}//regularBounds method
	public static void checkBoundaries(int locX, int objWidth, int locY, int objHeight){
		int lz = 5; //leeway-zone
		
		int halfWidth = objWidth/2;
		int halfHeight = objHeight/2;
		int centerX = locX+(objWidth/2);
		int centerY = locY+(objHeight/2);		
		
		if (up){
			if (isBetween(x,centerX,halfWidth+lz) || isBetween(x+width,centerX,halfWidth+lz)){
				if (isBetween(y,locY+objHeight,lz)) up = false;	
			}
		}
		
		if (down){
			if (isBetween(x,centerX,halfWidth+lz) || isBetween(x+width,centerX,halfWidth+lz)){
				if (isBetween(y+height,locY,2*lz)) down = false;	
			}
		}
		
		if (isBetween(y,centerY,halfHeight) || isBetween(y+height,centerY,halfHeight) || isBetween(locY,(y+height/2),height/2) || isBetween(locY+objHeight,(y+height/2),height/2)){
			if (left){
				if (isBetween(x,locX+objWidth,lz)){
					left = false;
//					print("Left="+left);
				}
			}
			if (right){
				if (isBetween(x+width,locX,lz)){
					right = false;
//					print("Right=" + right);
				}
			}
		}
		
	}//checkBoundaries method
	public static void clearDirs(){
		up=true;
		down=true;
		right=true;
		left=true;
	}//clearDirs method
	public static boolean isBetween(int testLoc,int targetLoc,int variation){
		boolean isCloseTo = false;
		
		if (targetLoc-variation <= testLoc && testLoc <= targetLoc+variation){
			isCloseTo = true;
		}
		
		return isCloseTo;
	}//isCloseTo method
	
	public static void print(String text){
		System.out.println("[INFO]: "+text);
	}//print method
	
	
}//WindowTest class
