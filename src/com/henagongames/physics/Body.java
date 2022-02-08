package com.henagongames.physics;

import com.henagongames.physics.geom.Shape;
import com.henagongames.physics.geom.Vector;

/**
 * The basic physics element, representing an object in the physical world
 * @author Riley
 */
public class Body {
	
	public Shape shape;
	public Vector position = new Vector(0, 0);
	public Vector velocity = new Vector(0, 0);
	public Vector momentum = new Vector(0, 0);
	public Vector force = new Vector(0, 0);
	//public Vector impulse = new Vector(0, 0);
	public float angle = 0;
	public float angularVelocity = 0;
	public float angularMomentum = 0;
	public float torque = 0;
	//public float angularImpulse = 0;
	public float density = 1;
	public float restitution = 0;
	public float friction = 0;
	public float mass;
	public float momentOfInertia;
	public Object userData;
	
	public Body(Shape shape, Vector position) {
		this.shape = shape;
		this.position = position;
		this.momentOfInertia = shape.momentOfInertia();
	}
	public Body(Shape shape, Vector position, float density, float restitution, float friction) {
		this.shape = shape;
		this.position = position;
		setProperties(density, restitution, friction);
	}
	
	public void setProperties(float density, float restitution, float friction) {
		this.density = density;
		this.restitution = restitution;
		this.friction = friction;
		this.mass = density*shape.area();
		this.momentOfInertia = shape.momentOfInertia();
	}
	
	public void update(float dt) {
		if(mass != 0) {
			momentum = momentum.add(force.multiply(dt));
			velocity = momentum.divide(mass);
			position = position.add(velocity.multiply(dt));
		}
	}
	
}
