package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Boss1 {

	// Game object
	public Rectangle platform;
	public Rectangle hurtbox;

	// animation
	private Animation crouch;
	private TextureRegion currentFrame;

	// state tracking
	private float stateTime;
	private boolean shooting = false;

	// position data
	public float x, y;
	public int w, h;

	private Beam beam;

	public Boss1(float x, float y)
	{
		this.x = x;
		this.y = y;

		Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/boss1.png"));
		
		w = spriteSheet.getWidth()/4;
		h = spriteSheet.getHeight()/2;
		
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, w, h);

		// crouching animation
		TextureRegion[] crouchFrames = new TextureRegion[8];
		for(int i = 0; i < 4; i++) {
			crouchFrames[i] = tmp[0][i];
			crouchFrames[7-i] = tmp[0][i];
		}
		crouch = new Animation(0.50f, crouchFrames);
		currentFrame = crouchFrames[0];
		
		platform = new Rectangle(0, 0, 0, 0);
		hurtbox = new Rectangle(0, 0, 0, 0);
		
		beam = new Beam(x + w, y);
	}

	private void update(float delta)
	{
		stateTime += delta;
		currentFrame = crouch.getKeyFrame(stateTime, true);

		// move platform and hurtbox based on key frame
		for(int i = 2; i < 6; i++) {
			if(currentFrame.equals(crouch.getKeyFrames()[i])) {
				platform.set(x + 3*w/5, y + h/30, 4*w/14, h/5);
				hurtbox.set(x + w/6, y, w/3, 4*h/7);
				if(i == 3 || i == 4)
					shooting = true;
				else
					shooting = false;
			}
		}
		if(currentFrame.equals(crouch.getKeyFrames()[1]) || currentFrame.equals(crouch.getKeyFrames()[6]) ) {
			platform.set(0, 0, 0, 0);
			hurtbox.set(x + w/6, y, w/3, 5*h/7);
			shooting = false;
		}
		else if(currentFrame.equals(crouch.getKeyFrames()[0]) || currentFrame.equals(crouch.getKeyFrames()[7])) {
			platform.set(0, 0, 0, 0);
			hurtbox.set(x + w/6, y, w/3, 6*h/7);
			shooting = false;
		}
	}

	public void draw(SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		if(shooting) {
			beam.draw(batch);
		}
		batch.draw(currentFrame, x, y);
	}
}
