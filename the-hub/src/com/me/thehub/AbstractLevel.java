package com.me.thehub;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class AbstractLevel implements Screen {
	
	// rendering tools
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected OrthogonalTiledMapRenderer tmr;
	
	// graphics objects
	protected Player player;
	protected TiledMap map;
	protected TiledMapTileLayer collision;
	protected ArrayList<Bullet> bullets;
	
	// game window attributes
	protected float w;
	protected float h;
	public boolean loaded = false;
	

	protected void updateCamera()
	{
		// center camera on player
		float newX = player.x;
		float newY = player.y;

		// make sure camera doesn't leave the level
		if(player.player.getX() < w/2)
			newX = w/2;
		else if(player.player.getX() > collision.getWidth() * collision.getTileWidth() - w/2)
			newX = collision.getWidth() * collision.getTileWidth() - w/2;
		if(player.player.getY() < h/2)
			newY = h/2;
		else if(player.player.getY() > collision.getHeight() * collision.getTileHeight() - h/2)
			newY = collision.getHeight() * collision.getTileHeight() - h/2;

		// set new camera position
		camera.position.set(newX, newY, 0);
		batch.setProjectionMatrix(camera.combined);
	}
	
	protected void spawnPlayerBullets()
	{
		// shoot shit (one bullet at a time) if gun is unlocked
		if(Gdx.input.isKeyPressed(Keys.X) && player.canShoot && player.gunUnlocked) { 
			bullets.add(player.shoot());
			player.canShoot = false;
		}
		else if(!Gdx.input.isKeyPressed(Keys.X))
			player.canShoot = true;
	}
	
	// get rid of useless objects
	protected void cleanUp()
	{
		Bullet b;
		Iterator<Bullet> it = bullets.iterator();
		while(it.hasNext())
		{
			b = it.next();
			if(b.x > collision.getTileWidth() * collision.getWidth())
				it.remove();
		}
	}
	
	@Override
	public void render(float delta) {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
