package com.henagongames.physics.collision;

import com.henagongames.physics.Body;
import com.henagongames.physics.geom.Circle;
import com.henagongames.physics.geom.Line;
import com.henagongames.physics.geom.Polygon;
import com.henagongames.physics.geom.Shape;
import com.henagongames.physics.geom.Vector;

/**
 * Contains many algorithms for collision detection
 * @author Riley
 */
public class CollisionDetector {
	
	public static boolean intersecting(Line a, Line b) {
		Vector p = a.point1;
		Vector q = b.point1;
		Vector r = a.point2.subtract(p);
		Vector s = b.point2.subtract(q);
		if(r.cross(s) == 0) return false;
		float t = q.subtract(p).cross(s)/r.cross(s);
		float u = q.subtract(p).cross(r)/r.cross(s);
		return t >= 0 && t <= 1 && u >= 0 && u <= 1;
	}
	
	public static Vector pointOfIntersection(Line line1, Line line2) {
		Vector p = line1.point1;
		Vector q = line2.point1;
		Vector r = line1.point2.subtract(p);
		Vector s = line2.point2.subtract(q);
		float t = q.subtract(p).cross(s)/r.cross(s);
		return p.add(r.multiply(t));
	}
	
	public static boolean isColliding(Shape a, Shape b) {
		if(a instanceof Circle) {
			if(b instanceof Circle) {
				return isColliding((Circle) a, (Circle) b);
			}else if(b instanceof Polygon) {
				return isColliding((Polygon) b, (Circle) a);
			}
		}else if(a instanceof Polygon) {
			if(b instanceof Circle) {
				return isColliding((Polygon) a, (Circle) b);
			}else if(b instanceof Polygon) {
				return isColliding((Polygon) a, (Polygon) b);
			}
		}
		return false;
	}
	public static boolean isColliding(Circle a, Circle b) {
		return Math.pow(a.radius+b.radius, 2) >= Math.pow(a.position.x-b.position.x, 2)+Math.pow(a.position.y-b.position.y, 2);
	}
	public static boolean isColliding(Polygon a, Polygon b) {
		//check if the polygons are colliding using the separating axis theorem
		Line[] sa = a.sides();
		Line[] sb = b.sides();
		for(int i = 0; i < sa.length; i++) {
			Line line = sa[i];
			Vector p0 = line.point1.add(a.position);
			Vector p1 = line.point2.add(a.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are separated
			if(mina > maxb || minb > maxa) {
				return false;
			}
		}
		for(int i = 0; i < sb.length; i++) {
			Line line = sb[i];
			Vector p0 = line.point1.add(b.position);
			Vector p1 = line.point2.add(b.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are separated
			if(mina > maxb || minb > maxa) {
				return false;
			}
		}
		return true;
	}
	public static boolean isColliding(Polygon a, Circle b) {
		//check collision using the separating axis theorem
		Line[] s = a.sides();
		for(int i = 0; i < s.length; i++) {
			Line line = s[i];
			Vector p0 = line.point1.add(a.position);
			Vector p1 = line.point2.add(a.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for circle b
			Vector p = b.position;
			Vector d = p0.subtract(p);
			float dxp = d.dot(u);
			minb = dxp-b.radius;
			maxb = dxp+b.radius;
			//check if the projections are not overlapping
			if(mina > maxb || minb > maxa) {
				return false;
			}
		}
		//test the vector between the circle's center and the nearest point on the polygon
		Vector p = b.position;
		int j = 0;
		float m = Float.MAX_VALUE;
		for(int i = 0; i < a.vertices.length; i++) {
			Vector pi = a.vertices[i].add(a.position);
			Line l = new Line(p, pi);
			float mi = l.length();
			if(mi < m) {
				m = mi;
				j = i;
			}
		}
		Vector pj = a.vertices[j].add(a.position);
		Vector k = pj.subtract(p);
		Vector u = k.unit();
		//minimum and maximum values on the x' axis
		float mina = Float.MAX_VALUE;
		float maxa = -Float.MAX_VALUE;
		float minb = -b.radius;
		float maxb = b.radius;
		//calculate min and max for poly a
		for(int i = 0; i < a.vertices.length; i++) {
			Vector pi = a.vertices[i].add(a.position);
			Vector d = p.subtract(pi);
			float dxp = d.dot(u);
			mina = Math.min(mina, dxp);
			maxa = Math.max(maxa, dxp);
		}
		//check if the projections are not overlapping
		if(mina > maxb || minb > maxa) {
			return false;
		}
		//they are colliding since no separating axis was found
		return true;
	}
	
	public static boolean isColliding(Polygon a, Line b) {
		Line[] sides = a.sides().clone();
		for(int i = 0; i < sides.length; i++) {
			sides[i].point1 = sides[i].point1.add(a.position);
			sides[i].point2 = sides[i].point2.add(a.position);
		}
		for(int i = 0; i < sides.length; i++) {
			if(intersecting(sides[i], b)) {
				return true;
			}
		}
		return isColliding(a, b.point1);
	}
	public static boolean isColliding(Polygon a, Vector b) {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		for(int i = 0; i < a.vertices.length; i++) {
			minX = Math.min(a.vertices[i].x, minX);
			minY = Math.min(a.vertices[i].y, minY);
		}
		Line line = new Line(new Vector(minX-1, minY-1).add(a.position), b);
		Line[] sides = a.sides().clone();
		int intersections = 0;
		for(int i = 0; i < sides.length; i++) {
			Vector point1 = sides[i].point1.add(a.position);
			Vector point2 = sides[i].point2.add(a.position);
			if(intersecting(new Line(point1, point2), line)) {
				intersections++;
			}
		}
		return intersections == 1;
	}
	public static boolean isColliding(Circle a, Line b) {
		//(a dot b) / |a| is scalar projection of a onto b
		//((a dot b) / |a|^2)*a is vector projection of a onto b
		Vector va = a.position.subtract(b.point1);
		Vector vb = b.point2.subtract(b.point1);
		Vector projection = va.multiply(va.dot(vb) / (va.x * va.x + va.y * va.y));
		Vector d = b.point1.add(projection);
		float length = new Line(a.position, d).length();
		return length <= a.radius;
	}
	public static boolean isColliding(Circle a, Vector b) {
		return Math.pow(a.radius, 2) >= Math.pow(a.position.x-b.x, 2)+Math.pow(a.position.y-b.y, 2);
	}
	
	public static Vector normal(Body a, Body b) {
		if(a.shape instanceof Circle) {
			if(b.shape instanceof Circle) {
				Circle c1 = (Circle) a.shape.copy();
				Circle c2 = (Circle) b.shape.copy();
				c1.position = c1.position.add(a.position);
				c2.position = c2.position.add(b.position);
				return normal(c1, c2);
			}else if(b.shape instanceof Polygon) {
				Circle c = (Circle) a.shape.copy();
				Polygon p = (Polygon) b.shape.copy();
				c.position = c.position.add(a.position);
				p.position = p.position.add(b.position);
				return normal(p, c).multiply(-1);
			}
		}else if(a.shape instanceof Polygon) {
			if(b.shape instanceof Circle) {
				Polygon p = (Polygon) a.shape.copy();
				Circle c = (Circle) b.shape.copy();
				p.position = p.position.add(a.position);
				c.position = c.position.add(b.position);
				return normal(p, c);
			}else if(b.shape instanceof Polygon) {
				Polygon p1 = (Polygon) a.shape.copy();
				Polygon p2 = (Polygon) b.shape.copy();
				p1.position = p1.position.add(a.position);
				p2.position = p2.position.add(b.position);
				return normal(p1, p2);
			}
		}
		return new Vector(0, 0);
	}
	public static Vector normal(Circle a, Circle b) {
		float dist = (float) Math.hypot(a.position.x-b.position.x, a.position.y-b.position.y);
		float radiusSum = a.radius+b.radius;
		float magnitude = radiusSum-dist;
		float dir = (float) Math.atan2(a.position.y-b.position.y, a.position.x-b.position.x);
		Vector unit = new Vector((float) Math.cos(dir), (float) Math.sin(dir));
		Vector norm = unit.multiply(magnitude);
		return norm;
	}
	public static Vector normal(Polygon a, Polygon b) {
		//check if the polygons are colliding using the separating axis theorem
		Line[] sa = a.sides();
		Line[] sb = b.sides();
		Vector norm = new Vector(Float.MAX_VALUE, Float.MAX_VALUE);
		for(int i = 0; i < sa.length; i++) {
			Line line = sa[i];
			Vector p0 = line.point1.add(a.position);
			Vector p1 = line.point2.add(a.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are not separated on this axis
			if(mina <= maxb && minb <= maxa) {
				float ca = (mina+maxa)/2;
				float cb = (minb+maxb)/2;
				Vector v = new Vector(0, 0);
				if(ca > cb) {
					v = u.multiply(mina-maxb);
				}else{
					v = u.multiply(maxa-minb);
				}
				if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
					norm = v;
				}
			}
		}
		for(int i = 0; i < sb.length; i++) {
			Line line = sb[i];
			Vector p0 = line.point1.add(b.position);
			Vector p1 = line.point2.add(b.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are not separated on this axis
			if(mina <= maxb && minb <= maxa) {
				float ca = (mina+maxa)/2;
				float cb = (minb+maxb)/2;
				Vector v = new Vector(0, 0);
				if(ca > cb) {
					v = u.multiply(mina-maxb);
				}else{
					v = u.multiply(maxa-minb);
				}
				if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
					norm = v;
				}
			}
		}
		return norm;
	}
	public static Vector normal(Polygon a, Circle b) {
		//check collision using the separating axis theorem
		Vector norm = new Vector(Float.MAX_VALUE, Float.MAX_VALUE);
		Line[] s = a.sides();
		for(int i = 0; i < s.length; i++) {
			Line line = s[i];
			Vector p0 = line.point1.add(a.position);
			Vector p1 = line.point2.add(a.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for circle b
			Vector p = b.position;
			Vector d = p0.subtract(p);
			float dxp = d.dot(u);
			minb = dxp-b.radius;
			maxb = dxp+b.radius;
			//check if the projections are overlapping
			if(mina <= maxb && minb <= maxa) {
				float ca = (mina+maxa)/2;
				float cb = (minb+maxb)/2;
				Vector v = new Vector(0, 0);
				if(ca > cb) {
					v = u.multiply(mina-maxb);
				}else{
					v = u.multiply(maxa-minb);
				}
				if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
					norm = v;
				}
			}
		}
		//test the vector between the circle's center and the nearest point on the polygon
		Vector p = b.position;
		int j = 0;
		float m = Float.MAX_VALUE;
		for(int i = 0; i < a.vertices.length; i++) {
			Vector pi = a.vertices[i].add(a.position);
			Line l = new Line(p, pi);
			float mi = l.length();
			if(mi < m) {
				m = mi;
				j = i;
			}
		}
		Vector pj = a.vertices[j].add(a.position);
		Vector k = pj.subtract(p);
		Vector u = k.unit();
		//minimum and maximum values on the x' axis
		float mina = Float.MAX_VALUE;
		float maxa = -Float.MAX_VALUE;
		float minb = -b.radius;
		float maxb = b.radius;
		//calculate min and max for poly a
		for(int i = 0; i < a.vertices.length; i++) {
			Vector pi = a.vertices[i].add(a.position);
			Vector d = p.subtract(pi);
			float dxp = d.dot(u);
			mina = Math.min(mina, dxp);
			maxa = Math.max(maxa, dxp);
		}
		//check if the projections are overlapping
		if(mina <= maxb && minb <= maxa) {
			float ca = (mina+maxa)/2;
			float cb = (minb+maxb)/2;
			Vector v = new Vector(0, 0);
			if(ca > cb) {
				v = u.multiply(mina-maxb);
			}else{
				v = u.multiply(maxa-minb);
			}
			if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
				norm = v;
			}
		}
		return norm;
	}
	
	public static Vector contactPoint(Body a, Body b, Vector n) {
		if(a.shape instanceof Circle) {
			Circle s = (Circle) a.shape.copy();
			s.position = s.position.add(a.position);
			return contactPoint(s, n.multiply(-1));
		}else if(a.shape instanceof Polygon) {
			if(b.shape instanceof Polygon) {
				Polygon s1 = (Polygon) a.shape.copy();
				Polygon s2 = (Polygon) b.shape.copy();
				s1.position = s1.position.add(a.position);
				s2.position = s2.position.add(b.position);
				return contactPoint(s1, s2);
			}else {
				Circle s = (Circle) b.shape.copy();
				s.position = s.position.add(b.position);
				return contactPoint(s, n);
			}
		}
		return new Vector(0, 0);
	}
	public static Vector contactPoint(Circle a, Vector n) {
		Vector u = n.unit();
		Vector p = a.position;
		float r = a.radius;
		return p.add(u.multiply(r));
	}
	public static Vector contactPoint(Polygon a, Polygon b) {
		//check if the polygons are colliding using the separating axis theorem
		Line[] sa = a.sides();
		Line[] sb = b.sides();
		Vector norm = new Vector(Float.MAX_VALUE, Float.MAX_VALUE);
		Vector contactPoint = new Vector(0, 0);
		for(int i = 0; i < sa.length; i++) {
			Line line = sa[i];
			Vector p0 = line.point1.add(a.position);
			Vector p1 = line.point2.add(a.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are not separated on this axis
			if(mina <= maxb && minb <= maxa) {
				float ca = (mina+maxa)/2;
				float cb = (minb+maxb)/2;
				Vector v = new Vector(0, 0);
				if(ca > cb) {
					v = u.multiply(mina-maxb);
				}else{
					v = u.multiply(maxa-minb);
				}
				if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
					norm = v;
					Vector vu = v.unit();
					Vector pos = b.position;
					int index = 0;
					float dxp0 = Float.MAX_VALUE;
					for(int j = 0; j < b.vertices.length; j++) {
						Vector p = b.vertices[j].add(b.position);
						Vector d = pos.subtract(p);
						float dxp = d.dot(vu);
						if(dxp < dxp0) {
							index = j;
							dxp0 = dxp;
						}
					}
					contactPoint = b.vertices[index].add(b.position);
				}
			}
		}
		for(int i = 0; i < sb.length; i++) {
			Line line = sb[i];
			Vector p0 = line.point1.add(b.position);
			Vector p1 = line.point2.add(b.position);
			Vector l = p1.subtract(p0);
			Vector u = new Vector(-l.y, l.x).unit();
			//minimum and maximum values on the x' axis
			float mina = Float.MAX_VALUE;
			float maxa = -Float.MAX_VALUE;
			float minb = Float.MAX_VALUE;
			float maxb = -Float.MAX_VALUE;
			//calculate min and max for poly a
			for(int j = 0; j < a.vertices.length; j++) {
				Vector pj = a.vertices[j].add(a.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				mina = Math.min(mina, dxp);
				maxa = Math.max(maxa, dxp);
			}
			//calculate min and max for poly b
			for(int j = 0; j < b.vertices.length; j++) {
				Vector pj = b.vertices[j].add(b.position);
				Vector d = p0.subtract(pj);
				float dxp = d.dot(u);
				minb = Math.min(minb, dxp);
				maxb = Math.max(maxb, dxp);
			}
			//check if they are not separated on this axis
			if(mina <= maxb && minb <= maxa) {
				float ca = (mina+maxa)/2;
				float cb = (minb+maxb)/2;
				Vector v = new Vector(0, 0);
				if(ca > cb) {
					v = u.multiply(mina-maxb);
				}else{
					v = u.multiply(maxa-minb);
				}
				if(Math.abs(norm.magnitude()) > Math.abs(v.magnitude())) {
					norm = v;
					Vector vu = v.unit().multiply(-1);
					Vector pos = a.position;
					int index = 0;
					float dxp0 = Float.MAX_VALUE;
					for(int j = 0; j < a.vertices.length; j++) {
						Vector p = a.vertices[j].add(a.position);
						Vector d = pos.subtract(p);
						float dxp = d.dot(vu);
						if(dxp < dxp0) {
							index = j;
							dxp0 = dxp;
						}
					}
					contactPoint = a.vertices[index].add(a.position);
				}
			}
		}
		return contactPoint;
	}
	
}
