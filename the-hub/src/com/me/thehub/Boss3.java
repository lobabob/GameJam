package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Boss3 {
	
	// face and animation
	private Animation faceB;
	private Animation faceW;
	private TextureRegion currentFrame;
	public Rectangle hitbox;
	private float stateTime;
	
	// keep track of platforms
	public Rectangle[] platforms;
	private Vector2[] spokes;
	
	// edit wheel properties, SO MODULAR I COULD DIE
	private static final int NUM_PLATFORMS = 8;
	private static final int W = 100;
	private static final int H = 25;
	private static final int RADIUS = 300;
	private static final int SPEED = 30;
	
	private Vector2 radius;
	private Vector2 pos;
	
	public Boss3(int x, int y)
	{
		// create facial animation
		Texture spriteSheet = new Texture(Gdx.files.internal("spritesheets/boss3.png"));
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, 
				spriteSheet.getHeight()/4);
		
		TextureRegion[] black = new TextureRegion[8];
		TextureRegion[] white = new TextureRegion[8];
		for(int i = 0; i < 4; i++) {
			black[i] = tmp[i][0];
			black[7-i] = tmp[i][0];
			white[i] = tmp[i][1];
			white[7-i] = tmp[i][1];
		}
		faceB = new Animation(0.5f, black);
		faceW = new Animation(0.5f, white);
		
		// create platforms
		platforms = new Rectangle[NUM_PLATFORMS];
		spokes = new Vector2[NUM_PLATFORMS];
		radius = new Vector2(0, RADIUS);
		
		for(int i = 0; i < NUM_PLATFORMS; i++) {
			platforms[i] = new Rectangle(radius.x + x, radius.y + y, W, H);
			spokes[i] = radius.cpy();
			radius.rotate(360/NUM_PLATFORMS);
		}
		pos = new Vector2(x, y + RADIUS);
	}

	private void update(float delta)
	{
		for(int i = 0; i < NUM_PLATFORMS; i++) {
			spokes[i].rotate(SPEED * delta);
			platforms[i].setPosition(spokes[i]);
			platforms[i].setPosition(platforms[i].x + pos.x, platforms[i].y + pos.y);
		}
		stateTime += delta;
		currentFrame = faceB.getKeyFrame(stateTime, true);
	}
	
	public void draw(ShapeRenderer sr, SpriteBatch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		
		for(int i = 0; i < NUM_PLATFORMS; i++) {
			sr.rect(platforms[i].x, platforms[i].y, platforms[i].width, platforms[i].height);
		}
		
		batch.begin();
		batch.draw(currentFrame, pos.x, pos.y);
		batch.end();
	}
	
}
