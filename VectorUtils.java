import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class VectorUtils {

	public VectorUtils() {

	}

	public static RealVector crossProduct(RealVector u, RealVector v) {

		//clever way to calculate the cross products found here:
		//http://www.sanfoundry.com/java-program-compute-cross-product-two-vectors/

		double u1, u2, u3, v1, v2, v3; 
		u1 = u.getEntry(0); 
		u2 = u.getEntry(1); 
		u3 = u.getEntry(2); 

		v1 = v.getEntry(0); 
		v2 = v.getEntry(1); 
		v3 = v.getEntry(2); 

		double uvi, uvj, uvk; 
		uvi = u2 * v3 - v2 * u3; 
		uvj = v1 * u3 - u1 * v3; 
		uvk = u1 * v2 - v1 * u2;

		double[] result = {uvi, uvj, uvk}; 

		RealVector cross_product = MatrixUtils.createRealVector(result); 

		return cross_product; 

	}

	public static double determinant(RealMatrix matrix) {
		double determinant = 0.0; 
		
		double a = matrix.getEntry(0, 0);
		double b = matrix.getEntry(0, 1);
		double c = matrix.getEntry(0, 2);
		double d = matrix.getEntry(1, 0);
		double e = matrix.getEntry(1, 1);
		double f = matrix.getEntry(1, 2);
		double g = matrix.getEntry(2, 0);
		double h = matrix.getEntry(2, 1);
		double i = matrix.getEntry(2, 2); 
//		
//		double firstHalf = ((matrix.getEntry(0, 0) * matrix.getEntry(1,  1) * matrix.getEntry(2,  2)) 
//				+ (matrix.getEntry(0, 1) * matrix.getEntry(1,  2) * matrix.getEntry(2,  0)) 
//				+ (matrix.getEntry(0, 2) * matrix.getEntry(1,  0) * matrix.getEntry(2,  1))); 
//
//		double secondHalf = ((matrix.getEntry(0, 2) * matrix.getEntry(1,  1) * matrix.getEntry(2,  0))
//				+ (matrix.getEntry(0,  1) * matrix.getEntry(1,  0) * matrix.getEntry(2,  2)) 
//				+ (matrix.getEntry(0, 0) * matrix.getEntry(1,  2) * matrix.getEntry(2,  1))); 

		return ((a*e*i) + (b*f*g) + (c*d*h)) - ((c*e*g) + (b*d*i) + (a*f*h));
 

	}

	public static void main(String[] args) {
		double [][] matrix = {
				{2, 1, 3},
				{-1, 2, 3}, 
				{4, -5, 0}
		};

		RealMatrix m = MatrixUtils.createRealMatrix(matrix); 

		double det = determinant(m); 
		System.out.println(det);
	}
}

