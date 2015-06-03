package Utilities;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;


public class OpenGlSetup {

	private final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	
	
	public void render(){
		
		
	}//render method
	public void start(){
		initGL();
		
		while(!Display.isCloseRequested()){
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
			GL11.glDisable(GL_BLEND);
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0,WIDTH,HEIGHT,0,1,-1);
			glMatrixMode(GL_MODELVIEW);
	}//initGL method
	public static void main(String Args[]){
		OpenGlSetup app = new OpenGlSetup();
		app.start();
	}//main method
	
}
