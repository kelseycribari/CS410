import org.apache.commons.math3.linear.RealMatrix;

public class Transformation {
	public double wx = 0.0;
	public double wy = 0.0; 
	public double wz = 0.0; 
	
	public double theta = 0.0; 
	public double scale = 0.0; 
	public double tx = 0.0; 
	public double ty = 0.0; 
	public double tz = 0.0; 

	
	public String modelFileName = ""; 
	
	public Rotate rotation; 
	public Scale scaling; 
	public Translate translation; 
	
	RealMatrix transformationMatrix; 
	
	
	public Transformation() {
	
	}
	
	public void buildTransformationMatrix() {
		rotation = new Rotate(wx, wy, wz, theta); 
		scaling = new Scale(scale); 
		translation = new Translate(tx, ty, tz); 
		
		rotation.buildRotationMatrix(); 
		scaling.buildScaleMatrix();
		translation.buildTranslationMatrix(); 
		
		transformationMatrix = (translation.getTranslationMatrix().multiply(scaling.getScaleMatrix())).multiply(rotation.getRotationMatrix());
		
	}
	
	public RealMatrix getTransformationMatrix() {
		return this.transformationMatrix; 
	}
}
