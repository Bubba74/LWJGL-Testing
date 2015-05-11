import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class MovingShip {
	
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int WIDTH = (int)d.getWidth();
	public int HEIGHT = (int)d.getHeight();
	public int x = 500;
	public int y = 400;
	public float rotateAngle = 0.01f;
	
	public float r = 1f;
	public float g = 0f;
	public float b = 0f;
	
	public List<Projectile> projs = new ArrayList<Projectile>();
	
	public TrueTypeFont font;
	
	
	
	public void start(){
		initGL();
		Font awtFont = new Font("Times new Roman",Font.BOLD,36);
		font = new TrueTypeFont(awtFont,false);
		
		
		
		render();
		
	}//start method
	public void render(){
		
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			
//			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			
			GL11.glPushMatrix();
			GL11.glDisable(GL_BLEND);
			GL11.glLoadIdentity();

			GL11.glTranslatef((float)x, (float)y, 0);
			GL11.glRotatef(rotateAngle, 0.0f, 0.0f, 1.0f);
				drawShip();
			GL11.glPopMatrix();
			
			Draw.drawString(font, "RotateAngle: "+rotateAngle, WIDTH/2, HEIGHT/2, Color.magenta);
			
//			drawSettings();
			
			
			if (rotateAngle>=180){
				rotateAngle=-179.99f;
			}else if (rotateAngle<=-180)rotateAngle=179.99f;
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12);
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8);
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))rotateAngle-=2;
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))rotateAngle+=2;
			
			while (Keyboard.next()) {
				if (!Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
						shoot();
					}
				}   
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			if (Keyboard.isKeyDown(Keyboard.KEY_R)){
				x = 500;
				y = 400;
			}
			if (Mouse.isButtonDown(0) && HEIGHT-Mouse.getY()>120) drawLine();
			
			int size = projs.size();
			for(Projectile p:projs){
				
				p.setX((float)(p.getX()+p.getDeltaX()*p.getSpeed()));
				p.setY((float)(p.getY()+p.getDeltaY()*p.getSpeed()));
				
				GL11.glPushMatrix();
				GL11.glDisable(GL_BLEND);
				GL11.glLoadIdentity();
				
				GL11.glColor3f(1, 1, 0);
				GL11.glTranslated(p.getX(), p.getY(), 0);
					
				GL11.glBegin(GL11.GL_POLYGON);
					GL11.glVertex2f(-10, 0);
					GL11.glVertex2f(-5, 5);
					GL11.glVertex2f(0, 10);
					GL11.glVertex2f(5, 5);
					GL11.glColor3f(0, 0, 0);
				
					GL11.glVertex2f(10, 0);
					GL11.glVertex2f(5, -5);
					GL11.glVertex2f(0, -10);
					GL11.glVertex2f(-5, -5);
				GL11.glEnd();
				
				GL11.glPopMatrix();
				
			}
			
			Display.update();
			Display.sync(50);
		}
	}//render method
	public void shoot(){
		int speed = 4;
		
		double ratioX = valueX(rotateAngle,1);
		double ratioY = valueY(rotateAngle,1);
		double evenX =ratioX/Math.hypot(ratioX,ratioY);
		double evenY = ratioY/Math.hypot(ratioX, ratioY);
		
		projs.add(new Projectile(x,y,evenX,evenY,speed));
		
		
		
		
		
		
	}//shoot method
	public void drive(int mag){
		
		double percentX = valueX(rotateAngle,1);
		double percentY = valueY(rotateAngle,1);
		double evenX =percentX/Math.hypot(percentX,percentY);
		double evenY = percentY/Math.hypot(percentX, percentY);
		x+=evenX*mag;
		y+=evenY*mag;
		
	}//drive method
	
	public void drawShip(){
		GL11.glBegin(GL11.GL_TRIANGLES);
		
		GL11.glColor3f(0, 0, 1);
		
		GL11.glVertex2f(-50,0);
		GL11.glVertex2f(0,50);//Top-left
		GL11.glVertex2f(0,0);
		
		
		GL11.glColor3f(1, 0, 0);
		
		GL11.glVertex2f(50,0);
		GL11.glVertex2f(0,50);//Top-right
		GL11.glVertex2f(0,0);
		
		GL11.glVertex2f(50,0);
		GL11.glVertex2f(0,-50);//Bottom-right
		GL11.glVertex2f(0,0);
		
		GL11.glColor3f(0, 0, 1);
		
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

	public double valueX(float angle,int mag){
		float angleX = angle;
		double percent;
		
		if (angleX<0)angleX*=-1;
		if (angleX>90){
			angleX = (angleX - (2*(angleX-90)));
			mag *= -1;
		}
		percent = (90-angleX)/90;
		
		return percent*mag;
		
	}//valueX method
	public double valueY(float angle, int mag){
		float angleY = rotateAngle;
		if (angleY<-90){
			angleY = angleY + (2*(-90-angleY));
		}	
		if (angleY>90){
			angleY = angleY - (2*(angleY-90));
		}
		double percent = angleY/90;
		
		
		return percent*mag;
	}//valueY method
	public void initGL(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Rotating Ship ["+WIDTH+", "+HEIGHT+"]");
			Display.setInitialBackground(00, 255, 255);
			
			Display.create();
	
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
		MovingShip main = new MovingShip();
		main.start();
	}
	
}
