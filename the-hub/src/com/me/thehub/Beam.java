package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Beam {
	
	private Sprite beam;
	
	public Beam(float x, float y)
	{
		Texture beamTex = new Texture(Gdx.files.internal("entities/beam.png"));
		beam = new Sprite(beamTex);
		beam.setPosition(x, y);
	}
	
	public void draw(SpriteBatch batch) {
		beam.draw(batch);
	}

}
