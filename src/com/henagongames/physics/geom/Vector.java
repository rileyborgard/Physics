package com.henagongames.physics.geom;

/**
 * A 2-Dimensional vector, in the form of (x, y)
 * @author Riley
 */
public class Vector {
	
	public float x;
	public float y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector v) {
		return new Vector(x+v.x, y+v.y);
	}
	public Vector subtract(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	public Vector multiply(Vector v) {
		return new Vector(x*v.x, y*v.y);
	}
	public Vector divide(Vector v) {
		return new Vector(x/v.x, y/v.y);
	}
	
	public Vector add(float f) {
		return new Vector(x+f, y+f);
	}
	public Vector subtract(float f) {
		return new Vector(x-f, y-f);
	}
	public Vector multiply(float f) {
		return new Vector(x*f, y*f);
	}
	public Vector divide(float f) {
		return new Vector(x/f, y/f);
	}
	
	public Vector unit() {
		float m = magnitude();
		return (m == 0 ? new Vector(0, 0): divide(m));
	}
	
	public float dot(Vector v) {
		return x*v.x + y*v.y;
	}
	public float cross(Vector v) {
		return x*v.y - y*v.x;
	}
	public float magnitude() {
		return (float) Math.hypot(x, y);
	}
	public float direction() {
		return (float) Math.atan2(y, x);
	}
	
}
