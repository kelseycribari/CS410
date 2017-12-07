import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Scale {
	
	private double scale; 
	RealMatrix scaleMatrix; 
	
	public Scale(double scale) {
		this.scale = scale; 
	}
	
	public void buildScaleMatrix() {
		scaleMatrix = MatrixUtils.createRealIdentityMatrix(4);
		scaleMatrix = scaleMatrix.scalarMultiply(scale); 
		scaleMatrix.setEntry(3, 3, 1);
	}
	
	public RealMatrix getScaleMatrix() {
		return this.scaleMatrix; 
	}
	
	public static void main(String[] args) {
		Scale scale = new Scale(3); 
		scale.buildScaleMatrix();
		System.out.println(scale.scaleMatrix.toString());
	}
	
	

}
