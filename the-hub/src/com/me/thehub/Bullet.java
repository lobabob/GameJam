package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	// game object
	private Sprite bullet;
	public Rectangle bounds;
	
	// movement and position data
	private Vector2 vel;
	public float x;
	public float y;
	
	// constants
	private static final int SPEED = 1100;
	private static final int SIZE = 8;
	
	public Bullet(float x, float y, Vector2 dir)
	{
		vel = dir.cpy().scl(SPEED);
		this.x = x;
		this.y = y;
		
		// create and place bullet
		Texture bulletTex = new Texture("entities/bullet.png");
		bullet = new Sprite(bulletTex);
		bullet.setPosition(x, y);
		bullet.setSize(SIZE, SIZE);
		
		bounds = new Rectangle(x, y, SIZE, SIZE);
	}
	
	private void update(float delta)
	{
		x += vel.x * delta;
		bullet.setX(x);
		bounds.setX(x);
	}
	
	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		bullet.draw(batch);
	}

}
