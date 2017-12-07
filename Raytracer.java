import java.io.IOException;
import java.util.ArrayList;

public class Raytracer {

	Camera camera; 
	Image image; 
	ArrayList<Sphere> spheres; 
	ArrayList<Transformation> transformations; 
	ArrayList<Model> models; 
	ArrayList<Face> aggregatedFaces; 
	ArrayList<Light> sphereLights; 
	Ambient ambient; 
	
	public Raytracer() {
		camera = new Camera(); 
		spheres = new ArrayList<Sphere>(); 
		transformations = new ArrayList<Transformation>();
		models = new ArrayList<Model>(); 
		aggregatedFaces = new ArrayList<Face>(); 
		ambient = new Ambient(); 
	}
	
	
	
	public static void main(String [] args) throws IOException {
		String driverFileName = args[0];
		boolean debug = true; 
		
		Raytracer raytracer = new Raytracer(); 
		
		DriverReader driverReader = new DriverReader(driverFileName);
		
		driverReader.readFile(raytracer.camera, raytracer.ambient); 
		raytracer.spheres = driverReader.getSpheres(); 
		raytracer.transformations = driverReader.getTransformations(); 
		
		raytracer.camera.createWVector(); 
		raytracer.camera.createUVector(); 
		raytracer.camera.createVVector();
		
		for (Transformation t : raytracer.transformations) {
			Model model = new Model(t.modelFileName, t.getTransformationMatrix(), debug);
			
			model.readFileAndPopulateFields(); 
			model.createTransformedModelMatrix();
			model.createFaces(); 
			for (Face f : model.getFaces()) {
				raytracer.aggregatedFaces.add(f);
			}
		}
		
		if (debug) { System.out.println("RayTracer, size of aggregatedFaces: " + raytracer.aggregatedFaces.size()); }
		raytracer.sphereLights = driverReader.getLights(); 
		//System.out.println(raytracer.sphereLights.size());
		raytracer.image = new Image(raytracer.camera, raytracer.aggregatedFaces, raytracer.spheres, args[0], args[1], raytracer.ambient, raytracer.sphereLights, debug); 
		
		if (debug) { System.out.println("Getting here.");} 
		raytracer.image.calculatePixelCoordinates();
		
		
	}
	
	
	
}
