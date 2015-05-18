
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class DropDownMenu {
	private int WIDTH = Display.getWidth();
	private int HEIGHT = Display.getHeight();
	
	public final int MIN = 0;
	public final int MAX = 1;
	private int state = 0;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private TrueTypeFont font = new TrueTypeFont(new Font("Times new Roman",Font.BOLD,24),false);
	private int fontHeight = font.getHeight("T");
	private List<String> textOptions = new ArrayList<String>();
	private String currentOption;
	private String defaultOption = "--NONE--";
	private String newOption = "";
	private boolean newText = false;
	

	public DropDownMenu(){
		textOptions.add(defaultOption);
		currentOption = textOptions.get(0);
	}//Constructor A
	public DropDownMenu(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		textOptions.add(defaultOption);
		currentOption = textOptions.get(0);
	}//Constructor B
			
	public void addOption(String option){
		if (!textOptions.contains(option))textOptions.add(option);
//		textOptions.add(option);
	}//addOption method
	public void addOption(String ... options){
		for (String text:options){
			if (!textOptions.contains(text))textOptions.add(text);
		}
	}//addOption method
	public void reset(){
		textOptions.clear();
		currentOption = defaultOption;
		textOptions.add(defaultOption);
	}//reset method
	
	public void setDisplayMode(int state){
		this.state = state;
	}
	public void display(){
		if (state == MIN){
			height = fontHeight;
			border();

			
			glColor3f(0,0,0);
			glBegin(GL_QUADS);
				glVertex2i(x-42,y-2);
				glVertex2i(x-18,y-2);
				glVertex2i(x-18,y+37);
				glVertex2i(x-42,y+37);
			glEnd();

			glColor3f(1,0,0);
			glBegin(GL_QUADS);
				glVertex2i(x,y);
				glVertex2i(x+width,y);
				glVertex2i(x+width,y+height);
				glVertex2i(x,y+height);
			glEnd();
			glBegin(GL_TRIANGLES);
				glVertex2i(x-40,y);
				glVertex2i(x-20,y);
				glVertex2i(x-30,y+35);
			glEnd();
			
			
			GL11.glEnable(GL_BLEND);
			GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			font.drawString(x, y, currentOption);
			
			GL11.glDisable(GL_BLEND);
			
		} else if (state == MAX){
			height = fontHeight*(textOptions.size()+1);
			border();
			
			glColor3f(0,0,0);
			glBegin(GL_QUADS);
				glVertex2i(x-42,y-2);
				glVertex2i(x-18,y-2);
				glVertex2i(x-18,y+37);
				glVertex2i(x-42,y+37);
			glEnd();
			
			
			glColor3f(1,0,0);

			glBegin(GL_QUADS);
				glVertex2i(x,y);
				glVertex2i(x+width,y);
				glVertex2i(x+width,y+fontHeight);
				glVertex2i(x,y+fontHeight);
			glEnd();
			
			glBegin(GL_TRIANGLES);
				glVertex2i(x-40,y+35);
				glVertex2i(x-20,y+35);
				glVertex2i(x-30,y);
			glEnd();
			
			glBegin(GL_QUADS);
				glVertex2i(x,y+fontHeight);
				glVertex2i(x+width,y+fontHeight);
				glVertex2i(x+width,y+height);
				glVertex2i(x,y+height);
			glEnd();
			
			GL11.glEnable(GL_BLEND);
			GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			font.drawString(x, y, currentOption);
			
			for (String text:textOptions){
				font.drawString(x, y+(textOptions.indexOf(text)+1)*fontHeight, text);
			
			}
			GL11.glDisable(GL_BLEND);

			
			
		} else {
			Display.destroy();
			System.exit(0);
		}
		//Plus sign, (x+width+15,y,fontHeight,fontHeight);
		glColor3f(1,1,1);
		glBegin(GL_QUADS);
			glVertex2i(x+width+15,y);
			glVertex2i(x+width+15+fontHeight,y);
			glVertex2i(x+width+15+fontHeight,y+fontHeight);
			glVertex2i(x+width+15,y+fontHeight);
		glEnd();
		
		GL11.glEnable(GL_BLEND);
		int StringX = x+width+15+(fontHeight-font.getWidth("+"))/2;
		font.drawString(StringX, y, "+",Color.black);
		if (newText){
			currentOption = newOption;
			newOption();
//			System.out.println("Writing");
		}
		
		GL11.glDisable(GL_BLEND);
		
		
		
		
	}//display method

	public void border(){
		glColor3f(0,0,0);
		glBegin(GL_QUADS);
			glVertex2i(x-10,y-10);
			glVertex2i(x+width+10,y-10);
			glVertex2i(x+width+10,y+height+10);
			glVertex2i(x-10,y+height+10);
		glEnd();
		
	}//border method
	
	public void update(){
		while(Mouse.next()){
			/////////////////////////////////////////////////////
			if (x-42<Mouse.getX() &&
				Mouse.getX()<x-18 &&
				y-2<HEIGHT-Mouse.getY()  &&
				HEIGHT-Mouse.getY()<y+37){
				if (Mouse.isButtonDown(0)){
					state = (state==MIN ? MAX:MIN);
//					System.out.println(state);
				}
			}
			/////////////////////////////////////////////////////
			for (int i=0;i<textOptions.size();i++){
				if (x<Mouse.getX() && Mouse.getX()<x+width &&
					y+(fontHeight*(i+1))<HEIGHT-Mouse.getY() && HEIGHT-Mouse.getY()<y+(fontHeight*(i+2))){
					if (Mouse.isButtonDown(0)){
						currentOption = textOptions.get(i);
					}
					
				}
			}
			/////////////////////////////////////////////////////
			if (x+width+15<Mouse.getX() && Mouse.getX()<x+width+15+fontHeight &&
					y<HEIGHT-Mouse.getY() && HEIGHT-Mouse.getY()<y+fontHeight){
				if (Mouse.isButtonDown(0)){
					newText = true;
//					}
				}
			}
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			/////////////////////////////////////////////////////
			
			
		}//while loop
//		System.out.printf("MouseX: %s | MouseY: %s\n",Mouse.getX(),Mouse.getY());
		
	}//update method
	
	public void newOption(){

		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				
				if(Keyboard.getEventKey()==Keyboard.KEY_RETURN){
					newText=false;
					textOptions.add(newOption);
					newOption = "";
				} else {
				newOption += Keyboard.getEventCharacter();
				}
				if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE)newOption="";
			}
		}//while next loop
		newOption = newOption.toUpperCase();
					
	}//newOption method

	public String getCurrent(){return this.currentOption;}
	public boolean isBusy(){return this.newText;}
	public int size(){return this.textOptions.size();}
	public String valueAt(int iter){return this.textOptions.get(iter);}
	public int indexOf(String option){return this.textOptions.indexOf(option);}
	
	public void setX(int x){this.x = x;}//setX method
	public int getX(){return this.x;}//getX method
	
	public void setY(int y){this.y = y;}//setY method
	public int getY(){return this.y;}//getY method

	public void setWidth(int width){this.width = width;}//setWidth method
	public int getWidth(){return this.width;}//getWidth method

	public void setHeight(int height){this.height = height;}//setHeight method
	public int getHeight(){return this.height;}//getHeight method
	
}//DropDownMenu class
