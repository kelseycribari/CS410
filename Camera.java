import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Camera {
	
	RealVector eye; public double ex, ey, ez;  
 
	RealVector look; public double lx, ly, lz;  
	
	public double d = 0.0; 
	
	public double leftBound, rightBound, bottomBound, topBound; 
	
	public int width, height; 

	RealVector up; 
	RealVector W, U, V; 
	
	public int recursionLevel; 
	
	public Camera() {
		
	}
	
	public void createEyeVector() {
		double [] eyeArray = {ex, ey, ez}; 
		eye = MatrixUtils.createRealVector(eyeArray); 
	}
	
	public void createLookVector() {
		double [] lookArray = {lx, ly, lz}; 
		look = MatrixUtils.createRealVector(lookArray);
	}
	
	public void createUpVector(double [] upArray) {
		up = MatrixUtils.createRealVector(upArray); 
	}
	
	public void createWVector() {
		W = eye.subtract(look);
		W.unitize(); 
	}
	
	public void createUVector() {
		U = VectorUtils.crossProduct(up, W);
//		U = U.mapMultiply(-1); 
		U.unitize(); 
	}
	
	public void createVVector() {
		V = VectorUtils.crossProduct(W, U);
	//	V = V.mapMultiply(-1); 
	}
}
