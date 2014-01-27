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
import com.me.thehub.Boss2;
import com.me.thehub.Bullet;
import com.me.thehub.Player;
import com.me.thehub.Driver.Screens;

public class Level2B extends AbstractLevel {

	// which layers to render
	private int[] layer1 = {0};
	
	// level triggers
	public Rectangle hub3;
	
	private Boss2 boss;

	public Level2B(Player player, SpriteBatch batch) 
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
		map = load.load("maps/BossRoomTwo/SecondBoss.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		collision = (TiledMapTileLayer)map.getLayers().get(0);

		// load entities
		this.player = player;
		bullets = new ArrayList<Bullet>();
		hub3 = new Rectangle(collision.getWidth() * collision.getTileWidth() - 10, 32, 32, 64);
		boss = new Boss2(1000, 32);
	}

	public Screens checkTriggers()
	{
		if(Intersector.overlaps(hub3, player.bounds)) {
			loaded = false;
			player.doubleJumpUnlocked = true;
			return Screens.HUB3;
		}
		else return Screens.LEVEL2B;
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
			player.setStart(96, 32, new Vector2(1, 0));
			loaded = true;
		}
		render(Gdx.graphics.getDeltaTime()); 
	}
	
	
}
