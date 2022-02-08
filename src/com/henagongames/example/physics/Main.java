package com.henagongames.example.physics;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.henagongames.physics.Body;
import com.henagongames.physics.World;
import com.henagongames.physics.collision.CollisionDetector;
import com.henagongames.physics.collision.CollisionHandler;
import com.henagongames.physics.geom.Circle;
import com.henagongames.physics.geom.Line;
import com.henagongames.physics.geom.Polygon;
import com.henagongames.physics.geom.Vector;

public class Main extends BasicGame implements CollisionHandler {
	
	World world;
	ArrayList<Body> bodies = new ArrayList<Body>();
	Image ball;
	ArrayList<Vector> cp = new ArrayList<Vector>();
	
	public Main(String title) {
		super(title);
	}
	
	public static void main(String[] args) {
		try{
			AppGameContainer app = new AppGameContainer(new Main("Physics 1.0.0"));
			app.setDisplayMode(800, 600, false);
			app.setClearEachFrame(true);
			app.setMinimumLogicUpdateInterval(15);
			app.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(GameContainer game) throws SlickException {
		ball = new Image("res/ball.png");
		//init world
		Vector gravity = new Vector(0, 0.8f);
		world = new World(gravity);
		world.collisionHandler = this;
		//init body1
		Vector v[] = {
			new Vector(0, 50),
			new Vector(800, 0),
			new Vector(800, 100),
			new Vector(0, 100)
		};
		Polygon s = new Polygon(new Vector(0, 0), v);
		Vector p = new Vector(0, 500);
		Body b = new Body(s, p, 0, 0, 0.1f);
		addBody(b);
	}
	public void render(GameContainer game, Graphics g) throws SlickException {
		for(int i = 0; i < bodies.size(); i++) {
			Body b = bodies.get(i);
			g.setColor(Color.white);
			if(b.shape instanceof Circle) {
				Circle s = (Circle) b.shape;
				float r = s.radius;
				Vector p = s.position.add(b.position);
				//g.drawOval(Math.round(p.x-r), Math.round(p.y-r), Math.round(r*2), Math.round(r*2));
				Image img = ball.copy();
				img.setCenterOfRotation(r, r);
				img.rotate(-b.angle);
				img.draw(p.x-r, p.y-r, r*2, r*2);
			}else {
				Polygon s = (Polygon) b.shape;
				Line[] sides = s.sides();
				for(int j = 0; j < sides.length; j++) {
					Line l = sides[j];
					Vector p1 = l.point1.add(s.position).add(b.position);
					Vector p2 = l.point2.add(s.position).add(b.position);
					g.drawLine(Math.round(p1.x), Math.round(p1.y), Math.round(p2.x), Math.round(p2.y));
				}
			}
			g.setColor(Color.green);
			Vector m = b.shape.centerOfMass().add(b.position);
			g.fillOval(m.x-4, m.y-4, 8, 8);
		}
		g.setColor(Color.red);
		for(int i = 0; i < cp.size(); i++) {
			Vector p = cp.get(i);
			g.fillOval(p.x-4, p.y-4, 8, 8);
		}
	}
	public void update(GameContainer game, int k) throws SlickException {
		while(cp.size() > 0) {
			cp.remove(0);
		}
		world.update(5);
		Input input = game.getInput();
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Circle s = new Circle(new Vector(0, 0), 25);
			Vector p = new Vector(input.getMouseX(), input.getMouseY());
			Body b = new Body(s, p, 0.5f, 0.3f, 0.1f);
			addBody(b);
		}
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			Vector v[] = {
				new Vector(0, 0),
				new Vector(0, 40),
				new Vector(40, 40),
				new Vector(40, 0)
			};
			Polygon s = new Polygon(new Vector(0, 0), v);
			Vector p = new Vector(input.getMouseX(), input.getMouseY());
			Body b = new Body(s, p, 0.5f, 0.3f, 0.1f);
			addBody(b);
		}
		if(input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
			Circle s = new Circle(new Vector(0, 0), 50);
			Vector p = new Vector(input.getMouseX(), input.getMouseY());
			Body b = new Body(s, p, 0f, 0f, 0.1f);
			addBody(b);
		}
		for(int i = 0; i < bodies.size(); i++) {
			if(bodies.get(i).position.y > 1000) {
				removeBody(bodies.get(i));
			}
		}
	}
	
	public void addBody(Body b) {
		bodies.add(b);
		world.addBody(b);
	}
	public void removeBody(Body b) {
		bodies.remove(b);
		world.removeBody(b);
	}
	
	public void handleCollision(Body a, Body b) {
		cp.add(CollisionDetector.contactPoint(a, b, CollisionDetector.normal(a, b)));
	}
	
}
