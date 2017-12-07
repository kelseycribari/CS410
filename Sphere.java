import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Sphere {
	
	RealVector center; 
	public double radius; 
	Material material; 
	
	
	public Sphere() {
		
	}
	
	public void setCenterVector(double [] centerArray) {
		center = MatrixUtils.createRealVector(centerArray);
	}
	
	

}
