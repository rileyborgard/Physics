package com.henagongames.physics.geom;

/**
 * A line segment, containing two endpoints
 * @author Riley
 */
public class Line {
	
	public Vector point1;
	public Vector point2;
	
	public Line(Vector point1, Vector point2) {
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public float length() {
		return (float) Math.hypot(point1.x-point2.x, point1.y-point2.y);
	}
	public Vector midPoint() {
		return new Vector(point1.x+point2.x, point1.y+point2.y).divide(2);
	}
	
}
