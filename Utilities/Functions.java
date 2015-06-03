package Utilities;

public class Functions {
	/*
	 * Similar to how the Draw class contains many
	 * different draw methods, the Utilities class
	 * contains many different Utilities.
	 */
	
	public static double circleValue(float angle,boolean coord){
		/*
		 * Runs a series of calculations to determine
		 * the x and y coordinates of a angle 'angle.'
		 * This can be used to create a circle, since
		 * this method returns x and y lengths with a
		 * hypotenuse of 1. Always.
		 * circleValue returns x length if 'coord' is
		 * true, and y length if 'coord' is false.
		 */
		
		///////////////DETERMINES X LENGTH/////////////////
		float angleX = angle;
		float percentX;
		boolean negativeX = false;
		
		if (angleX<0)angleX*=-1;
		if (angleX>90){
			angleX = (angleX - (2*(angleX-90)));
			negativeX = true;
		}
		if (negativeX){
			percentX = (90-angleX)/-90;
		} else {
			percentX = (90-angleX)/90;
		}
		///////////////DETERMINES Y LENGTH/////////////////
		float angleY = angle;
		float percentY;
		if (angleY<-90){
			angleY = angleY + (2*(-90-angleY));
		}	
		if (angleY>90){
			angleY = angleY - (2*(angleY-90));
		}
		percentY = angleY/90;
		//////////TRANSFORMATION INTO 'CIRCLE'/////////////
		
		
		if (coord){
			return percentX/Math.hypot(percentX,percentY); 
		} else {
			return percentY/Math.hypot(percentX,percentY);
		}
		
	}//circleValue method
	
}//Functions class
