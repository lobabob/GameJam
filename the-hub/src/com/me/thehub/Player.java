package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Player {

	public Sprite player;
	private PlayerInputProcessor input;

	// sounds
	private Sound gun;
	private Sound bike;
	
	// animations
	private TextureRegion currentFrame;
	private Animation walk_noWeapon;
	private Animation jump_noWeapon;
	private Animation walk_gun;
	private Animation jump_gun;
	private Animation walk_bike;
	private Animation jump_bike;
	private TextureRegion idle_noWeapon;
	private TextureRegion idle_gun;
	private TextureRegion idle_bike;
	private float stateTime;

	// collision data
	private TiledMapTileLayer collision;
	private float tileWidth, tileHeight;
	public Rectangle bounds;

	// constants for player attributes
	private static final float SPEED = 370;
	private static final int JUMP = 615;
	private static final float GRAVITY = 1500;
	private static final int W = 32;
	private static final int H = 90;

	// movement and position data
	private Vector2 vel;
	private Vector2 dir;
	public float x;
	public float y;

	// switches for possible player actions
	private boolean canJump = true;
	private int numJumps = 2;
	public boolean canShoot = true;

	// switches for ability unlocks
	public boolean doubleJumpUnlocked = false;
	public boolean gunUnlocked = false;
	public boolean bikeUnlocked = false;

	public Player()
	{
		input = new PlayerInputProcessor();

		// create player sprite
		Texture playerTex = new Texture(Gdx.files.internal("entities/player.png"));
		TextureRegion playerIMG = new TextureRegion(playerTex, 279, 283);
		player = new Sprite(playerIMG);
		player.setSize(W, H);
		bounds = new Rectangle();

		// immobilize
		vel = new Vector2(0, 0);
		
		// load sounds
		gun = Gdx.audio.newSound(Gdx.files.internal("sounds/Sound Effects/Player/Gunshot.wav"));
		bike = Gdx.audio.newSound(Gdx.files.internal("sounds/Sound Effects/Player/Laser.wav"));

		/***********************************************/
		/************* CREATING ANIMATIONS *************/
		/***********************************************/

		TextureRegion[][] tmp;
		TextureRegion[] walkFrames;
		TextureRegion[] jumpFrames;

		/*************************/
		/******* no gun :( *******/
		/*************************/

		Texture spriteSheet_NoWeapon = new Texture(Gdx.files.internal("spritesheets/player_noweapon.png"));
		tmp = TextureRegion.split(spriteSheet_NoWeapon, spriteSheet_NoWeapon.getWidth()/5, spriteSheet_NoWeapon.getHeight()/3);

		// walking animation
		walkFrames = new TextureRegion[4];
		for(int i = 0; i < 4; i++) {
			walkFrames[i] = tmp[1][i];
		}
		walk_noWeapon = new Animation(0.25f, walkFrames);

		// jumping animation
		jumpFrames = new TextureRegion[2];
		jumpFrames[0] = tmp[2][0];
		jumpFrames[1] = tmp[2][1];
		jump_noWeapon = new Animation(0.20f, jumpFrames);

		// idle image
		idle_noWeapon = tmp[0][0];

		/***********************/
		/******* gun :) ********/
		/***********************/

		Texture spriteSheet_Gun = new Texture(Gdx.files.internal("spritesheets/player_gun.png"));
		tmp = TextureRegion.split(spriteSheet_Gun, spriteSheet_Gun.getWidth()/6, spriteSheet_Gun.getHeight()/4);

		// walking animation
		walkFrames = new TextureRegion[4];
		for(int i = 0; i < 4; i++) {
			walkFrames[i] = tmp[3][i];
		}
		walk_gun = new Animation(0.25f, walkFrames);

		// jumping animation
		jumpFrames = new TextureRegion[2];
		jumpFrames[0] = tmp[2][0];
		jumpFrames[1] = tmp[2][1];
		jump_gun = new Animation(0.20f, jumpFrames);

		// idle image
		idle_gun = tmp[0][5];

		/*****************************/
		/******* bigger gun :D *******/
		/*****************************/

		Texture spriteSheet_Bike = new Texture(Gdx.files.internal("spritesheets/player_bike.png"));
		tmp = TextureRegion.split(spriteSheet_Bike, spriteSheet_Bike.getWidth()/5, spriteSheet_Bike.getHeight()/3);

		// walking animation
		walkFrames = new TextureRegion[4];
		for(int i = 0; i < 4; i++) {
			walkFrames[i] = tmp[1][i];
		}
		walk_bike = new Animation(0.25f, walkFrames);

		// jumping animation
		jumpFrames = new TextureRegion[2];
		jumpFrames[0] = tmp[2][0];
		jumpFrames[1] = tmp[2][1];
		jump_bike = new Animation(0.20f, jumpFrames);

		// idle image
		idle_bike = tmp[0][0];

		/***********************************************/
		/************* ANIMATIONS COMPLETED*************/
		/***********************************************/

		stateTime = 0f;
	}

	public void setCollision(TiledMapTileLayer collision) {
		this.collision = collision;
		tileWidth = collision.getTileWidth();
		tileHeight = collision.getTileHeight();
	}

	// set player starting position and direction in a level
	public void setStart(float x, float y, Vector2 dir)
	{
		player.setPosition(x, y);
		this.x = x;
		this.y = y;
		this.dir = dir;
		bounds.set(x, y, W, H);
	}

	public Bullet shoot() 
	{
		// pistol position
		if(gunUnlocked && !bikeUnlocked) {
			gun.play(1.0f);
			return new Bullet(x + dir.x * 3 * W/4, y + 2*H/3 + 3, dir);
		}
		// bike position
		else {
			bike.play(1.0f);
			return new Bullet(x + dir.x * 5 * W/4, y + H/2, dir);
		}
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

			// if we land, restore jumps
			if(collisionY) {
				canJump = true;
				numJumps = 2;
			}
			// if we fall off platform, enable one jump if double jump is unlocked
			else if(numJumps > 0 && doubleJumpUnlocked)
				numJumps = 1;
			else 
				numJumps = 0;

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
		// if collided, restore old position
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
		if(input.keyDown(Keys.LEFT)) {
			// flip animations if switching direction
			if(dir.x > 0) {
				for(int i = 0; i < walk_noWeapon.getKeyFrames().length; i++) {
					walk_noWeapon.getKeyFrames()[i].flip(true, false);
					walk_gun.getKeyFrames()[i].flip(true, false);
					walk_bike.getKeyFrames()[i].flip(true, false);
				}
				for(int i = 0; i < jump_noWeapon.getKeyFrames().length; i++) {
					jump_noWeapon.getKeyFrames()[i].flip(true, false);
					jump_gun.getKeyFrames()[i].flip(true, false);
					jump_bike.getKeyFrames()[i].flip(true, false);
				}
				idle_noWeapon.flip(true, false);
				idle_gun.flip(true, false);
				idle_bike.flip(true, false);
			}
			vel.x = -SPEED;
			dir.x = -1;
		}
		else if(input.keyDown(Keys.RIGHT)) {
			// flip animations if switching direction
			if(dir.x < 0) {
				for(int i = 0; i < walk_noWeapon.getKeyFrames().length; i++) {
					walk_noWeapon.getKeyFrames()[i].flip(true, false);
					walk_gun.getKeyFrames()[i].flip(true, false);
					walk_bike.getKeyFrames()[i].flip(true, false);
				}
				for(int i = 0; i < jump_noWeapon.getKeyFrames().length; i++) {
					jump_noWeapon.getKeyFrames()[i].flip(true, false);
					jump_gun.getKeyFrames()[i].flip(true, false);
					jump_bike.getKeyFrames()[i].flip(true, false);
				}
				idle_noWeapon.flip(true, false);
				idle_gun.flip(true, false);
				idle_bike.flip(true, false);
			}
			vel.x = SPEED;
			dir.x = 1;
		}
		else {
			vel.x = 0;
		}

		// try to jump
		if(input.keyDown(Keys.SPACE) && numJumps > 0 && canJump) {
			vel.y = JUMP;
			numJumps--;
			canJump = false;
		}
		// allow double jumps if unlocked
		else if(input.keyUp(Keys.SPACE) && doubleJumpUnlocked)
			canJump = true;

		// choose animation
		stateTime += delta;

		// if no weapon
		if(!gunUnlocked && !bikeUnlocked) {
			if(vel.x != 0 && vel.y < 5*GRAVITY*delta && vel.y > -5*GRAVITY*delta)
				currentFrame = walk_noWeapon.getKeyFrame(stateTime, true);
			else if(vel.y > 5*GRAVITY*delta || vel.y < -5*GRAVITY*delta)
				currentFrame = jump_noWeapon.getKeyFrame(stateTime, true);
			else 
				currentFrame = idle_noWeapon;
		}
		// if gun unlocked
		else if(gunUnlocked && !bikeUnlocked) {
			if(vel.x != 0 && vel.y < 5*GRAVITY*delta && vel.y > -5*GRAVITY*delta)
				currentFrame = walk_gun.getKeyFrame(stateTime, true);
			else if(vel.y > 5*GRAVITY*delta || vel.y < -5*GRAVITY*delta)
				currentFrame = jump_gun.getKeyFrame(stateTime, true);
			else 
				currentFrame = idle_gun;
		}
		// if bike unlocked
		else {
			if(vel.x != 0 && vel.y < 5*GRAVITY*delta && vel.y > -5*GRAVITY*delta)
				currentFrame = walk_bike.getKeyFrame(stateTime, true);
			else if(vel.y > 5*GRAVITY*delta || vel.y < -5*GRAVITY*delta)
				currentFrame = jump_bike.getKeyFrame(stateTime, true);
			else 
				currentFrame = idle_bike;
		}

		// check collisions and set player position
		checkWorldCollisions(delta);
		x = player.getX();
		y = player.getY();
		bounds.setPosition(x, y);
	}

	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		//player.draw(batch);
		batch.draw(currentFrame, x - 53, y - 2);

	}

	public void preventFall() {
		if(vel.y < 0)
			vel.y = 0;
		canJump = true;
		if(doubleJumpUnlocked)
			numJumps = 2;
	}
}
