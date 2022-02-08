package com.henagongames.physics.geom;

/**
 * A shape representing a circle, containing a position and a radius
 * @author Riley
 */
public class Circle extends Shape {
	
	public float radius;
	
	public Circle(Vector position, float radius) {
		super(position);
		this.radius = radius;
	}
	
	public float area() {
		return (float) (Math.PI*radius*radius);
	}
	public float momentOfInertia() {
		return (float) ((Math.PI/2)*Math.pow(radius, 4));
		//return (float) Math.pow(radius, 2)*area()/2;
	}
	public Vector centerOfMass() {
		return position;
	}
	public Shape copy() {
		return new Circle(position, radius);
	}
	public Shape rotate(float theta) {
		return this;
	}
	
}
