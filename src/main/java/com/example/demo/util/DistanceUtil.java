package com.example.demo.util;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

/**
 * calculation formulas from https://www.movable-type.co.uk/scripts/latlong.html
 */
public class DistanceUtil {
	/**
	 * @param latitude1 latitude of a 1st point
	 * @param latitude2 latitude of a 2nd point
	 * @param longitude1 longitude of a 1st point
	 * @param longitude2 latitude of a 2nd point
	 * @return
	 */
	public static double calculate(double latitude1, double longitude1, double latitude2, double longitude2) {
		double radius = 6371e3;
		double phi1 = toRadians(latitude1);
		double phi2 = toRadians(latitude2);
		double phiDelta = toRadians(latitude2 - latitude1);
		double lambdaDelta = toRadians(longitude2 - longitude1);
		double a = sin(phiDelta / 2) * sin(phiDelta / 2)
				+ cos(phi1) * cos(phi2) * sin(lambdaDelta / 2) * sin(lambdaDelta / 2);
		double c = 2 * Math.atan2(sqrt(a), sqrt(1 - a));
		return radius * c;
	}

//	ObjectPositionProcessed(object=object5, latitude1=-64.9877084114726861, longitude1=-43.28087365918708562, latitude2=-65.35484481757052067,
//	                        longitude2=-43.31440592517564647, from=1579618448, to=1579616528, distance=5562281.190128438, speed=-1.5796165279999983E12)

	public static void main(String[] args) {
		System.out.println(calculate(-65.0586251145940252, -43.0327354744829119, -64.6171370744153785, -43.30522132087171316));
	}

}
