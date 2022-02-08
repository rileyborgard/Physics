package com.henagongames.physics.geom;

/**
 * Abstract class representing any 2-Dimensional shape
 * @author Riley
 */
public abstract class Shape {
	
	public Vector position;
	public Object userData;
	
	public Shape(Vector position) {
		this.position = position;
	}
	
	public abstract float area();
	public abstract float momentOfInertia();
	public abstract Vector centerOfMass();
	public abstract Shape copy();
	public abstract Shape rotate(float theta);
	
}
