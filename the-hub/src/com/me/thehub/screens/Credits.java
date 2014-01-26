package com.me.thehub.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.thehub.Driver.Screens;

public class Credits implements Screen {
	
	private boolean credsDone = false;
	
	private SpriteBatch batch;
	private Sprite credImg;
	private Texture texture;
	
	private Sound credsMusic;
	private long credsMusic_id;
	
	public Credits(SpriteBatch b) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = b;
		
		texture = new Texture("creds.png");
		credImg = new Sprite(texture);
		credImg.setSize(w, h);
		credImg.setOrigin(credImg.getWidth()/2, credImg.getHeight()/2);
		credImg.setPosition(-credImg.getWidth()/2, -credImg.getHeight()/2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		credImg.draw(batch);
		batch.end();
		
		credsMusic.resume(credsMusic_id);
		
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
			credsDone = true;
		}
	}

	@Override
	public void resize(int width, int height) {
		credImg.setSize(width, height);
	}

	@Override
	public void show() {
		credsMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/Finished Tracks/Menu_Credits.wav"));
		credsMusic_id = credsMusic.loop();
		render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

	public Screens getState() {
		if(credsDone) {
			credsDone = false;
			credsMusic.pause(credsMusic_id);
			return Screens.MENU;
		}
		return Screens.CREDITS;
	}

}
