package com.me.thehub.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.thehub.AbstractLevel;
import com.me.thehub.Boss1;
import com.me.thehub.Bullet;
import com.me.thehub.Player;
import com.me.thehub.Driver.Screens;

public class Level1B extends AbstractLevel {

	// which layers to render
	private int[] layer1 = {0};
	
	// level triggers
	public Rectangle hub2;
	
	private Boss1 boss;

	public Level1B(Player player, SpriteBatch batch) 
	{		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();

		// load camera
		camera = new OrthographicCamera(w, h);
		camera.translate(w/2, h/2);

		// load SpriteBatch
		this.batch = batch;
		batch.setProjectionMatrix(camera.combined);

		// load map and collision layer
		TmxMapLoader load = new TmxMapLoader();
		map = load.load("maps/BossRoomOne/FirstBoss.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		collision = (TiledMapTileLayer)map.getLayers().get(0);

		// load entities
		this.player = player;
		boss = new Boss1(1000, 32);
		bullets = new ArrayList<Bullet>();
		hub2 = new Rectangle(collision.getWidth() * collision.getTileWidth() - 10, 32, 32, 64);
	}

	public Screens checkTriggers()
	{
		if(Intersector.overlaps(hub2, player.bounds)) {
			loaded = false;
			player.gunUnlocked = true;
			return Screens.HUB2;
		}
		else return Screens.LEVEL1B;
	}
	
	private void update(float delta)
	{
		updateCamera();
		spawnPlayerBullets();
	}

	@Override
	public void render(float delta) 
	{	
		update(delta);

		// update the camera and map view
		tmr.setView(camera);
		camera.update();

		// draw background, platforms
		tmr.render(layer1);

		// draw entities
		batch.begin();

		player.draw(batch);	
		for(Bullet b: bullets)
			b.draw(batch);
		boss.draw(batch);

		batch.end();
		
		cleanUp();
	}

	@Override
	public void show() { 
		if(!loaded) {
			player.setCollision(collision);
			player.setStart(32, 32, new Vector2(1, 0));
			loaded = true;
		}
		render(Gdx.graphics.getDeltaTime()); 
	}
	
	
}
