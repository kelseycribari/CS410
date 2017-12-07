import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;


public class Rotate {
	
	private double wx; 
	private double wy; 
	private double wz; 
	RealVector w; 
	private double theta; 
	
	RealMatrix rotationMatrix; 
	
	public Rotate(double wx, double wy, double wz, double theta) {
		this.wx = wx; 
		this.wy = wy; 
		this.wz = wz; 
		double[] wArray = {wx, wy, wz}; 
		w = MatrixUtils.createRealVector(wArray);
		w.unitize(); 
		this.theta = theta; 
	}
	
	public void buildRotationMatrix() {
		RealVector m = w.copy();
		int j = 0; 
		for (int i = 0; i < 3; i++) {
			if (m.getEntry(i) == w.getMinValue()) {
				j = i; 
			}
		}
		
		m.setEntry(j, 1.0); 

		RealVector u = VectorUtils.crossProduct(w, m); 
		u.unitize();
		RealVector v = VectorUtils.crossProduct(w, u); 
		w = w.append(0.0); 
		u = u.append(0.0);
		v = v.append(0.0);
		
		double [] bottomLine = {0.0, 0.0, 0.0, 1.0};
		RealVector bottom = MatrixUtils.createRealVector(bottomLine); 
		
		RealMatrix Rw = MatrixUtils.createRealMatrix(4, 4); 
		Rw.setRowVector(0, u);
		Rw.setRowVector(1, v);
		Rw.setRowVector(2, w);
		Rw.setRowVector(3, bottom);
		
		double thetaRadians = Math.toRadians(theta); 
		
		double [] z1Array = {Math.cos(thetaRadians), -1*Math.sin(thetaRadians), 0.0, 0.0};
		RealVector z1 = MatrixUtils.createRealVector(z1Array);
		double [] z2Array = {Math.sin(thetaRadians), Math.cos(thetaRadians), 0.0, 0.0};
		RealVector z2 = MatrixUtils.createRealVector(z2Array);
		double [] z3Array = {0.0, 0.0, 1.0, 0.0};
		RealVector z3 = MatrixUtils.createRealVector(z3Array);
		double [] z4Array = {0.0, 0.0, 0.0, 1.0}; 
		RealVector z4 = MatrixUtils.createRealVector(z4Array);
		
		
		
		RealMatrix ZTheta = MatrixUtils.createRealMatrix(4, 4); 
		ZTheta.setRowVector(0, z1);
		ZTheta.setRowVector(1, z2);
		ZTheta.setRowVector(2, z3);
		ZTheta.setRowVector(3, z4);
		
		
		RealMatrix RwTranspose = Rw.transpose(); 
		
		RealMatrix RwTZTheta = RwTranspose.multiply(ZTheta);
		rotationMatrix = RwTZTheta.multiply(Rw); 
		
	}
	
	public RealMatrix getRotationMatrix() {
		return this.rotationMatrix; 
	}
	
	public static void main(String [] args) {
		Rotate rotate = new Rotate(10.0, 10.0, 10.0, 45);
		rotate.buildRotationMatrix();
		System.out.println(rotate.rotationMatrix.toString());
	}
	
	

}
