package Main;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Waves {

	private final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//	private final int WIDTH = 800;
	private final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//	private final int HEIGHT = 500;
	
	private int x = WIDTH/8;
	private int y = HEIGHT/2;
	private int mouseX;
	
	private final int XNUM = 100;
	
	private float yArray[] = new float[XNUM];
	private float vArray[] = new float[XNUM];
	private float temp[] = new float[XNUM];
	private boolean update = true;
	
	private int space = (WIDTH-2*x)/XNUM;
	
	public void render(){
		
		
		if (Mouse.getX()>x && Mouse.getX()<WIDTH-x){
			int z = Mouse.getX()-x;
			mouseX = z/space;
		}
		if (Mouse.isButtonDown(0)){
			yArray[mouseX] = HEIGHT-Mouse.getY();
		}
		
		while(Keyboard.next()){
			if (Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey()==Keyboard.KEY_C)update = !update;
			}
		}
		

		display();
		update();
		
	}//render method
	
	public void display(){
		
		glColor3f(1,0,0);
		glBegin(GL_QUAD_STRIP);
			
			glVertex2i(x,y);
			glVertex2i(x,y+5);
			for (int i=0;i<yArray.length;i++){
				glVertex2f(x+space*i,yArray[i]);
				glVertex2f(x+space*i,yArray[i]+5);
				
			}
		glEnd();
		
	}//display method
	public void update(){
		
		float d = 0.05f;
		float f = 1.f;
		float g = 0.999f;
		float s = 0.9f;

		for (int i=1;i<yArray.length-1;i++){
			vArray[i] += f * ( yArray[i-1] + yArray[i+1] - 2 * yArray[i] );
			vArray[i] *= g;

		}
		
		for (int i=1;i<yArray.length-1;i++){
			temp[i] = (yArray[i] += vArray[i]*d);
		}
		for (int i=1;i<yArray.length-1;i++){
			yArray[i] = s * temp[i] + (1-s)*(temp[i-1] + temp[i+1])/2;
		}
		
	}//update method
	public void clearArray(){
		for (int i=0;i<yArray.length;i++){
			yArray[i] = HEIGHT/2;
		}
	}//loadArray method

	public void start(){
		initGL();
		clearArray();
		
		while(!Display.isCloseRequested()){
			if (update)glClear(GL_COLOR_BUFFER_BIT);

			render();
			
			
			Display.update();
			Display.sync(100);
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

		glEnable(GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_BLEND);
			
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
	}//initGL method
	public static void main(String Args[]){
		Waves app = new Waves();
		app.start();
	}//main method
	
}
