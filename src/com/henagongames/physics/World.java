package com.henagongames.physics;

import java.util.ArrayList;

import com.henagongames.physics.collision.CollisionDetector;
import com.henagongames.physics.collision.CollisionHandler;
import com.henagongames.physics.collision.CollisionResolver;
import com.henagongames.physics.geom.Shape;
import com.henagongames.physics.geom.Vector;

/**
 * Contains physics bodies and updates them all in one update method
 * and also prevents any bodies from overlapping
 * @author Riley
 */
public class World {
	
	public ArrayList<Body> bodies = new ArrayList<Body>();
	public Vector gravity;
	public Object userData;
	public CollisionHandler collisionHandler = new CollisionHandler() {
		public void handleCollision(Body a, Body b) {
			
		}
	};
	
	public World() {
		gravity = new Vector(0, 0);
	}
	public World(Vector gravity) {
		this.gravity = gravity;
	}
	
	public void update(int accuracy) {
		if(accuracy <= 0) accuracy = 1;
		float dt = 1f/accuracy;
		for(int n = 0; n < accuracy; n++) {
			for(int i = 0; i < bodies.size(); i++) {
				Body a = bodies.get(i);
				for(int j = i+1; j < bodies.size(); j++) {
					Body b = bodies.get(j);
					Shape s1 = a.shape.copy();
					Shape s2 = b.shape.copy();
					s1.position = s1.position.add(a.position);
					s2.position = s2.position.add(b.position);
					if(CollisionDetector.isColliding(s1, s2) && (a.mass != 0 || b.mass != 0)) {
						collisionHandler.handleCollision(a, b);
						CollisionResolver.resolveCollision(a, b, dt);
					}
				}
			}
			for(int i = 0; i < bodies.size(); i++) {
				bodies.get(i).update(dt);
			}
		}
	}
	
	public void addBody(Body b) {
		b.force = b.force.add(gravity.multiply(b.mass));
		bodies.add(b);
	}
	public void addBodies(Body[] b) {
		for(int i = 0; i < b.length; i++){
			b[i].force = b[i].force.add(gravity.multiply(b[i].mass));
			addBody(b[i]);
		}
	}
	public void removeBody(Body b) {
		b.force = b.force.subtract(gravity.multiply(b.mass));
		bodies.remove(b);
	}
	
	public void setGravity(Vector v) {
		for(int i = 0; i < bodies.size(); i++) {
			Body b = bodies.get(i);
			b.force = b.force.subtract(gravity.multiply(b.mass));
			b.force = b.force.add(v.multiply(b.mass));
		}
		gravity = v;
	}
	
}
