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
	
	public String displayText = "";
	
	public List<Projectile> projs = new ArrayList<Projectile>();
	public int projNum = 0;
	public List<FighterShip> ships = new ArrayList<FighterShip>();
	public int playerNum = 2;
	
	public boolean autoFire = false;
	
	public TrueTypeFont font;
	
	
	
	public void start(){
		initGL();
		Font awtFont = new Font("Times new Roman",Font.BOLD,36);
		font = new TrueTypeFont(awtFont,false);
		
		
		
		render();
		
	}//start method
	public void render(){

		for (int i=0;i<playerNum;i++){
			ships.add(new FighterShip());
		}
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			for (FighterShip ship:ships){
				GL11.glPushMatrix();
					GL11.glDisable(GL_BLEND);
					GL11.glLoadIdentity();
			
					GL11.glTranslatef(ship.getX(), ship.getY(), 0);
					GL11.glRotatef(ship.getRotateAngle(), 0.0f, 0.0f, 1.0f);
					
					if (ships.indexOf(ship)==0){
					drawShip(1,1,0);
					} else {
						drawShip(1,0,1);
					}
					
				GL11.glPopMatrix();
			
			
			if (ship.getRotateAngle()>=180){
				ship.setRotateAngle(-179.99f);
			}else if (ship.getRotateAngle()<=-180)ship.setRotateAngle(179.99f);
			
			}//for loop through ships array
			
			switch(playerNum){
			case 1:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(0);
				
				break;
			case 2:
				if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,0);
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))ships.get(0).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))ships.get(0).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && autoFire) shoot(0);
				
				////^^^^^PLAYER 1^^^^^^^/////vvvvvPlayer 2vvvvvv//////
				if (Keyboard.isKeyDown(Keyboard.KEY_W))drive(12,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_S))drive(-8,1);
				if (Keyboard.isKeyDown(Keyboard.KEY_A))ships.get(1).addToRA(-2);
				if (Keyboard.isKeyDown(Keyboard.KEY_D))ships.get(1).addToRA(2);
				if (Keyboard.isKeyDown(Keyboard.KEY_F) && autoFire) shoot(1);
				
				break;
			default:
				System.exit(0);
				break;
			}
			
			while(Keyboard.next()){
				if (!autoFire){
					if (Keyboard.getEventKeyState()) {
						if (playerNum>0){ 
							if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(0);
						}
						if (playerNum>1){
							if (Keyboard.getEventKey() == Keyboard.KEY_F)shoot(1);
						}	
					}
				}
			}//while loop
			
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//			if (Mouse.isButtonDown(0) && HEIGHT-Mouse.getY()>120) drawLine();
			
			Projectile removeProj = null;
			
			for(Projectile p:projs){
				if (p.getX()<0 || 
					p.getX()>WIDTH ||
					p.getY()<0 ||
					p.getY()>HEIGHT
					){
					removeProj = p;
				} else {
				drawProj(p);
				removeProj = null;
				}
			}
			if (removeProj!=null){
				projs.remove(removeProj);
				removeProj = null;
			}
			
			
//			displayText = 
//					"RotateAngle: " + rotateAngle +
//					"ProjNum: " + projs.size() +
//					"" +
//					"";
//			Draw.drawString(font, displayText, WIDTH/2, HEIGHT/2, Color.magenta);
			
			
			Display.update();
			Display.sync(30);
		}
	}//render method
	public void shoot(int shipNum){
		int speed = 12;
		
		double ratioX = valueX(ships.get(shipNum).getRotateAngle());
		double ratioY = valueY(ships.get(shipNum).getRotateAngle());
		double evenX =ratioX/Math.hypot(ratioX,ratioY);
		double evenY = ratioY/Math.hypot(ratioX, ratioY);
		
		projs.add(new Projectile(ships.get(shipNum).getX(),ships.get(shipNum).getY(),evenX,evenY,speed,projNum));
//		projs.add(new Projectile(x,y,evenX,evenY,speed,projNum));
		projNum++;
		
	}//shoot method
	public void drive(int mag,int shipNum){
		double percentX = valueX(ships.get(shipNum).getRotateAngle());
		double percentY = valueY(ships.get(shipNum).getRotateAngle());
		double evenX = percentX/Math.hypot(percentX,percentY);
		double evenY = percentY/Math.hypot(percentX, percentY);
		ships.get(shipNum).addToX((float)evenX*mag);
		ships.get(shipNum).addToY((float)evenY*mag);
		
//		System.out.println("EvenX: "+evenX + " EvenY: "+evenY);
		
	}//drive method
	
	public void drawProj(Projectile p){
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
		
	}//drawProj method
	public void drawShip(float r, float g, float b){
		
		GL11.glColor3f(r, g, b);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2f(-50,0);
			GL11.glVertex2f(0,50);//Top-left
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(50,0);
			GL11.glVertex2f(0,50);//Top-right
			GL11.glVertex2f(0,0);
			
			GL11.glVertex2f(50,0);
			GL11.glVertex2f(0,-50);//Bottom-right
			GL11.glVertex2f(0,0);
			
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

	public double valueX(float angle){
		float angleX = angle;
		double percent;
		int mag = 1;
		
		if (angleX<0)angleX*=-1;
		if (angleX>90){
			angleX = (angleX - (2*(angleX-90)));
			mag *= -1;
		}
		percent = (90-angleX)/90;
		
		return percent*mag;
		
	}//valueX method
	public double valueY(float angle){
		float angleY = angle;
		if (angleY<-90){
			angleY = angleY + (2*(-90-angleY));
		}	
		if (angleY>90){
			angleY = angleY - (2*(angleY-90));
		}
		double percent = angleY/90;
		
		return percent;
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
