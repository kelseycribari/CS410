import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

//Class designed to represent a ray eminating from a pixel 
public class Ray {

	//base point
	RealVector pixPt;
	//direction vector
	RealVector Dv; 
	public float bestTVal; 
	public Sphere bestSphere; 
	RealVector bestPoint; 
	Face bestFace; 
	boolean isSphere; 
	boolean isFace;  
	
	public Ray() {
		bestTVal = (float)Double.MAX_VALUE; 
		isSphere = false; 
		isFace = false; 
	}
	
	public Ray(RealVector L, RealVector D) {
		double[] Ldata = {L.getEntry(0), L.getEntry(1), L.getEntry(2)}; 
		this.pixPt = MatrixUtils.createRealVector(Ldata); 
		double[] Ddata = {D.getEntry(0), D.getEntry(1), D.getEntry(2)}; 
		this.Dv = MatrixUtils.createRealVector(Ddata);
		bestTVal = (float)Double.MAX_VALUE;
		isSphere = false; 
		isFace = false; 
	}
}
