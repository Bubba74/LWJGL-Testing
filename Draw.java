import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Draw {

	public static Random rn = new Random();
	
	public static Texture texture;
	
	public static void gingerBreadMan(int xCord,int yCord, int size){
		int unit = 3;
//		xCord = 200;
//		yCord = 300;
		//head
		
		
		GL11.glColor3f(1f,.7f,0f);// skin color
		
		glBegin(GL_POLYGON);
			//left head
			glVertex2i(xCord+6*unit,yCord+6*unit);
			glVertex2i(xCord+5*unit,yCord+5*unit);
			glVertex2i(xCord+5*unit,yCord+2*unit);
			glVertex2i(xCord+7*unit,yCord+0*unit);
			//right head
			glVertex2i(xCord+11*unit,yCord+0*unit);
			glVertex2i(xCord+13*unit,yCord+2*unit);
			glVertex2i(xCord+13*unit,yCord+5*unit);
			glVertex2i(xCord+12*unit,yCord+6*unit);
		glEnd();

		glBegin(GL_POLYGON);
			//neck
			glVertex2i(xCord+6*unit,yCord+8*unit);
			glVertex2i(xCord+6*unit,yCord+7*unit);
			glVertex2i(xCord+7*unit,yCord+6*unit);
			glVertex2i(xCord+11*unit,yCord+6*unit);
			glVertex2i(xCord+12*unit,yCord+7*unit);
			glVertex2i(xCord+12*unit,yCord+8*unit);
		glEnd();
		
		glBegin(GL_POLYGON);
			//left hand
			glVertex2i(xCord+1*unit,yCord+10*unit);
			glVertex2i(xCord+0*unit,yCord+9*unit);
			glVertex2i(xCord+0*unit,yCord+8*unit);
			glVertex2i(xCord+1*unit,yCord+7*unit);
			
			glVertex2i(xCord+3*unit,yCord+7*unit);
			glVertex2i(xCord+4*unit,yCord+8*unit);
			glVertex2i(xCord+4*unit,yCord+9*unit);
			glVertex2i(xCord+3*unit,yCord+10*unit);
		glEnd();

		glBegin(GL_POLYGON);
			//left arm
			glVertex2i(xCord+1*unit,yCord+10*unit);
			glVertex2i(xCord+6*unit,yCord+10*unit);
			glVertex2i(xCord+6*unit,yCord+12*unit);
			glVertex2i(xCord+2*unit,yCord+12*unit);
		glEnd();

		glBegin(GL_POLYGON);
			//right hand
			glVertex2i(xCord+15*unit,yCord+10*unit);
			glVertex2i(xCord+14*unit,yCord+9*unit);
			glVertex2i(xCord+14*unit,yCord+8*unit);
			glVertex2i(xCord+15*unit,yCord+7*unit);
			
			glVertex2i(xCord+17*unit,yCord+7*unit);
			glVertex2i(xCord+18*unit,yCord+8*unit);
			glVertex2i(xCord+18*unit,yCord+9*unit);
			glVertex2i(xCord+17*unit,yCord+10*unit);
		glEnd();
		
		glBegin(GL_POLYGON);
			//right arm
			glVertex2i(xCord+12*unit,yCord+10*unit);
			glVertex2i(xCord+17*unit,yCord+10*unit);
			glVertex2i(xCord+16*unit,yCord+12*unit);
			glVertex2i(xCord+12*unit,yCord+12*unit);
		glEnd();
		
		glBegin(GL_POLYGON);
			//body
			glVertex2i(xCord+6*unit,yCord+8*unit);
			glVertex2i(xCord+12*unit,yCord+8*unit);
			glVertex2i(xCord+12*unit,yCord+16*unit);
			glVertex2i(xCord+6*unit,yCord+16*unit);
		glEnd();
		
		
		/////^^^^^^^SKIN COLOR^^^^^^^^^^///////
		glBegin(GL_QUADS);			
			//FACE
			GL11.glColor3f(0.0f,0.0f,0.0f);
			//left eye
			glVertex2i(xCord+7*unit,yCord+2*unit);
			glVertex2i(xCord+8*unit,yCord+2*unit);
			glVertex2i(xCord+8*unit,yCord+3*unit);
			glVertex2i(xCord+7*unit,yCord+3*unit);
			//right eye
			glVertex2i(xCord+10*unit,yCord+2*unit);
			glVertex2i(xCord+11*unit,yCord+2*unit);
			glVertex2i(xCord+11*unit,yCord+3*unit);
			glVertex2i(xCord+10*unit,yCord+3*unit);
			
			GL11.glColor3f(.85f,.25f,.25f);
			//left lip
			glVertex2i(xCord+7*unit,yCord+5*unit);
			glVertex2i(xCord+8*unit,yCord+4*unit);
			glVertex2i(xCord+8*unit,yCord+5*unit);
			glVertex2i(xCord+7*unit,yCord+6*unit);
			//middle lip
			glVertex2i(xCord+8*unit,yCord+4*unit);
			glVertex2i(xCord+10*unit,yCord+4*unit);
			glVertex2i(xCord+10*unit,yCord+5*unit);
			glVertex2i(xCord+8*unit,yCord+5*unit);
			//right lip
			glVertex2i(xCord+10*unit,yCord+4*unit);
			glVertex2i(xCord+11*unit,yCord+5*unit);
			glVertex2i(xCord+11*unit,yCord+6*unit);
			glVertex2i(xCord+10*unit,yCord+5*unit);
		glEnd();
		
		
		
/*      SAMPLE CODE
		
		GL11.glColor3f(0.0f,0.0f,0.0f);
		glBegin(GL_QUADS);
			glVertex2i(xCord+*unit,yCord+*unit);
			glVertex2i(xCord+*unit,yCord+*unit);
			glVertex2i(xCord+*unit,yCord+*unit);
			glVertex2i(xCord+*unit,yCord+*unit);
		glEnd();
*/	
		
	}//gingerBreadMan method
	
	public static Texture getTexture(String src){
		Texture texture;
		
		try{
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("/media/ecdc2b6c-fd54-4dd8-a2c8-c41834bb8f58/lib/textures/" + src + ".png")));
		} catch(IOException e){
			e.printStackTrace();
			texture = null;
		}
		
		return texture;
	}//getTexture method
	
	public static void drawImage(int x, int y,int size, String pic){
		Texture image = getTexture(pic);
		image.bind();
		
		GL11.glEnable(GL_TEXTURE_2D);

		GL11.glEnable(GL_BLEND);
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glColor3f(1f, 1f, 1f);
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2i(x,y);
			glTexCoord2f(1,0);
			glVertex2i(x+size,y);
			glTexCoord2f(1,1);
			glVertex2i(x+size,y+size);
			glTexCoord2f(0,1);
			glVertex2i(x,y+size);
		glEnd();
		
		GL11.glDisable(GL_TEXTURE_2D);
//		GL11.glDisable(GL_BLEND);
	}//drawImage method
	
	public static void drawRect(int x, int width, int y, int height, float r, float g, float b){
		
		
		GL11.glColor3f(r, g, b);
		glBegin(GL_QUADS);
			glVertex2i(x,y);
			glVertex2i(x+width,y);
			glVertex2i(x+width,y+height);
			glVertex2i(x,y+height);
		glEnd();
		
		
		
	}//drawRect method
	
	public static void drawObstacle(Obstacle o){
		int x = o.getX();
		int y = o.getY();
		int width = o.getWidth();
		int height = o.getHeight();
		
		GL11.glColor3f(o.getR(), o.getG(), o.getB());
		glBegin(GL_QUADS);
			glVertex2i(x,y);
			glVertex2i(x+width,y);
			glVertex2i(x+width,y+height);
			glVertex2i(x,y+height);
		glEnd();
		
	}//drawObstacle method
	
	public static void drawString(TrueTypeFont f, String s, int centerX, int centerY,org.newdawn.slick.Color c){
		float leftX = centerX - (f.getWidth(s)/2);
		float topY = centerY - (f.getHeight(s)/2);
		GL11.glEnable(GL_BLEND);
		f.drawString(leftX,topY,s,c);
		
	}//drawString method
	
	public static void drawProj(Projectile p){
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
	public static void drawProj(Projectile p,float r,float g,float b){
		p.setX((float)(p.getX()+p.getDeltaX()*p.getSpeed()));
		p.setY((float)(p.getY()+p.getDeltaY()*p.getSpeed()));
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glColor3f(r, g, b);
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
	public static void drawFighter(Fighter f){

		f.setX((int)(f.getX()+f.getDX()*10));
		f.setY((int)(f.getY()+f.getDY()*10));
		
		int hi = f.getHeight();
		int MH = f.MAXHEALTH;
		int hl = f.getHealth();
		int newY = ((MH-hl)*hi)/MH;
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glTranslated(f.getX(), f.getY(), 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1, 0, 0);
			glVertex2i(0,0);
			glVertex2i(f.getWidth(),0);
			glVertex2i(f.getWidth(),hi);
			glVertex2i(0,hi);
		GL11.glEnd();
		GL11.glBegin(GL_QUADS);
			GL11.glColor3f(0, 1, 0);
			glVertex2i(0,newY);
			glVertex2i(f.getWidth(),newY);
			glVertex2i(f.getWidth(),newY+hi-newY);
			glVertex2i(0,newY+hi-newY);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
//		System.out.printf("X: %s, Y: %s, Width: %s, Height: %s",f.getX(),f.getY(),f.getWidth(),f.getHeight());
//		System.out.printf("hi: %s, MH: %s, hl: %s, newY: %s, newHi: %s\n",hi,MH,hl,newY,hi-newY);
		
	}//drawFighter method

	public static void drawPowerup(Powerup p){

		p.setX((int)(p.getX()+p.getDX()*5));
		p.setY((int)(p.getY()+p.getDY()*5));
		
		int hi = 60;
		int wi = 40;
		Powerup.type t = p.getType();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glTranslated(p.getX(), p.getY(), 0);
		
		if (t == Powerup.type.HEALTH){
			GL11.glColor3f(0, 1, 1);
		} else if (t == Powerup.type.LASER){
			GL11.glColor3f(1, 0, 0);
		} else if (t == Powerup.type.FIREWORK){
			GL11.glColor3f(1, 0, 1);
		} else if (t == Powerup.type.CLEAR){
			GL11.glColor3f(1, 1, 1);
		}
		

		GL11.glBegin(GL11.GL_QUADS);
			glVertex2i(0,0);
			glVertex2i(wi,0);
			glVertex2i(wi,hi);
			glVertex2i(0,hi);
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
	}//drawPowerup method
	
	public static void drawFirework(Firework f){
		f.setX((int)(f.getX()+f.getDX()*f.getSpeed()));
		f.setY((int)(f.getY()+f.getDY()*f.getSpeed()));
		
		int hi = 60;
		int wi = 40;
		
		GL11.glPushMatrix();
		GL11.glDisable(GL_BLEND);
		GL11.glLoadIdentity();
		
		GL11.glTranslated(f.getX(), f.getY(), 0);
		
		GL11.glColor3f(0, 0.1f, 0.1f);

		GL11.glBegin(GL11.GL_QUADS);
			glVertex2i(0,0);
			glVertex2i(wi,0);
			glVertex2i(wi,hi);
			glVertex2i(0,hi);
		GL11.glEnd();
		
		GL11.glPopMatrix();

	}//drawFirework method
	
}//Draw class
