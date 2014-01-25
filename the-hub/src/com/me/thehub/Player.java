package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player {

	public Sprite player;

	// collision data
	private TiledMapTileLayer collision;
	private float tileWidth, tileHeight;
	public Rectangle bounds;

	// constants for player attributes
	private static final int SPEED = 30000;
	private static final int JUMP = 35000;
	private static final float GRAVITY = 1500;
	private static final int W = 32;
	private static final int H = 60;

	// movement and position data
	private Vector2 vel;
	private boolean canJump = true;
	public float x;
	public float y;

	public Player(int x, int y, TiledMapTileLayer collision)
	{
		this.collision = collision;
		this.x = x;
		this.y = y;

		// create player sprite
		Texture playerTex = new Texture(Gdx.files.internal("test/player.png"));
		TextureRegion playerIMG = new TextureRegion(playerTex, 279, 283);
		player = new Sprite(playerIMG);
		player.setSize(W, H);

		// place player in level
		player.setPosition(x, y);
		vel = new Vector2(0, 0);

		// set collision data
		tileWidth = collision.getTileWidth();
		tileHeight = collision.getTileHeight();
		bounds = new Rectangle(x, y, W, H);
	}

	/****************************************************/
	/******* COLLISION CHECKING, ABANDON ALL HOPE *******/
	/****************************************************/
	
	// this looks terrible, but that's because collisions are terrible.
	// just trust that it works and use it appropriately
	private void checkWorldCollisions(float delta)
	{
		float oldX = player.getX();
		float oldY = player.getY();
		boolean collisionX = false, collisionY = false;
		
		// move x and check collisions
		player.setX(oldX + vel.x * delta);
		if(vel.x < 0)
		{
			// check top left
			collisionX = collision.getCell((int)(player.getX() / tileWidth), 
					(int)((player.getY() + player.getHeight()) / tileHeight)).getTile()
					.getProperties().containsKey("blocked");

			// check middle left
			if(!collisionX)
				collisionX = collision.getCell((int)(player.getX() / tileWidth), 
						(int)((player.getY() + player.getHeight()/2) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");

			// check bottom left
			if(!collisionX)
				collisionX = collision.getCell((int)(player.getX() / tileWidth), 
						(int)(player.getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
		}
		else if(vel.x > 0)
		{
			// top right
			collisionX = collision.getCell((int)( (player.getX() + player.getWidth()) / tileWidth), 
					(int)((player.getY() + player.getHeight()) / tileHeight)).getTile()
					.getProperties().containsKey("blocked");

			// middle right
			if(!collisionX)
				collisionX = collision.getCell((int)( (player.getX() + player.getWidth()) / tileWidth), 
						(int)((player.getY() + player.getHeight()/2) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");

			// bottom right
			if(!collisionX)
				collisionX = collision.getCell((int)( (player.getX() + player.getWidth()) / tileWidth), 
						(int)((player.getY()) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");
		}
		// move y and check tile collisions
		player.setY(oldY + vel.y * delta);
		if(vel.y < 0)
		{
			// bottom left
			if(!collisionX)
				collisionY = collision.getCell((int)( (player.getX()) / tileWidth), 
						(int)((player.getY()) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// bottom middle
			if(!collisionY)
				collisionY = collision.getCell((int)( (player.getX() + player.getWidth()/2) / tileWidth), 
						(int)(player.getY() / tileHeight)).getTile().getProperties().containsKey("blocked");


			// bottom right
			if(!collisionY && !collisionX)
				collisionY = collision.getCell((int)( (player.getX() + player.getWidth()) / tileWidth), 
						(int)(player.getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
			if(collisionY)
				canJump = true;
		}
		else if(vel.y > 0)
		{
			if(!collisionX)
				// top left
				collisionY = collision.getCell((int)( player.getX() / tileWidth), 
						(int)((player.getY() + player.getHeight()) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");
			// top middle
			if(!collisionY)
				collisionY = collision.getCell((int)( (player.getX() + player.getWidth()/2) / tileWidth), 
						(int)((player.getY() + player.getHeight()) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");
			// top right
			if(!collisionY && !collisionX)
				collisionY = collision.getCell((int)( (player.getX() + player.getWidth()) / tileWidth), 
						(int)((player.getY() + player.getHeight()) / tileHeight)).getTile()
						.getProperties().containsKey("blocked");
		}

		if(collisionX) {
			player.setX(oldX);
			vel.x = 0;
		}
		if(collisionY) {
			player.setY(oldY);
			vel.y = 0;
		}
		// if we're not hitting anything, gravity pulls us down
		else vel.y -= GRAVITY * delta;
	}
	
	/****************************************************/
	/****************** PRAISE THE SUN ******************/
	/****************************************************/

	private void update(float delta)
	{
		// set horizontal movement vector component
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			vel.x = -SPEED * delta;
		else if(Gdx.input.isKeyPressed(Keys.RIGHT))
			vel.x = SPEED * delta;
		else
			vel.x = 0;

		// try to jump
		if(Gdx.input.isKeyPressed(Keys.SPACE) && canJump) {
			vel.y = JUMP * delta;
			canJump = false;
		}
		// check collisions and set player position
		checkWorldCollisions(delta);
		x = player.getX();
		y = player.getY();
	}

	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		player.draw(batch);
	}
}
