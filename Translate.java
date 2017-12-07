import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Translate {
	
	public RealMatrix translationMatrix; 
	double[] translate = new double[3]; 
	double tx = 0.0;
	double ty = 0.0; 
	double tz = 0.0; 

	
	public Translate(double tx, double ty, double tz) {
		this.tx = tx; 
		this.ty = ty; 
		this.tz = tz; 
		translate[0] = this.tx;
		translate[1] = this.ty;
		translate[2] = this.tz;
	}
	
	public void buildTranslationMatrix() {
		 
		double[][] tArray = {
				{1, 0, 0, translate[0]},
				{0, 1, 0, translate[1]}, 
				{0, 0, 1, translate[2]}, 
				{0, 0, 0, 1}, 
		}; 
		
		translationMatrix = MatrixUtils.createRealMatrix(tArray); 
		return; 
	}
	
	public RealMatrix getTranslationMatrix() {
		return this.translationMatrix; 
	}
}
