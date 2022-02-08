package com.henagongames.physics.collision;

import com.henagongames.physics.Body;

/**
 * You can create a collision handler by implementing this class and adding it to a world.
 * With a collision handler you can make something special happen when two specific
 * bodies collide. The world will call the handleCollision method whenever two bodies in that world collide.
 * You can differentiate bodies by assigning their userData variables values that you can link to what
 * type of body it is.
 * @author Riley
 */
public interface CollisionHandler {
	
	public void handleCollision(Body a, Body b);
	
}
