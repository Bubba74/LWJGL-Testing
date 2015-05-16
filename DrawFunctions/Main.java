package DrawFunctions;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Main {
	/*
	 * This class is ment to test new draw functions.
	 * To utilize it, create a NEW class, and call it
	 * from the while update loop. This way each func-
	 * tion remains isolated and is easy to manipulate.
	 * This class also portrays the requirements to set
	 * up a new LWJGL environment. - Henry
	 */
	
	public static void main(String Ars[]){
		
		try {
			///////////SETS UP AND INITIATES DISPLAY///////////////
			
			Display.setDisplayMode(new DisplayMode(800,500));
			//Sets the display's 'displaymode' to the size (800,500)
			Display.setTitle("Hello");
			//Sets the display's title to "Hello"
			Display.setLocation(500, 300);
			//Sets the display's location to (500,300)
			Display.setInitialBackground(00, 255, 00);
			//Sets the display's background color to green(00,255,00)
			Display.create();
			//Creates the display
			//NOTE: THIS DOES NOT MAINTAIN THE DISPLAY
			//Use Display.update() in a while loop to
			//continue displaying the window.

			///////^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^/////////////
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,800,500,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
		
		
		
		while (!Display.isCloseRequested()){
			/////ALL DISPLAY FUNCTIONS GO HERE///////////////
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//			Draw.drawCircle(200, 200, 50);
//			Draw.drawImage(200, 200, 200, "GingerbreadMan");
			
			/////^^^^^^^^^^^^^^^^^^^^^^^^^^^^^///////////////
			
			
			
			Display.update();
			//Updates the display
			Display.sync(40);
			//Sets FPS to 40
		}

		Display.destroy();
		//Destroys the window
		System.exit(0);
		//Terminates program
		
	}//main method
}//Main class
