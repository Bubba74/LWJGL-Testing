import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Fireworks {
	
	private Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = (int)dm.getWidth();
	private int HEIGHT = (int)dm.getHeight();
	private List<Projectile> sparks = new ArrayList<Projectile>();
	private List<Firework> rockets = new ArrayList<Firework>();
	private Random rn = new Random();
	
	public void render(){
		if (rn.nextInt(50)==0) newFirework();
		
		Firework removeRock = null;
		for (Firework f:rockets){
			if ((int)System.currentTimeMillis()-f.getTime()<=f.getDelay()){
				Draw.drawFirework(f);
			} else {
				if (f.getY()>HEIGHT/2){
					Draw.drawFirework(f);
				} else {
					removeRock = f;
					explode(f.getX(),f.getY(),40,f.getPower());
				}
			}
			
		}
		if (removeRock != null){
			rockets.remove(removeRock);
		}
		
		
		Projectile removeProj = null;
		for (Projectile p:sparks){
			if (p.getX()<0 || 
					p.getX()>WIDTH ||
					p.getY()<0 ||
					p.getY()>HEIGHT){
					removeProj = p;
			}else {
				p.addToDeltaY(+0.005f);
				Draw.drawProj(p,rn.nextFloat(),rn.nextFloat(),rn.nextFloat());
			}
		}
		if (removeProj != null) sparks.remove(removeProj);
		
	}//render method
	
	public void newFirework(){
		int speed = rn.nextInt(10);

		float dX = (float).5 - (rn.nextFloat());
		float dY = -1 * rn.nextFloat();
		
		int x = rn.nextInt(WIDTH/2)+WIDTH/4;
		int y = HEIGHT-50;
		
		rockets.add(new Firework(x,y,dX,dY,speed,rn.nextInt(10),(int)System.currentTimeMillis(),rn.nextInt((20-speed)/4)*3000));
	}//newFirework method
	public void explode(int x, int y,int num,int power){

			for (int i=0;i<num;i++){
				
				int dA = 360/num;
				double fAngle = dA * i;
				
				if (fAngle >180){
					fAngle -= 360;
				}
				double evenX = Utilities.circleValue((float)fAngle, true);
				double evenY = Utilities.circleValue((float)fAngle, false);
			
				float sf = 1;
				sparks.add(new Projectile(x,y,evenX*sf,evenY*sf,power,0));
			}
			for (int i=0;i<num;i++){
				
				int dA = 360/num;
				double fAngle = dA * i;
				
				if (fAngle >180){
					fAngle -= 360;
				}	
				double evenX = Utilities.circleValue((float)fAngle, true);
				double evenY = Utilities.circleValue((float)fAngle, false);
				
				float sf = 1;
				sf = rn.nextFloat();
				
				sparks.add(new Projectile(x,y,evenX*sf,evenY*sf,power,0));
			}
		
		
	}//firework method
	
	public void init(){
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.setInitialBackground(0, 0, 0);
			Display.setTitle("!!!`FIREWORKS!!!");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		glMatrixMode(GL_MODELVIEW);
	}//init method
	public void start(){
		init();

		while (!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			Display.update();
			Display.sync(40);
		}
		Display.destroy();
		System.exit(0);
	}//start method
	public static void main (String Args[]){
		Fireworks show = new Fireworks();
		show.start();
	}//main method
}//Fireworks class
