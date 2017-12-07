import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

public class Material {
	
	RealVector ka = null; 
	RealVector kd = null; 
	RealVector ks = null; 
	RealVector kr = null; 
	RealVector ke = null; 
	
	public double phong; 
	public double illumination; 
	public double d; 
	public double Ni; 
	
	RealVector ambientRGB; 
	
	public String materialName; 
	
	public Material() {
		double [] krArray = {1, 1, 1}; 
		kr = MatrixUtils.createRealVector(krArray); 
	}
	
//	public void readMaterialFile(String filename) {
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(filename));
//			String l = ""; 
//			String[] line;
//			while((l = reader.readLine()) != null) {
//				line = l.split(" ");
//				switch(line[0]) {
//				case "newmtl": 
//					materialName = line[1]; 
//					//System.out.println(materialName);
//					break; 
//				case "Ns":
//					phong = Double.parseDouble(line[1]); 
//					break; 
//				case "Ka" :
//					double [] Ka = {
//						Double.parseDouble(line[1]), 
//						Double.parseDouble(line[2]),
//						Double.parseDouble(line[3])
//					};
//					this.ka = MatrixUtils.createRealVector(Ka); 
//					break; 
//				case "Kd" : 
//					double [] Kd = {
//						Double.parseDouble(line[1]), 
//						Double.parseDouble(line[2]), 
//						Double.parseDouble(line[3])
//					};
//					this.kd = MatrixUtils.createRealVector(Kd); 
//					break; 
//				case "Ks":
//					double [] Ks = {
//						Double.parseDouble(line[1]), 
//						Double.parseDouble(line[2]),
//						Double.parseDouble(line[3])
//					};
//					this.ks = MatrixUtils.createRealVector(Ks); 
//					break; 
//				case "Ke":
//					double [] Ke = {
//							Double.parseDouble(line[1]), 
//							Double.parseDouble(line[2]), 
//							Double.parseDouble(line[3])
//					};
//					this.ke = MatrixUtils.createRealVector(Ke);
//					break; 
//				case "Ni":
//					this.Ni = Double.parseDouble(line[1]); 
//					break; 
//				case "d":
//					this.d = Double.parseDouble(line[1]); 
//					break;
//				case "illum":
//					this.illumination = Integer.parseInt(line[1]); 
//					break; 
//				}
//			}
//		} catch (FileNotFoundException e) {
//			System.err.println("Error: Could not read material file.");
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.err.println("Error reading line of material file." );
//			e.printStackTrace();
//		} 
//		
//	}
	
	public void setKaVector(double [] kaArray) {
		ka = MatrixUtils.createRealVector(kaArray); 
	}
	
	public void setKdVector(double [] kdArray) {
		kd = MatrixUtils.createRealVector(kdArray); 
	}
	
	public void setKsVector(double [] ksArray) {
		ks = MatrixUtils.createRealVector(ksArray); 
	}
	
	public void setKrVector(double [] krArray) {
		kr = MatrixUtils.createRealVector(krArray); 
	}

}
