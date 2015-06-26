package Main;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class BallPhysics {

	private Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	
	private final int WIDTH = (int)dm.getWidth();
	private final int HEIGHT = (int)dm.getHeight();
	
	
	private int xArray[] = new int[WIDTH];
	private float yArray[] = new float[WIDTH*2];
	private Ball ball;
	

	public void render(){
		if (Mouse.isButtonDown(0) && Mouse.getX()>31 && Mouse.getX()<WIDTH-31){
			for (int i=Mouse.getX()-30;i<Mouse.getX()+30;i++){
				xArray[i] = Mouse.getX();
				yArray[i] = HEIGHT - Mouse.getY();
			}
		}
		while(Keyboard.next()){
			if (Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) ball.isPaused= !ball.isPaused;
				if (Keyboard.getEventKey() == Keyboard.KEY_R) ball.reset();
				
			}
		}
		
		displayTerrain();
		
		ball.display();
		ball.updateY(yArray);
//		ball.updateLoc();
		ball.newUpdate();
//		ball.trace();
	}//render method
	
	
	public void displayTerrain(){
		
		glColor3f(1,0,0);
		glBegin(GL_QUAD_STRIP);
			glVertex2i(0,400);
			glVertex2i(0,410);
			
			for (int i=0;i<WIDTH;i++){
				glVertex2i(xArray[i],(int)yArray[i]);
				glVertex2i(xArray[i],(int)yArray[i]+8);
			}
		
		glEnd();
		
	}//display method

	public void clearArrays(){
		for (int i=0;i<WIDTH;i++){
			xArray[i] = i;
			yArray[i] = 400;
		}
	}//clearArrays method
	
	public void start(){
		initGL();
		clearArrays();
		
		ball = new Ball(xArray,yArray);
		ball.isPaused = true;
		
		
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
			Display.setInitialBackground(0, 255, 100);
			Display.create();
		} catch (LWJGLException e){
			e.printStackTrace();
		}

		
		GL11.glEnable(GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_BLEND);
			
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
	}//initGL method
	public static void main(String Args[]){
		BallPhysics app = new BallPhysics();
		app.start();
	}//main method
	
}
