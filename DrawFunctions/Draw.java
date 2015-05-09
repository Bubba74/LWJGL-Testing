package DrawFunctions;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Draw {
	
	public static void drawCircle(int x,int y,int rad){
		glClear(GL_COLOR_BUFFER_BIT);
		glBegin(GL_LINES);
			glVertex2i(x,y);
			glVertex2i(x+rad,y);
		glEnd();
		
	}//drawCircle method
	
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
		
		
		GL11.glEnable(GL_TEXTURE_2D);
		glClear(GL_COLOR_BUFFER_BIT);
		
		image.bind();
		
		GL11.glEnable(GL_BLEND);
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
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
		
//		GL11.glDisable(GL_TEXTURE_2D);
	}//drawImage method
	
	
	
	
}//Draw class
