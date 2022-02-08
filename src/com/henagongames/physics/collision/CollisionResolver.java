package com.henagongames.physics.collision;

import com.henagongames.physics.Body;
import com.henagongames.physics.geom.Vector;

/**
 * Resolves the collision between two bodies
 * @author Riley
 */
public class CollisionResolver {
	
	public static void resolveCollision(Body a, Body b, float dt) {
		//calculate normal vector
		Vector norm = CollisionDetector.normal(a, b);
		Vector normb = norm.multiply(-1);
		//undo overlap between bodies
		float ratio1 = a.mass/(a.mass+b.mass);
		float ratio2 = b.mass/(b.mass+a.mass);
		a.position = a.position.add(norm.multiply(ratio1));
		b.position = b.position.add(normb.multiply(ratio2));
		//calculate contact point of collision and other values needed for rotation
		Vector cp = CollisionDetector.contactPoint(a, b, norm);
		Vector c = a.shape.centerOfMass().add(a.position);
		Vector cb = b.shape.centerOfMass().add(b.position);
		Vector d = cp.subtract(c);
		Vector db = cp.subtract(cb);
		//create the normal force vector from the velocity
		Vector u = norm.unit();
		Vector ub = u.multiply(-1);
		Vector F = new Vector(0, 0);
		boolean doA = a.mass != 0;
		if(doA) {
			F = a.force;
		}else {
			F = b.force;
		}
		Vector n = new Vector(0, 0);
		Vector nb = new Vector(0, 0);
		if(doA) {
			Vector Fyp = u.multiply(F.dot(u));
			n = Fyp.multiply(-1);
			nb = Fyp;
		}else{
			Vector Fypb = ub.multiply(F.dot(ub));
			n = Fypb;
			nb = Fypb.multiply(-1);
		}
		//calculate normal force for body A
		float r = a.restitution;
		Vector v1 = a.velocity;
		Vector vy1p = u.multiply(u.dot(v1));
		Vector vx1p = v1.subtract(vy1p);
		Vector vy2p = vy1p.multiply(-r);
		Vector v2 = vy2p.add(vx1p);
		//calculate normal force for body B
		float rb = b.restitution;
		Vector v1b = b.velocity;
		Vector vy1pb = ub.multiply(ub.dot(v1b));
		Vector vx1pb = v1b.subtract(vy1pb);
		Vector vy2pb = vy1pb.multiply(-rb);
		Vector v2b = vy2pb.add(vx1pb);
		//calculate friction for body A
		float mk = (a.friction+b.friction)/2;
		Vector v = a.velocity;
		Vector vyp = u.multiply(v.dot(u));
		Vector vxp = v.subtract(vyp);
		float fk = -n.multiply(mk).magnitude();
		Vector fkv = vxp.unit().multiply(fk);								//friction force
		Vector vr = vxp.subtract(d.multiply(a.angularVelocity));
		Vector fkvr = vr.unit().multiply(fk);								//friction torque - indicated by r for rotation
		//calculate friction for body B
		Vector vb = b.velocity;
		Vector vypb = ub.multiply(vb.dot(ub));
		Vector vxpb = vb.subtract(vypb);
		float fkb = -nb.multiply(mk).magnitude();
		Vector fkvb = vxpb.unit().multiply(fkb);							//friction force
		Vector vrb = vxpb.subtract(db.multiply(b.angularVelocity));
		Vector fkvrb = vrb.unit().multiply(fkb);							//friction torque - indicated by r for rotation
		//move bodies based on calculations
		a.momentum = v2.multiply(a.mass).add(fkv.multiply(dt));
		if(a.mass != 0) {
			a.velocity = a.momentum.divide(a.mass);
			a.position = a.position.add(a.velocity.multiply(dt));
		}
		b.momentum = v2b.multiply(b.mass).add(fkvb.multiply(dt));
		if(b.mass != 0) {
			b.velocity = b.momentum.divide(b.mass);
			b.position = b.position.add(b.velocity.multiply(dt));
		}
		//apply torque to bodies
//		float t = -(d.cross(fkvr)+d.cross(n));
//		float tb = -(db.cross(fkvrb)+db.cross(nb));
//		if(a.mass != 0) {
//			a.angularMomentum = t*dt;//+d.cross(v2.multiply(a.mass));
//			a.angularVelocity = a.angularMomentum/a.momentOfInertia;
//			a.angle += a.angularVelocity*dt;
//			a.shape.rotate(a.angularVelocity*dt);
//		}
//		if(b.mass != 0) {
//			b.angularMomentum = tb*dt;//+db.cross(v2b.multiply(b.mass));
//			b.angularVelocity = b.angularMomentum/b.momentOfInertia;
//			b.angle += b.angularVelocity*dt;
//			b.shape.rotate(b.angularVelocity*dt);
//		}
	}
	
}
