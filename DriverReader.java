import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.math3.linear.MatrixUtils;

public class DriverReader {
	
	private String driverFileName = ""; 
	private String folderName = ""; 
	ArrayList<Sphere> spheres = new ArrayList<Sphere>(); 
	ArrayList<Transformation> transformations = new ArrayList<Transformation>(); 
	ArrayList<Light> lights = new ArrayList<Light>(); 
	public DriverReader(String fileName) {
		driverFileName = fileName; 
	}
	
	public void readFile(Camera camera, Ambient ambient) {
		//ArrayList<Transformation> transformations = new ArrayList<Transformation>(); 
		String [] line; 
		try {
			Scanner scan = new Scanner(new File(driverFileName));
			while (scan.hasNext()) {
				line = scan.nextLine().split(" ");
				switch (line[0]) {
				case "eye":
					camera.ex = Double.parseDouble(line[1]);
					camera.ey = Double.parseDouble(line[2]); 
					camera.ez = Double.parseDouble(line[3]); 
					camera.createEyeVector();
					break; 
				case "look":
					camera.lx = Double.parseDouble(line[1]); 
					camera.ly = Double.parseDouble(line[2]);
					camera.lz = Double.parseDouble(line[3]);
					camera.createLookVector(); 
					break; 
				case "up": 
					double [] upArray = new double[3]; 
					upArray[0] = Double.parseDouble(line[1]);
					upArray[1] = Double.parseDouble(line[2]); 
					upArray[2] = Double.parseDouble(line[3]); 
					camera.createUpVector(upArray);
					break; 
				case "d":
					double temp = Double.parseDouble(line[1]);
					camera.d = -temp; 
					break; 
				case "bounds": 
					camera.leftBound = Double.parseDouble(line[1]); 
					camera.bottomBound = Double.parseDouble(line[2]); 
					camera.rightBound = Double.parseDouble(line[3]); 
					camera.topBound = Double.parseDouble(line[4]); 
					break; 
				case "res":
					camera.width = Integer.parseInt(line[1]);
					camera.height = Integer.parseInt(line[2]);
					break; 
				case "recursionLevel":
					camera.recursionLevel = Integer.parseInt(line[1]); 
					break; 
				case "ambient":
					ambient.red = Double.parseDouble(line[1]);
					ambient.green = Double.parseDouble(line[2]);
					ambient.blue = Double.parseDouble(line[3]);
					double [] rgb = {ambient.red, ambient.green, ambient.blue}; 
					ambient.ambientRGB = MatrixUtils.createRealVector(rgb); 
					break; 
				case "light":
					Light light = new Light(); 
					double [] worldCoordinates = new double[3]; 
					light.x = Integer.parseInt(line[1]);
					worldCoordinates[0] = light.x;
					light.y = Integer.parseInt(line[2]); 
					worldCoordinates[1] = light.y;
					light.z = Integer.parseInt(line[3]); 
					worldCoordinates[2] = light.z;
					light.w = Integer.parseInt(line[4]); 
					light.createWorldCoordinatesVector(worldCoordinates); 
					
					double[] RGB = new double[3]; 
					light.red = Double.parseDouble(line[5]); 
					light.green = Double.parseDouble(line[6]); 
					light.blue = Double.parseDouble(line[7]); 
					RGB[0] = light.red; 
					RGB[1] = light.green; 
					RGB[2] = light.blue; 
					light.createRGBVector(RGB);
					
					lights.add(light); 
					break; 
				case "sphere": 
					double [] centerArray = new double[3]; 
					Sphere sphere = new Sphere(); 
					Material material = new Material(); 
					centerArray[0] = Double.parseDouble(line[1]);
					centerArray[1] = Double.parseDouble(line[2]);
					centerArray[2] = Double.parseDouble(line[3]);
					sphere.radius = Double.parseDouble(line[4]);
					sphere.setCenterVector(centerArray);
					
					double[] kaArray = { 
							Double.parseDouble(line[5]), 
							Double.parseDouble(line[6]), 
							Double.parseDouble(line[7])
					};
					material.setKaVector(kaArray); 
					
					double[] kdArray = { 
						Double.parseDouble(line[8]), 
						Double.parseDouble(line[9]),
						Double.parseDouble(line[10]) 
					};
					material.setKdVector(kdArray); 
					
					double[] ksArray = { 
							Double.parseDouble(line[11]),
							Double.parseDouble(line[12]),
							Double.parseDouble(line[13])
					};
					material.setKsVector(ksArray);
					
					double[] krArray = { 
							Double.parseDouble(line[14]),  
							Double.parseDouble(line[15]),  
							Double.parseDouble(line[16])
					};
					material.setKrVector(krArray);
					sphere.material = material; 
					sphere.material.phong = 16; 
					
					
					spheres.add(sphere);
					break; 
				case "model": 
					Transformation transformation = new Transformation(); 
					transformation.wx = Double.parseDouble(line[1]);
					transformation.wy = Double.parseDouble(line[2]);
					transformation.wz = Double.parseDouble(line[3]);
					transformation.theta = Double.parseDouble(line[4]);
					transformation.scale = Double.parseDouble(line[5]);
					transformation.tx = Double.parseDouble(line[6]);
					transformation.ty = Double.parseDouble(line[7]);
					transformation.tz = Double.parseDouble(line[8]);
					transformation.modelFileName = line[9];
					transformation.buildTransformationMatrix();
					transformations.add(transformation);
					break; 
				case "#":
					break; 
				default: 
					System.err.println("ERR (DriverReader): Something went wrong while reading the driver file. "
							+ "Check the formatting and try running again."); 
					break; 
				}
				
			}	
				
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Sphere> getSpheres() {
		return spheres; 
	}
	public ArrayList<Transformation> getTransformations() {
		return transformations; 
	}
	
	public ArrayList<Light> getLights() {
		return lights; 
	}
	
	public String createFolder() {
		folderName = driverFileName.substring(0, driverFileName.length() - 4);
		new File(folderName).mkdir(); 
		
		return folderName; 
	}
}
