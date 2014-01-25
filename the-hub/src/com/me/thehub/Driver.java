package com.me.thehub;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

public class Driver implements ApplicationListener {

	private Screen test;
	
	@Override
	public void create() 
	{		
		test = new TestWorld();
	}

	@Override
	public void render() 
	{
		// clear render buffer
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// choose screen to show
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
