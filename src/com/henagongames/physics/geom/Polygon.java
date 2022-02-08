package com.henagongames.physics.geom;

public class Polygon extends Shape {
	
	public Vector[] vertices;
	
	public Polygon(Vector position, Vector[] vertices) {
		super(position);
		this.vertices = vertices;
	}
	
	public Line[] sides() {
		Line[] sides = new Line[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			Vector point1 = vertices[i];
			Vector point2 = vertices[(i+1)%vertices.length];
			sides[i] = new Line(point1, point2);
		}
		return sides;
	}
	
	public float area() {
		//calculate area by dividing polygon into many triangles and adding their areas
		//the triangles' areas are calculated using heron's formula
		float area = 0;
		for(int i = 1; i < vertices.length-1; i++) {
			float a = (float) Math.hypot(vertices[0].x-vertices[i].x, vertices[0].y-vertices[i].y);
			float b = (float) Math.hypot(vertices[0].x-vertices[i+1].x, vertices[0].y-vertices[i+1].y);
			float c = (float) Math.hypot(vertices[i].x-vertices[i+1].x, vertices[i].y-vertices[i+1].y);
			float s = (a+b+c)/2;
			area += Math.sqrt(s*(s-a)*(s-b)*(s-c));
		}
		return area;
	}
	public float momentOfInertia() {
		Vector C = centerOfMass().subtract(position);	//center of mass
		Line[] sides = sides();							//sides of the polygon
		float moi = 0;									//moment of inertia
		for(int i = 0; i < sides.length; i++) {
			Line l = sides[i];							//current side of polygon being looped through
			Vector p1 = C;								//points 1, 2, and 3 are the points of the triangle
			Vector p2 = l.point1;
			Vector p3 = l.point2;
			Vector Cp = p1.add(p2).add(p3).divide(3);	//center of mass of the triangle, or C'
			float d = new Line(C, Cp).length();			//distance between center of mass
			Vector bv = p2.subtract(p1);				//vector for side b of triangle
			float b = bv.magnitude();					//scalar for length of side b
			Vector u = bv.divide(b);					//unit vector for side b
			Vector cv = p3.subtract(p1);				//vector for side c of triangle, only used to calculate variables a and h
			float a = cv.dot(u);						//length of a in triangle
			Vector av = u.multiply(a);					//vector for a in triangle
			Vector hv = cv.subtract(av);				//vector for height of triangle, or h in diagram
			float h = hv.magnitude();					//length of height of triangle, or h in diagram
			float I = ((b*b*b*h)-(b*b*h*a)+(b*h*a*a)+(b*h*h*h))/36;		//calculate moment of inertia of individual triangle
			float M = (b*h)/2;							//mass or area of triangle
			moi += I+M*d*d;								//equation in sigma series of website
		}
		return moi;
	}
	public Vector centerOfMass() {
		Vector center = new Vector(0, 0);
		for(int i = 0; i < vertices.length; i++) {
			center = center.add(vertices[i]);
		}
		return center.divide(vertices.length).add(position);
	}
	public Shape copy() {
		return new Polygon(position, vertices);
	}
	public Shape rotate(float theta) {
		Vector c = centerOfMass().subtract(position);
		Polygon s = new Polygon(position, vertices);
		for(int i = 0; i < s.vertices.length; i++) {
			Vector p = s.vertices[i];
			Vector d = p.subtract(c);
			float dm = d.magnitude();
			float dd = d.direction();
			Vector d2 = new Vector(dm*(float) Math.cos(theta+dd), dm*(float) Math.sin(theta+dd));
			s.vertices[i] = c.add(d2);
		}
		return s;
	}
	
}
