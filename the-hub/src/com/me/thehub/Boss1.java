package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Boss1 {
	
	// Game object
	private Sprite boss;
	private Rectangle platform;
	
	// animation
	private Animation crouch;
	private Animation flash;
	private TextureRegion upright;
	private TextureRegion crouched;
	private TextureRegion currentFrame;
	
	// state tracking
	private float stateTime;
	private boolean fullyCrouched;
	private boolean fullyUpright;
	
	// position data
	public float x, y;
	
	public Boss1(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/boss1.png"));
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/4, spriteSheet.getHeight()/2);
		
		// crouching animation
		TextureRegion[] crouchFrames = new TextureRegion[8];
		for(int i = 0; i < 4; i++) {
			crouchFrames[i] = tmp[0][i];
			crouchFrames[7-i] = tmp[0][i];
		}
		crouch = new Animation(0.50f, crouchFrames);
		
		// sticky frames
		upright = crouchFrames[0];
		crouched = crouchFrames[3];
		
		platform = new Rectangle();
	}
	
	private void update(float delta)
	{
		stateTime += delta;
		currentFrame = crouch.getKeyFrame(stateTime, true);
	}
	
	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		batch.draw(currentFrame, x, y);
	}

}
