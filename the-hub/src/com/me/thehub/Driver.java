package com.me.thehub;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Driver implements ApplicationListener {
	
	private Screen test;
	private SpriteBatch batch;
	
	@Override
	public void create() 
	{	
		batch = new SpriteBatch();
		test = new TestWorld(batch);
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		test.show();
	}
	
	@Override
	public void dispose() {
		test.dispose();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
