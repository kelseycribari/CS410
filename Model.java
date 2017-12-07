import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;


public class Model {

	private String modelFileName = ""; 
	private ArrayList<String> comments = new ArrayList<String>(); 
	private ArrayList<String> faceStrings = new ArrayList<String>(); 
	private ArrayList<Face> faces = new ArrayList<Face>(); 
	private ArrayList<Face> prePopulatedFaces = new ArrayList<Face>(); 
	ArrayList<RealVector> vertices = new ArrayList<RealVector>(); 
	RealMatrix modelMatrix; 
	RealMatrix transformedModelMatrix; 

	RealMatrix transformationMatrix; 
	String folderName; 
	
	String materialFileName; 
	Material material = new Material(); 
	ArrayList<Material> materials = new ArrayList<Material>(); 
	
	public int currentMaterialIndex = 0; 
	//public String currentMaterial = "";  
	
	boolean debug;  


	public Model(String modelFileName, RealMatrix transformationMatrix, String folderName, boolean debug) {
		this.modelFileName = modelFileName; 
		this.transformationMatrix = transformationMatrix; 
		this.folderName = folderName; 
		this.debug = debug; 
	}
	
	public Model(String modelFileName, RealMatrix transformationMatrix, boolean debug) {
		this.modelFileName = modelFileName; 
		this.transformationMatrix = transformationMatrix; 
		this.debug = debug; 
	}

	public Model(String modelFileName) {
		this.modelFileName = modelFileName; 
	}

	public void readFileAndPopulateFields() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(modelFileName));	
			String line = ""; 
			String[] lineArray; 

			while((line = reader.readLine()) != null) {
				lineArray = line.split(" ");

				if (line.charAt(0) == 'm') {
					this.materialFileName = lineArray[1]; 
					readMaterialFile(materialFileName); 
				}
				if (line.charAt(0) == 'v') {
					if (line.charAt(1) == 'n') {
						continue; 
					} else {
						RealVector vertex = new ArrayRealVector(4); 
						Double x = Double.parseDouble(lineArray[1]);
						vertex.addToEntry(0, x);
						Double y = Double.parseDouble(lineArray[2]);
						vertex.addToEntry(1, y);
						Double z = Double.parseDouble(lineArray[3]);
						vertex.addToEntry(2, z);
						vertex.addToEntry(3, 1.0);
						vertices.add(vertex); 
					}
				}
				if (line.charAt(0) == 'u') {
					String currentMaterial = lineArray[1]; 
					System.out.println(currentMaterial);
					int materialIndex = 0; 
					for (Material m : materials) {
						System.out.println(currentMaterial.equals(m.materialName));
						if (currentMaterial.equals(m.materialName)) {
							currentMaterialIndex = materialIndex; 
							System.out.println("here " +  currentMaterialIndex);
						} else {
							materialIndex++; 
						}
					}
				}
				if (line.charAt(0) == 'f') {
					//String [] lineArray = s.split(" "); 
					
					Face face = new Face(); 
					face.faceLine = lineArray; 
					face.material = materials.get(currentMaterialIndex); 
					double [] krArray = {1.0, 1.0, 1.0}; 
					RealVector kr = MatrixUtils.createRealVector(krArray); 
					face.material.kr = kr; 
					prePopulatedFaces.add(face); 
					//faceStrings.add(line); 
				}
				if (line.charAt(0) == '#') {
					comments.add(line);
				}

			}
			comments.add("#transformed version of " + modelFileName);

			createModelMatrix(); 

			reader.close(); 

		} catch (FileNotFoundException e) {
			System.err.println("Error: File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: Issue reading file.");
			e.printStackTrace();
		}
	}


	public void createFaces() {
		System.out.println("Model, faceStrings size: " + faceStrings.size()); 
		for (Face f : prePopulatedFaces) {
			//String [] lineArray = s.split(" "); 
			
			//Face face = new Face(); 
			String [] aArray = f.faceLine[1].split("//");
			String [] bArray = f.faceLine[2].split("//");
			String [] cArray = f.faceLine[3].split("//");
			
			//System.out.println(transformedModelMatrix.toString());
			System.out.println("creating faces");
			RealVector tempA = transformedModelMatrix.getColumnVector(Integer.parseInt(aArray[0]) - 1);
			f.aVector = tempA.getSubVector(0, 3);
			RealVector tempB = transformedModelMatrix.getColumnVector(Integer.parseInt(bArray[0]) - 1);
			f.bVector = tempB.getSubVector(0, 3); 
			RealVector tempC = transformedModelMatrix.getColumnVector(Integer.parseInt(cArray[0]) -1);
			f.cVector = tempC.getSubVector(0, 3);  
			faces.add(f); 
		}	

	}
	public void createModelMatrix() {
		modelMatrix = MatrixUtils.createRealMatrix(4, vertices.size());
		int index = 0; 
		for (RealVector v : vertices) {
			modelMatrix.setColumnVector(index, v);
			index++; 

		}
	}

	public void createTransformedModelMatrix() {
		transformedModelMatrix = transformationMatrix.multiply(modelMatrix); 
	}
	
	public void writeModelToFile() {
		String nameWithoutExtension = modelFileName.substring(0, modelFileName.length() - 4); 
		int count = 0; 
		boolean fileCreated = false;
		File file; 
		String name = ""; 
		while(!fileCreated) {
			name = folderName + "/" + nameWithoutExtension + "_mw0" + Integer.toString(count) + ".obj"; 
			file = new File(name); 
			if (file.exists()) {
				count++; 
				continue; 
			} else {
				fileCreated = true; 
			}
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			for (String c : comments) {
				writer.write(c);
				writer.write('\n');
			}
			for (int i = 0; i < transformedModelMatrix.getColumnDimension(); i++) {
				writer.write("v ");
				RealVector column = transformedModelMatrix.getColumnVector(i);
				double[] col = column.toArray(); 
				for (int j = 0; j < transformedModelMatrix.getRowDimension() - 1; j++) {
					if ((col[j] < 0.0001) && (col[j] > 0)) {
						col[j] = 0; 
					}
					String temp = ""; 
					temp = Double.toString(col[j]) + " "; 
					writer.write(temp);
				}
				writer.write("\n");
			}
//			for (Face f : faces) {
//				writer.write(f);
//				writer.write("\n");
//			}
			writer.flush(); 
			writer.close(); 
		} catch (IOException e) {
			System.err.println("Error writing file.");
			e.printStackTrace();
		}

	}
	
	public void readMaterialFile(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String l = ""; 
			String[] line;
			int materialCount = -1; 
			while((l = reader.readLine()) != null) {
				line = l.split(" ");
				switch(line[0]) {
				case "newmtl": 
					Material tempMaterial = new Material(); 
					tempMaterial.materialName = line[1]; 
					materials.add(tempMaterial); 
					materialCount++; 
					//System.out.println(materialName);
					break; 
				case "Ns":
					materials.get(materialCount).phong = Double.parseDouble(line[1]); 
					break; 
				case "Ka" :
					double [] Ka = {
						Double.parseDouble(line[1]), 
						Double.parseDouble(line[2]),
						Double.parseDouble(line[3])
					};
					materials.get(materialCount).ka = MatrixUtils.createRealVector(Ka); 
					break; 
				case "Kd" : 
					double [] Kd = {
						Double.parseDouble(line[1]), 
						Double.parseDouble(line[2]), 
						Double.parseDouble(line[3])
					};
					materials.get(materialCount).kd = MatrixUtils.createRealVector(Kd); 
					break; 
				case "Ks":
					double [] Ks = {
						Double.parseDouble(line[1]), 
						Double.parseDouble(line[2]),
						Double.parseDouble(line[3])
					};
					materials.get(materialCount).ks = MatrixUtils.createRealVector(Ks); 
					break; 
				case "Ke":
					double [] Ke = {
							Double.parseDouble(line[1]), 
							Double.parseDouble(line[2]), 
							Double.parseDouble(line[3])
					};
					materials.get(materialCount).ke = MatrixUtils.createRealVector(Ke);
					break; 
				case "Ni":
					materials.get(materialCount).Ni = Double.parseDouble(line[1]); 
					break; 
				case "d":
					materials.get(materialCount).d = Double.parseDouble(line[1]); 
					break;
				case "illum":
					materials.get(materialCount).illumination = Integer.parseInt(line[1]); 
					break; 
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error: Could not read material file.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading line of material file." );
			e.printStackTrace();
		} 
		
	}
	
	public ArrayList<Face> getFaces() {
		return this.faces; 
	}

//	public static void main(String[] args) throws FileNotFoundException {
//		Model model = new Model(args[0]);
//
//		model.readFileAndPopulateFields();
//		
//		double [][] mat = {
//				{1, 2, 3, 4, 5, 6}, 
//				{1, 2, 3, 4, 5, 6}, 
//				{1, 2, 3, 4, 5, 6}, 
//				{0, 0, 0, 0, 0, 0}
//		}; 
//		RealMatrix matrix = MatrixUtils.createRealMatrix(mat); 
//		
//
//
//	}
}



