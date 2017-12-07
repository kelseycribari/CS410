import java.util.ArrayList;
import java.util.Scanner;


public class Modeltoworld {

	
	public static void main(String [] args) {
		
		String driverFileName = args[0]; 
		DriverReader driverReader = new DriverReader(driverFileName);
		
//		ArrayList<Transformation> transformations = new ArrayList<Transformation>(); 
//		transformations = driverReader.readFile();
//		
//		String folderName = driverReader.createFolder();
//		
//		ArrayList<Model> models = new ArrayList<Model>(); 
//		
//		for (Transformation t : transformations) {
//			Model model = new Model(t.modelFileName, t.getTransformationMatrix(), folderName);
//			model.readFileAndPopulateFields(); 
//			model.createTransformedModelMatrix();
//			model.writeModelToFile();
//		}
		
	}
}
