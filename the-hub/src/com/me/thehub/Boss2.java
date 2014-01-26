package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Boss2 {
	
	
	private Animation flash;
	private float stateTime;
	
	// position, movement
	private static final int SPEED = 30;
	private int x, y;
	private int w, h;
	private Vector2 dir;
	
	// hitboxes
	public Rectangle hitbox;
	public Rectangle hurtbox;
	
	public Boss2(int x, int y)
	{
		dir = new Vector2(-1, 0);
		this.x = x;
		this.y = y;
		
		// set up damage animation
		TextureRegion[] frames = new TextureRegion[2];
		Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/boss2.png"));
		
		w = spriteSheet.getWidth()/2;
		h = spriteSheet.getHeight();
		
		frames[0] = new TextureRegion(spriteSheet, 0, 0, w, h);
		frames[1] = new TextureRegion(spriteSheet, w, w*2, 0, h);
		flash = new Animation(0.1f, frames);
		
		hurtbox = new Rectangle(x + 30, y, w/5, h);
		hitbox = new Rectangle(x + 10, y + h/10, w/10, h/10);
	}
	
	private void update(float delta) 
	{
		x += dir.x * SPEED * delta;
		hurtbox.x = x + 30;
		hitbox.x = x + 10;
	}
	
	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		batch.draw(flash.getKeyFrames()[0], x, y);
	}

}
