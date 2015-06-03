package DrawFunctions;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;


public class Notify {
	
	private int WIDTH = Display.getWidth();
	private int HEIGHT = Display.getHeight();
	
	public List<String> outputs = new ArrayList<String>(); 
	public List<Integer> outputLength = new ArrayList<Integer>();
	public List<Integer> outputTime = new ArrayList<Integer>();
	
	public List<String> outputsTrash = new ArrayList<String>();
	public List<Integer> outputLengthTrash = new ArrayList<Integer>();
	public List<Integer> outputTimeTrash = new ArrayList<Integer>();
	
	private TrueTypeFont font;
	
	public Notify(){
		Font awtFont = new Font("Times new Roman",Font.BOLD,36);
		font = new TrueTypeFont(awtFont,false);
	}//notify constructor
	public void addOutput(String text, int time){
		if (!outputs.contains(text)){
			outputs.add(text);
			outputLength.add(time*1000);
			outputTime.add((int)System.currentTimeMillis());
		}
	}
	public void display(){
		int time = (int)System.currentTimeMillis();
		for (int i=0;i<outputs.size();i++){
			if (outputLength.get(i)<0){
				print(outputs.get(i),100+(i*100));
			} else {
				if (time-outputTime.get(i)<outputLength.get(i)){
					print(outputs.get(i),100+(i*100));
			
				} else {
					outputsTrash.add(outputs.get(i));
					outputLengthTrash.add(outputLength.get(i));
					outputTimeTrash.add(outputTime.get(i));
				}
			}
		}
		for (int i=0;i<outputsTrash.size();i++){
			outputs.remove(outputsTrash.get(i));
			outputLength.remove(outputLengthTrash.get(i));
			outputTime.remove(outputTimeTrash.get(i));
			outputsTrash.clear();
			outputLengthTrash.clear();
			outputTimeTrash.clear();
		}
		
		glDisable(GL_BLEND);
	}//display method
	public void clear(){
		outputs.clear();
		outputLength.clear();
		outputTime.clear();
	}//clear method
	public void print(String s, int height){
		float leftX = WIDTH/2 - (font.getWidth(s)/2);
		float topY = height/2 - (font.getHeight(s)/2);
		glEnable(GL_BLEND);
		font.drawString(leftX,topY,s,Color.black);
	}//print method
	
	
	
}//Notify class