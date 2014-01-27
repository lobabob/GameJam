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
import com.me.thehub.Bullet;
import com.me.thehub.Player;
import com.me.thehub.Driver.Screens;

public class Level3 extends AbstractLevel {

	// which layers to render
	private int[] layer1 = {0};
	
	// level triggers
	public Rectangle level3boss;

	public Level3(Player player, SpriteBatch batch) 
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
		map = load.load("maps/LevelThree/LevelThree.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		collision = (TiledMapTileLayer)map.getLayers().get(0);

		// load entities
		this.player = player;
		bullets = new ArrayList<Bullet>();
		level3boss = new Rectangle(collision.getWidth() * collision.getTileWidth() - 10, 20*32, 32, 64);
	}

	public Screens checkTriggers()
	{
		if(Intersector.overlaps(level3boss, player.bounds)) {
			loaded = false;
			return Screens.LEVEL3B;
		}
		else return Screens.LEVEL3;
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
