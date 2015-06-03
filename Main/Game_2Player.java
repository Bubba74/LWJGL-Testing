package Main;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import Utilities.Draw;
import Utilities.Functions;


public class Game_2Player {
	public Properties saveFile = new Properties();
	public InputStream file = null;
	
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public int WIDTH = (int)d.getWidth();
	public int HEIGHT = (int)d.getHeight();
	public boolean isPaused = true;
	public int endGame = -1;
	
	public enum State {
		MAIN_MENU, CONTROLS, GAME, END_GAME;
	}
	public State state = State.MAIN_MENU;
	
	public List<Entity> projs = new ArrayList<Entity>();
	public List<Entity> fighters = new ArrayList<Entity>();

	public String playerNames[] = new String[2];
	public int playerScores[] = new int[4];
	public DropDownMenu menu1;
	public DropDownMenu menu2;
	public DropDownMenu menu0;//trash menu, bad graphics
	public int currentMenu = 1;
	
	public TrueTypeFont font;
	public int fontHeight;
	public TrueTypeFont titleFont;
	public int titleFontHeight;
	
	
	public void start(){
		initGL();
		initMenus();
		loadAccounts();
		
		Font awtFont = new Font("Times new Roman",Font.BOLD,36);
		font = new TrueTypeFont(awtFont,false);
		fontHeight = font.getHeight("T");
		
		Font awtFont2 = new Font("Times new Roman",Font.BOLD,48);
		titleFont = new TrueTypeFont(awtFont2,false);
		titleFontHeight = titleFont.getHeight("T");
		
		
		fighters.add(new Entity());
		fighters.get(fighters.size()-1).setX(200);
		fighters.get(fighters.size()-1).setY(200);
		fighters.get(fighters.size()-1).setRotateAngle(-90);
		fighters.get(fighters.size()-1).setHealth(20);
		
		fighters.add(new Entity());
		fighters.get(fighters.size()-1).setX(600);
		fighters.get(fighters.size()-1).setY(200);
		fighters.get(fighters.size()-1).setRotateAngle(-90);
		fighters.get(fighters.size()-1).setHealth(20);

		heal();
		
		while(!Display.isCloseRequested()){
			
			checkInput();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			
			Display.update();
			Display.sync(60);
		}//while loop
		Display.destroy();
	}//start method
	public void checkInput(){
		switch(state){
		
		case MAIN_MENU:
			
			if (!menu1.isBusy() && !menu2.isBusy()){
				if (Keyboard.isKeyDown(Keyboard.KEY_C))state = State.CONTROLS;
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
					playerNames[0] = menu1.getCurrent();
					playerNames[1] = menu2.getCurrent();
					loadScores();
					state = State.GAME;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
//					System.exit(0);
					save();
//					quit();
				}
			}
			
			
//			System.out.println("Checking... "+state);
			break;
		case CONTROLS:
			if (Keyboard.isKeyDown(Keyboard.KEY_M))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
			break;
		case GAME:
			if (Keyboard.isKeyDown(Keyboard.KEY_M))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
			break;
		case END_GAME:
			if (Keyboard.isKeyDown(Keyboard.KEY_R))state = State.MAIN_MENU;
			if (Keyboard.isKeyDown(Keyboard.KEY_Y))state = State.MAIN_MENU;
//			System.out.println("Checking... "+state);
		}
//		System.out.println("Switch");

	}//checkInput method
	public void render(){
		switch(state){
			case MAIN_MENU:
//				System.out.println(""+state);
				mainMenu();
				break;
			case CONTROLS:
//				System.out.println(""+state);
				controls();
				break;
			case GAME:
				endGame = -1;
				game();
//				System.out.println(""+state);
				break;
			case END_GAME:
				endGame();
//				System.out.println(""+state);
				break;
		}
		
	}//render method
	
	public void mainMenu(){
		String choosePlayer = "Choose account for Player "+(3-currentMenu);
		int menuChooserWidth = font.getWidth(choosePlayer);
		int menuChooserX = (WIDTH/2)-(menuChooserWidth)/2;
		int menuChooserHeight = fontHeight;
		int menuChooserY = (3*HEIGHT/4)-(fontHeight)/2;

		GL11.glEnable(GL_BLEND);
		Draw.drawString(titleFont, "BATTLE", WIDTH/2, HEIGHT/3, Color.magenta);
		Draw.drawString(font, "by Henry Loh", WIDTH/2, HEIGHT/3 +10+ titleFontHeight, Color.magenta);
		Draw.drawString(font, "Press: ", WIDTH/2, HEIGHT/2, Color.blue);
		Draw.drawString(font, "[c] for controls", WIDTH/2, HEIGHT/2+fontHeight, Color.gray);
		Draw.drawString(font, "[space] to play", WIDTH/2, HEIGHT/2+2*(fontHeight), Color.gray);
		
		Draw.drawString(font, choosePlayer, WIDTH/2, 3*HEIGHT/4, Color.black);
		
		
		if(Mouse.getEventButtonState()){
			if (menuChooserX<Mouse.getX() && Mouse.getX()<menuChooserX+menuChooserWidth &&
				menuChooserY<HEIGHT-Mouse.getY() && HEIGHT-Mouse.getY()<menuChooserY+menuChooserHeight)
				{
//				System.out.println("Close");
				if (Mouse.isButtonDown(0)){
					currentMenu = 3-currentMenu;
//					System.out.println(""+currentMenu);
				}
			}
		}
		
		menu0.display();
		menu1.display();
		menu2.display();
		
		if (currentMenu == 1)menu1.update();
		if (currentMenu == 2)menu2.update();
		
	}//mainMenu method
	public void controls(){
		Draw.drawString(font, "Press [m] to return to main menu", WIDTH/2, HEIGHT/3+fontHeight, Color.gray);
	}//controls method
	
	public void game(){
		GL11.glDisable(GL_BLEND);
		for (Entity ship:fighters){

			GL11.glColor3f(1, 0, 0);
				
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glTranslatef(ship.getX(), ship.getY(), 0);
			GL11.glRotatef(ship.getRotateAngle(), 0.0f, 0.0f, 1.0f);
				
				if (fighters.indexOf(ship)==0){
					drawShip(0,0,1);
				} else if (fighters.indexOf(ship)==1){
					drawShip(0,1,0);
				}
			GL11.glPopMatrix();
				
			if (ship.getRotateAngle()>=180){
				ship.setRotateAngle(-179.99f);
			}else if (ship.getRotateAngle()<=-180)ship.setRotateAngle(179.99f);
			
			}//for loop through ships array

			updateHealth();

			if (Keyboard.isKeyDown(Keyboard.KEY_W))drive(12,0);
			if (Keyboard.isKeyDown(Keyboard.KEY_S))drive(-8,0);
			if (Keyboard.isKeyDown(Keyboard.KEY_A))fighters.get(0).addToRotateAngle(-2);
			if (Keyboard.isKeyDown(Keyboard.KEY_D))fighters.get(0).addToRotateAngle(2);
			
			////^^^^^PLAYER 1^^^^^^^/////vvvvvPlayer 2vvvvvv//////
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))drive(12,1);
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))drive(-8,1);
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))fighters.get(1).addToRotateAngle(-2);
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))fighters.get(1).addToRotateAngle(2);
			while(Keyboard.next()){
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_F)shoot(Projectile.projType.BULLET,0);
					if (Keyboard.getEventKey() == Keyboard.KEY_G)shoot(Projectile.projType.BOMB,0);
					if (Keyboard.getEventKey() == Keyboard.KEY_RCONTROL)shoot(Projectile.projType.BULLET,1);
					if (Keyboard.getEventKey() == Keyboard.KEY_RMENU)shoot(Projectile.projType.BOMB,1);
					if (Keyboard.getEventKey() == Keyboard.KEY_Y) heal();
				}
			}//while loop
			
			Entity removeProj = null;
			
			for(Entity p:projs){
				if (p.getX()<0 || 
					p.getX()>WIDTH ||
					p.getY()<0 ||
					p.getY()>HEIGHT
					){
					removeProj = p;
				} else {
					for (int i=0;i<fighters.size();i++){
						if (p.getShipNum() != i &&
							Math.abs(fighters.get(i).getX()-p.getX())<40 &&
							Math.abs(fighters.get(i).getY()-p.getY())<40
							){/*1*/
							removeProj = p;
						
							if (fighters.get(i).getHealth()==1){/*2*/
								fighters.get(i).addToHealth(-1);
							
								updateHealth();
								
								endGame = 1-i;
							}/*2*/ else{
								fighters.get(i).addToHealth(-1);
						}//else statement
						
						}/*1*/
					}//for loop of ships
					if (removeProj == null){
						drawProj(p.toProj());
						float newX = (float) (p.getX()+p.getDX()*p.getSpeed());
						float newY = (float)(p.getY()+p.getDY()*p.getSpeed());
						p.setX((int) newX);
						p.setY((int) newY);
					}
				
				}//else statement
			}//for loop of projectiles
			
			if (removeProj!=null){
				projs.remove(removeProj);
				removeProj = null;
			}
			if (endGame!=-1){
				state = State.END_GAME;
				endGame();
			}
			
	} //game method
	public void endGame(){
		Draw.drawString(titleFont, (endGame==0 ? playerNames[0]+" wins!!!":playerNames[1]+" wins!!!"), WIDTH/2, HEIGHT/3, Color.red);
		Draw.drawString(font, "Press [r] to play again", WIDTH/2, HEIGHT/2, Color.darkGray);
		heal();
	}//endGame method
	public void heal(){
		for (Entity e:fighters){
			e.setX((WIDTH/5)+(((3*WIDTH)/5)*fighters.indexOf(e)));
			e.setY(400);

			e.setHealth(20);
			e.setRotateAngle(-90);
		}
		projs.clear();
	}//heal method=
	public void save(){
		List<String> options = new ArrayList<String>();
		for (int i=0;i<menu1.size();i++){
			if(!options.contains(menu1.valueAt(i)))options.add(menu1.valueAt(i));
		}
		for (int i=0;i<menu2.size();i++){
			if(!options.contains(menu2.valueAt(i)))options.add(menu2.valueAt(i));
		}
		
		for (int i=0;i<2;i++){
			Properties save = new Properties();
			try{

				save.setProperty("LOSSES_"+i, ""+playerScores[(i*2)+1]);
				save.setProperty("WINS_"+i, ""+playerScores[i*2]);
				save.setProperty("Account"+i,playerNames[i]);

				save.store(new FileOutputStream(new File(options.get(i)+".stats")), "--save--");
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}//save method
	
	public void shoot(Projectile.projType type,int shipNum){
		double evenX = Functions.circleValue(fighters.get(shipNum).getRotateAngle(), true);
		double evenY = Functions.circleValue(fighters.get(shipNum).getRotateAngle(), false);
		
		switch (type){
		case BULLET:
			int bulletSpeed = 12;
			projs.add(new Entity());
			projs.get(projs.size()-1).setX(fighters.get(shipNum).getX());
			projs.get(projs.size()-1).setY(fighters.get(shipNum).getY());
			projs.get(projs.size()-1).setDX((float)evenX);
			projs.get(projs.size()-1).setDY((float)evenY);
			projs.get(projs.size()-1).setSpeed(bulletSpeed);
			projs.get(projs.size()-1).setShipNum(shipNum);
			break;
		case BOMB:
			int bombSpeed = 6;
			float bombX = (float)evenX/4;
			float bombY = .8f;
			projs.add(new Entity());
			projs.get(projs.size()-1).setX(fighters.get(shipNum).getX());
			projs.get(projs.size()-1).setY(fighters.get(shipNum).getY());
			projs.get(projs.size()-1).setDX(bombX);
			projs.get(projs.size()-1).setDY(bombY);
			projs.get(projs.size()-1).setSpeed(bombSpeed);
			projs.get(projs.size()-1).setShipNum(shipNum);
			break;
			
		}
//		projs.add(new Projectile(x,y,evenX,evenY,speed,ships.));
		
	}//shoot method
	public void drive(int mag,int shipNum){
		double evenX = Functions.circleValue(fighters.get(shipNum).getRotateAngle(), true);
		double evenY = Functions.circleValue(fighters.get(shipNum).getRotateAngle(), false);
		fighters.get(shipNum).addToX((int) (evenX*mag));
		fighters.get(shipNum).addToY((int) (evenY*mag));
		
//		System.out.println("EvenX: "+evenX + " EvenY: "+evenY);
		
	}//drive method
	
	public void updateHealth(){
		GL11.glEnable(GL_BLEND);
		for (int i=0;i<fighters.size();i++){
			Draw.drawString(font, ""+fighters.get(i).getHealth(), (int)fighters.get(i).getX(), (int)fighters.get(i).getY(), Color.magenta);
		}
	}//updateHealth method
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
	
	public void loadScores(){
		for(int i=0;i<playerNames.length;i++){
			try{
				saveFile.load(new FileInputStream(new File(playerNames[i]+".stats")));
			
			} catch(Exception e){file = null;}
		
			if(file == null){
				playerScores[i*2] = 0;
				playerScores[(i*2)+1] = 0;
				save();
			} else {
				for (int z=0;z<playerNames.length;z++){
					playerScores[i*2] = new Integer(saveFile.getProperty("WINS_"+i));
					playerScores[(i*2)+1] = new Integer(saveFile.getProperty("LOSSES_"+i));
				}
			}
		}//for loop
		
		
	}//loadScores method
	public void loadAccounts(){
		try{
			File properties = new File("Accounts.master");
			file = new FileInputStream( properties );
			saveFile.load(file);
		} catch(Exception e){file = null;}
		if (file == null){
			System.out.println(menu1.size());
			save();
			
		} else {
			for (int i=0;i<saveFile.size();i++){
				menu1.addOption(saveFile.getProperty("Account"+i));
				menu2.addOption(saveFile.getProperty("Account"+i));
			}
		}
		
		
	}//loadAccounts method
	public void initMenus(){
		menu0 = new DropDownMenu(-100,-100,50,50);
		menu1 = new DropDownMenu(200,300,200,100);
		menu2 = new DropDownMenu(WIDTH-400,300,200,100);
	}//initMenus method
	public void initGL(){
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setTitle("Rotating Ship ["+WIDTH+", "+HEIGHT+"]");
			Display.setInitialBackground(00, 255, 255);
			
			Display.create();
			Keyboard.create();
			Keyboard.poll();
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
		Game_2Player main = new Game_2Player();
		main.start();
	}
	
}
