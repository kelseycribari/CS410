import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Light {
	
	//P
	RealVector worldCoordinates; 
	public int x; 
	public int y; 
	public int z; 
	public int w; 
	//E
	RealVector lightRGB;
	public double red; 
	public double green; 
	public double blue; 
	
	public Light () {
	
	}
	
	public void createWorldCoordinatesVector(double [] coordinates) {
		worldCoordinates = MatrixUtils.createRealVector(coordinates); 
	}
	
	public void createRGBVector(double [] RGB) {
		lightRGB = MatrixUtils.createRealVector(RGB); 
	}
	

}
