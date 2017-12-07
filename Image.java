import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Image {

	public ArrayList<RealVector> pixelPoints;
	Camera camera; 
	public ArrayList<Face> faces; 
	public ArrayList<Sphere> spheres; 
	public ArrayList<Double> tVals; 
	public ArrayList<Ray> sphereRays;
	public ArrayList<Ray> faceRays; 
	public ArrayList<Light> sphereLights; 
	public double tmin; 
	public double tmax; 
	public String driverFileName; 
	public String outputFileName; 
	boolean debug; 
	private double [][] tValues; 
	private Ray [][] intersections; 
	Ambient ambient; 
	public int recursionLevel; 
	int recursionCount = 0; 
	RealVector accum; 



	public Image(Camera camera, ArrayList<Face> faces, ArrayList<Sphere> spheres, String driverFileName, String outputFileName, Ambient ambient, ArrayList<Light> sphereLights, boolean debug) {
		pixelPoints = new ArrayList<RealVector>(); 
		this.camera = camera; 
		this.faces = faces; 
		this.spheres = spheres; 
		tVals = new ArrayList<Double>(); 
		tmin = Double.MAX_VALUE;
		tmax = 0.0; 
		this.driverFileName = driverFileName; 
		this.outputFileName = outputFileName; 
		this.debug = debug; 
		tValues = new double[camera.width][camera.height];
		intersections = new Ray[camera.width][camera.height];
		this.ambient = ambient; 
		this.sphereLights = sphereLights; 
		this.recursionLevel = this.camera.recursionLevel; 
		double[] acc = {0.0, 0.0, 0.0}; 
		accum = MatrixUtils.createRealVector(acc); 
	}


	public void calculatePixelCoordinates() throws IOException { 

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		writer.write("P3");
		writer.write("\n");
		writer.write(camera.width + " " + camera.height + " " + "255\n");
		System.out.println("Writing to file");

		for (int i = 0; i < camera.width; i++) {
			for (int j = 0; j < camera.height; j++) {
				Ray ray = pixelRay(i, (camera.height - j - 1), camera);
				
				double [] rgbA = {0.0, 0.0, 0.0}; 
				RealVector rgb = MatrixUtils.createRealVector(rgbA);
				double [] attenuation = {1.0, 1.0, 1.0}; 
				RealVector att = MatrixUtils.createRealVector(attenuation);
				int rL = recursionLevel; 

				RealVector color = rayTrace(ray, rgb, att, rL); 

				int r = 0; 
				int g = 0; 
				int b = 0; 
	
				double [] coloredPixels = color.toArray(); 
				for (int q = 0; q < coloredPixels.length; q++) {
					coloredPixels[q] = Math.min(255, 255 * coloredPixels[q]);
				}
				r = (int)Math.round(coloredPixels[0]); 
				g = (int)Math.round(coloredPixels[1]); 
				b = (int)Math.round(coloredPixels[2]); 
				writer.write(r + " " + g + " " + b + " ");

			}

			writer.write("\n");

		}
		writer.flush(); 
		writer.close(); 

	}

	public Ray rayFind(Ray ray) {
		double currentT = 0.0;
		ray.isSphere = false; 
		ray.isFace = false; 

		currentT = calculateTriangleIntersections(ray);
		
		if (currentT == Double.MAX_VALUE) {
			currentT = 0.0; 
		}

		if (currentT > tmax) {
			tmax = currentT; 
		}
		if ((currentT < tmin) && (currentT > 0)) {
			tmin = currentT; 
		}
		//System.out.println(currentT);
		if ((currentT < ray.bestTVal) && (currentT > 0.00001)) {
			ray.bestTVal = (float)currentT; 
			ray.bestPoint = ray.pixPt.add(ray.Dv.mapMultiply(currentT));

			return ray; 
		}

		return null; 



	}

	public RealVector rayTrace(Ray ray, RealVector accum, RealVector atten, int recursionDepth) {

		Ray r = rayFind(ray); 
		
		if (r != null) {
			System.out.println(r.Dv);
			RealVector sn; 

			if (r.isFace) {
				sn = VectorUtils.crossProduct((r.bestFace.aVector.subtract(r.bestFace.cVector)), (r.bestFace.aVector.subtract(r.bestFace.bVector)));
			} else {
				sn = r.bestPoint.subtract(r.bestSphere.center);
			}
			sn.unitize();
			Material mat; 
			if (r.isSphere) {
				mat = r.bestSphere.material;
			} else {
				mat = r.bestFace.material;
			}

			RealVector color = ambient.ambientRGB.ebeMultiply(mat.ka);
			RealVector toC = r.Dv.mapMultiply(-1.0);
			toC.unitize();
			for (Light l : sphereLights) {

				RealVector toL = l.worldCoordinates.subtract(r.bestPoint);
				toL.unitize(); 
				double nDotL = sn.dotProduct(toL); 
				if (nDotL < 0.0 && r.isFace) {
					sn = sn.mapMultiply(-1.0); 
					sn.unitize(); 
					nDotL = sn.dotProduct(toL);
				}
				boolean shadow = shadow(l, r.bestPoint); 
				if (nDotL > 0.0 && (!shadow)) {
					color = color.add(mat.kd.ebeMultiply(l.lightRGB).mapMultiply(nDotL));
					RealVector spR = (sn.mapMultiply(2 * nDotL)).subtract(toL); 
					spR.unitize(); 
					double CdR = toC.dotProduct(spR); 
					if (CdR > 0.0) {
						if (mat.phong != 0.0) {
							color = color.add((mat.ks.ebeMultiply(l.lightRGB).mapMultiply(Math.pow(CdR, mat.phong))));
						} else {
							color = color.add((mat.ks.ebeMultiply(l.lightRGB).mapMultiply(Math.pow(CdR, 16))));
						}

					}
				}
			}

			RealVector temp = atten.ebeMultiply(color); 
			accum = accum.add(temp);
			if (recursionDepth > 0) {
				toC = r.Dv.mapMultiply(-1.0); 
				double s = sn.dotProduct(toC) * 2.0; 
				RealVector s2 = sn.mapMultiply(s);
				RealVector refR = s2.subtract(toC); 
				refR.unitize(); 
				Ray ray2 = new Ray(r.bestPoint, refR); 
				return rayTrace(ray2, accum, mat.kr.ebeMultiply(atten), (recursionDepth - 1)); 
			}

		}
		return accum;
	}

	public boolean shadow(Light light, RealVector bestPoint) {
		RealVector pointToLight = light.worldCoordinates.subtract(bestPoint); 
		RealVector pTL = pointToLight; 
		pTL.unitize();
		Ray r = new Ray(bestPoint, pointToLight); 
		float dP = (float)pTL.dotProduct(pointToLight); 
		r = rayFind(r); 

		if (r != null && r.bestTVal < dP) {
			return true; 
		} else {
			return false; 
		}


	}

	public Ray pixelRay(int i, int j, Camera cam) {
		double px = 0.0; 
		double py = 0.0; 

		Ray ray = new Ray(); 

		RealVector pixpt; 

		px = (double)((j / (cam.width - 1.0) * (cam.leftBound - cam.rightBound) + cam.rightBound)); 
		py = (double)((i / (cam.height - 1.0) * (cam.bottomBound - cam.topBound) + cam.topBound));

		RealVector dW = camera.W.mapMultiply(camera.d); 
		RealVector pxU = camera.U.mapMultiply(px); 
		RealVector pyV = camera.V.mapMultiply(py); 
		pixpt = camera.eye.add(dW).add(pxU).add(pyV);
		ray.pixPt = pixpt; 

		ray.Dv = pixpt.subtract(cam.eye); 
		ray.Dv.unitize(); 

		return ray; 

	}

	public double calculateTriangleIntersections(Ray ray) {  

		float currentTValue = (float) Double.MAX_VALUE;

		for (Face f : faces) {

			RealVector Yv = f.aVector.subtract(ray.pixPt); 
			RealMatrix MM = MatrixUtils.createRealMatrix(3, 3);
			RealVector AvBv = f.aVector.subtract(f.bVector);
			MM.setColumnVector(0, AvBv);
			RealVector AvCv = f.aVector.subtract(f.cVector);
			MM.setColumnVector(1, AvCv);
			MM.setColumnVector(2, ray.Dv);


			RealMatrix MMs1 = MM.copy(); RealMatrix MMs2 = MM.copy(); RealMatrix MMs3 = MM.copy(); 
			MMs1.setColumnVector(0, Yv);
			MMs2.setColumnVector(1, Yv);
			MMs3.setColumnVector(2, Yv);

			double detMM = VectorUtils.determinant(MM); 
			double detMMs1 = VectorUtils.determinant(MMs1);
			double detMMs2 = VectorUtils.determinant(MMs2); 
			double detMMs3 = VectorUtils.determinant(MMs3); 


			double sBeta = detMMs1 / detMM; 
			double sGamma = detMMs2 / detMM; 
			float sTVal = (float) (detMMs3 / detMM);  

			float beta = (float) sBeta; 
			float gamma = (float) sGamma; 
			
			if ((beta >= 0.0) && (gamma >= 0.0) && (beta + gamma <= 1.0) && (sTVal > 0.0001)) {
				if (sTVal < currentTValue) {
					//System.out.println(sTVal);
					currentTValue = sTVal; 
					ray.bestFace = f;
					ray.isSphere = false;
					ray.isFace = true; 
				}
			}
		}


		for (Sphere s : spheres) {
			float sphereTValue = (float)calculateSphereIntersection(ray, s); 
			if ((sphereTValue < currentTValue) && (sphereTValue > 0.0)) {
				currentTValue = sphereTValue; 
				ray.bestSphere = s; 
				ray.isSphere = true; 
				ray.isFace = false; 
			}
		}

		return currentTValue; 

	}

	public double calculateSphereIntersection(Ray ray, Sphere s) {
		double tValue = 0.0;

		RealVector Tv = s.center.subtract(ray.pixPt); 
		double v = Tv.dotProduct(ray.Dv); 
		double csquared = Tv.dotProduct(Tv); 

		double d = (Math.pow(s.radius, 2)) - (csquared - Math.pow(v, 2)); 

		if (d > 0.0) {
			d = Math.sqrt(d);
			tValue = v - d; 
		}
		return tValue; 
	}

	

	



}
