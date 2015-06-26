package Main;
import Utilities.Draw;
import Utilities.Functions;
import Utilities.Notify;


public class Ball {
	
	public int x = 400;
	public float y = 50;
	public int r = 20;
	public float dX = 0;
	public float dY = 0;
	public int speed = 2;
	
	private Notify display = new Notify();
	
	public int acc = 360;
	public float xValues[] = new float[acc];
	public float yValues[] = new float[acc];
	public int otherX[];
	public float otherY[];
	public boolean isPaused = true;
	
	public Ball (int otherX[],float otherY[]){
		init();
		this.otherX = otherX.clone();
		this.otherY = otherY.clone();
//		for (int z:otherX){
//			System.out.println(z);
//		}
//		for (int z:otherY){
//			System.out.println(z);
//		}
		
	}//Ball constructor
	public void init(){
		for (int i=0;i<acc;i++){
			int angle = i;
			if (angle>180)angle -= 360;
			
			xValues[i] = (float) Functions.circleValue(angle, true);
			yValues[i] = (float) Functions.circleValue(angle, false);
			System.out.printf("i=%s, X: %s, Y: %s\n",i,xValues[i],yValues[i]);
			
		}//for method
		
	}//init method
	public void display(){
		Draw.drawCircle(x,(int)y,r,"fill");
		display.display();
	}//display method
	public void updateLoc(){
		display.clear();

		if (!isPaused){
			
			float highestY = otherY[highestY(otherY,x,r)];
			if (highestY < y+r){
				y = highestY-r;
				dY = 0;
			} else {
				int leftIndex = highestY(otherY,x-r/2,r/2);
				int rightIndex = highestY(otherY,x+r/2,r/2);
				
				float highestLeft = otherY[leftIndex];
				float highestRight = otherY[rightIndex];
				
				if (highestLeft<y+r+5 || highestRight<y+r+5){
				
				
					float slope = 90;//slope of line beneath ball
				
				
					int sideA = rightIndex-leftIndex;
					int sideB = (int) (highestRight-highestLeft);
				
					slope = (float) Math.tan(sideB/sideA);

					if (highestLeft<highestRight){
						dX += (float)Functions.circleValue(slope, true);
						dY += (float)Functions.circleValue(slope, false);
					} else if (highestLeft>highestRight){
						dX += -1 * (float)Functions.circleValue(slope, true);
						dY -= (float)Functions.circleValue(slope, false);
					}else {
//						dX = 
					}
//				} else {
				}
					dY += 0.02f;
				
			}
			
//			System.out.println("BALL PAUSED");
		} else {
			dX = 0;
			dY = 0;
		}
		
		display.addOutput("X: "+x+" Y: "+y, -1);
		display.addOutput("dX: "+dX + " dY: "+dY, -1);
		x += dX*speed*0.5;
		dX *= 0.97;
		y += dY*speed*0.5;
		dY *= 0.97;
		
	}//updateLoc method
	
	public void trace(){
		x += 1;
		y = (otherY[x])-r-200;
	}//trace method
	public void newUpdate(){
		display.clear();
		
		if (!isPaused){
			
			
			
			
			
			
			
			
		} else {
			dX = 0;
			dY = 0;
		}
		
		
		display.addOutput("X: "+x+" Y: "+y, -1);
		display.addOutput("dX: "+dX + " dY: "+dY, -1);
		display.addOutput("HighestLeft: "+ otherY[highestY(otherY,x-r,x)] + "HighestRight: "+otherY[highestY(otherY,x,x+r)], -1);
		x += dX * speed;
		y += dY * speed;
		
//		dX *= 0.97;
//		dY *= 0.97;
	}//newUpdate method
	
	public void updateY(float yArray[]){
		otherY = yArray.clone();
		
	}//intersects method
	
	public int highestY (float yArray[],int low, int high){
		int highNum = low;
		
		for (int i=low+1;i<high;i++){
			if (yArray[i]<=yArray[highNum]){
				highNum = i;
			}
		}
		
		return highNum;
	}//highestY method 

	public float angleOf (float p1, float p2){
		float angle = 90;
//		float a = p2
		
		
		
		
		return angle;
	}//angleOf method
	
	public void reset(){
		x = 400;
		y = 50;
		dX = 0;
		dY = 0;
		isPaused = true;
	}//reset method
	
	
	
	
}//Ball class
