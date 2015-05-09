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


public class WindowTest {
	
	public static boolean click = false;
	public static int x = 300;
	public static int y = 300;
	public static int objX = 400;
	public static int objY = 400;
	public static int randomX;
	public static int randomY;
	public static int score = -1;
	public static boolean randomBoxInit = false;
	
	public static void main (String Args[]){
		try {
			//initiate display
			Display.setDisplayMode(new DisplayMode(800,500));
			Display.setTitle("Hello");
			Display.setLocation(500, 300);
			Display.setInitialBackground(00, 255, 00);
			
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,800,500,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		double time = System.currentTimeMillis();
		
		while (System.currentTimeMillis()-time < 3000){
			//render
			//https://www.opengl.org/sdk/docs/man2/
			//^^^^good information^^^^//
			
			print(""+(System.currentTimeMillis()-time));
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				Display.destroy();
				System.exit(0);
			}
			
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
				y-=10;
			} if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				y+=10;
			} if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				x+=10;
			} if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				x-=10;
			}
			
			
			if (Keyboard.isKeyDown(Keyboard.KEY_X)){
				click = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_Z)){
				click = false;
			}
			
			
			if (isCloseTo(x,objX+25,35) && isCloseTo(y,objY+25,35)){
				if (click){
					objX = x;
					objY = y;
				}
			}
			
			Random randomGenerator = new Random();
			if (!randomBoxInit){
				randomX = randomGenerator.nextInt(80)*10;
				randomY = randomGenerator.nextInt(50)*10;
				score++;
				System.out.println("[INFO] Score: "+score);
				time=System.currentTimeMillis();
				randomBoxInit = true;
			}
			
			if (isCloseTo(objX,randomX,10) && isCloseTo(objY,randomY,10)) randomBoxInit = false;
			

//			GL11.glClearColor(0f, 0f, 0f, 0f);
//			GL11.glColor3f(1f,1f,0f);
//			glBegin(GL_QUADS); //random block
//			glVertex2i(randomX,randomY);
//			glVertex2i(randomX+100,randomY);
//			glVertex2i(randomX+100,randomY+100);
//			glVertex2i(randomX,randomY+100);
//			glEnd();
			
			Draw.drawImage(randomX, randomY, 100, "GingerbreadMan");
			
			GL11.glColor3f(0.0f,0.5f,1.0f);			
			glBegin(GL_QUADS); //pickup block
			glVertex2i(objX,objY);
			glVertex2i(objX+50,objY);
			glVertex2i(objX+50,objY+50);
			glVertex2i(objX,objY+50);
			glEnd();			
			
//			Draw.drawImage(objX, objY, 100, "Baker");
			
			GL11.glColor3f(1f,0f,0f);			
			glBegin(GL_QUADS); //cursor
			glVertex2i(x,y);
			glVertex2i(x+5,y);
			glVertex2i(x+5,y+5);
			glVertex2i(x,y+5);
			glEnd();
			
			
			
//			for (int i = 0;i<101;i++){
//				glBegin(GL_LINES);
//					glVertex2i(100,100+i);
//					glVertex2i(200,200-i);
//				glEnd();
//			}
			
			Display.update();
			Display.sync(60);
		}
		print("FINAL SCORE: "+score);
		Display.destroy();
		
		
	}//main method
	public static void print(String text){
		System.out.println("[INFO]: "+text);
	}//print method
	public static boolean isCloseTo(int testLoc,int targetLoc,int variation){
		boolean isCloseTo = false;
		
		if (targetLoc-variation <= testLoc && testLoc <= targetLoc+variation){
			isCloseTo = true;
		}
		
		return isCloseTo;
	}//isCloseTo method
	
	
}//WindowTest class
