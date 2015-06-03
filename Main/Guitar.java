package Main;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.WaveData;


public class Guitar {
	
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int WIDTH = (int) d.getWidth();
	public int HEIGHT = (int) d.getHeight();
	
	public int baseX = 1600;
	public int baseY = 400;
	public int fretX = 400;
	
	
	public int x = fretX+(baseX-fretX)/2;
	public int y = baseY;
	public int lineX = x;
	public int lineY = y;
	public boolean grabbed = false;
	public int dynamicX = 0;
	public int dynamicY = 0;

	public int source;

	public void render(){
		
		while(Keyboard.next()){
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				//play sound
				AL10.alSourcePlay(source);
			}
		}
		
		
		if (Mouse.isButtonDown(0)){
			grabbed = true;
			dynamicY = lineY - y;
			dynamicX = lineX - x;
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
				fretX = Mouse.getX();
				x = fretX+(baseX-fretX)/2;
			}
			grabbed = false;
		}
		
		if (grabbed){
			lineX = Mouse.getX();
			lineY = HEIGHT-Mouse.getY();
		}
		
		drawLine();
		if (!Mouse.isButtonDown(0))updateLine();
		
	}//render method
	public void updateLine(){
		lineY -= dynamicY/5;
		lineX -= dynamicX/50;
		if (dynamicY>0){
			if ((y-dynamicY*.6)>lineY)dynamicY *= -.6;
		} else {
			if ((y-dynamicY*.6)<lineY)dynamicY *= -.6;
		}
		if (dynamicX>0){
			if ((x-dynamicX*.6)>lineX)dynamicX *= -.6;
		} else {
			if ((x-dynamicX*.6)<lineX)dynamicX *= -.6;
		}
		
	}//updateLine method
	
	public void drawLine(){
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(fretX, baseY);
			GL11.glVertex2i(fretX, baseY+10);
			GL11.glVertex2i(lineX, lineY+10);
			GL11.glVertex2i(lineX, lineY);
			
			GL11.glVertex2i(lineX, lineY+10);
			GL11.glVertex2i(lineX, lineY);
			GL11.glVertex2i(baseX, baseY);
			GL11.glVertex2i(baseX, baseY+10);
		GL11.glEnd();
	}//drawLine method
	
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
	
	public static void main (String Args[]){
		Guitar demo = new Guitar();
		demo.start();
		
	}//main method
	public void start() {
		initGL();
		
		while(!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			
			Display.update();
			Display.sync(40);
		}
		Display.destroy();

	}
	
	
	
	
}//Guitar class