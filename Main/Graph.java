package Main;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Graph {

	private final int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private final int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//	private final int WIDTH = 800;
//	private final int HEIGHT = 500;
	private int oX = WIDTH/2;
	private int oY = HEIGHT/2;
	private final int ONE = 20;
	private List<Integer> tens = new ArrayList<Integer>();
	
	public float operation(float x){
		float ans = x;
		return ans;
	}//operation method

	public void render(){
		glPushMatrix();
			glTranslatef(oX,oY,0);
			
			traces();
			axis();
			graph();
			
		glPopMatrix();
	}//render method

	public void graph(){
		glColor3f(1,0,0);
		glBegin(GL_LINE_STRIP);
			for(int i=-oX*ONE;i<(WIDTH-oX)*ONE;i++){
				float x = i;
				x /= ONE;
//				System.out.print(x + " : ");
				float y = x*x;
				y /= ONE;
//				System.out.println(y);
				glVertex2f(x,-y);
			}
		glEnd();
		
		
	}//graph method
	public void traces(){
		glColor3f(0.8f,0.8f,0.8f);
		glBegin(GL_QUADS);
			for (float i = -WIDTH/ONE;i<WIDTH/ONE;i++){
				if (tens.contains((int)i)){
					glColor3f(0,1,0);
					
					glVertex2f((i*ONE)-1,HEIGHT-oY);
					glVertex2f((i*ONE)+1,HEIGHT-oY);
					glVertex2f((i*ONE)+1,-HEIGHT-oY);
					glVertex2f((i*ONE)-1,-HEIGHT-oY);
					
					glColor3f(0.8f,0.8f,0.8f);
				} else {
					glVertex2f((i*ONE)-1,HEIGHT-oY);
					glVertex2f((i*ONE)+1,HEIGHT-oY);
					glVertex2f((i*ONE)+1,-HEIGHT-oY);
					glVertex2f((i*ONE)-1,-HEIGHT-oY);
				}
			}
			for (int i = -HEIGHT/ONE;i<HEIGHT/ONE;i++){
				if (tens.contains((int)i)){
					glColor3f(0,1,0);
					
					glVertex2i(-WIDTH-oX,(i*ONE)-1);
					glVertex2i(WIDTH-oX,(i*ONE)-1);
					glVertex2i(WIDTH-oX,(i*ONE)+1);
					glVertex2i(-WIDTH-oX,(i*ONE)+1);
					
					glColor3f(0.8f,0.8f,0.8f);
				} else {
					glVertex2i(-WIDTH-oX,(i*ONE)-1);
					glVertex2i(WIDTH-oX,(i*ONE)-1);
					glVertex2i(WIDTH-oX,(i*ONE)+1);
					glVertex2i(-WIDTH-oX,(i*ONE)+1);
				}
			}
			
		glEnd();
	}//traces method
	public void axis(){
		glColor3f(0,0,0);
		glBegin(GL_QUADS);
			glVertex2i(-3,HEIGHT-oY);
			glVertex2i(3,HEIGHT-oY);
			glVertex2i(3,-HEIGHT-oY);
			glVertex2i(-3,-HEIGHT-oY);
		
			glVertex2i(-WIDTH-oX,3);
			glVertex2i(WIDTH-oX,3);
			glVertex2i(WIDTH-oX,-3);
			glVertex2i(-WIDTH-oX,-3);
		glEnd();
	}//axis method
	
	public void loadTens(){
		for (int i = 0;i<10;i++){
			tens.add(10*i);
			tens.add(-10*i);
			System.out.println(10*i + " : "+(-10*i));
		}
	}//loadTens method
	
	public void start(){
		initGL();
		loadTens();
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
			Display.setInitialBackground(255, 255, 255);
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
		Graph app = new Graph();
		app.start();
	}//main method	
}