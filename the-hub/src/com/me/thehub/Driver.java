package com.me.thehub;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Driver implements ApplicationListener {

	public enum Screens { MENU, LEVEL1, LEVEL2 };

	private Screen test;
	private Screen test2;
	private SpriteBatch batch;
	private Player player;

	private Screens currentScreen;

	@Override
	public void create() 
	{	
		batch = new SpriteBatch();
		player = new Player();
		test = new TestWorld(player, batch);
		test2 = new TestWorld2(player, batch);

		currentScreen = Screens.LEVEL1;
	}

	@Override
	public void render() 
	{		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		switch(currentScreen)
		{
		case LEVEL1:
			test.show();
			currentScreen = ((TestWorld) test).checkTriggers();
			break;
		case LEVEL2:
			test2.show();
			break;
		default:
			break;
		}
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
