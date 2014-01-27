package com.me.thehub.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.thehub.AbstractLevel;
import com.me.thehub.Boss3;
import com.me.thehub.Bullet;
import com.me.thehub.Player;
import com.me.thehub.Driver.Screens;

public class Level3B extends AbstractLevel {
	
	// which layers to render
	private int[] layer1 = {0};
	
	// level triggers
	public Rectangle thehub4;

	private Boss3 boss;
	private ShapeRenderer sr;
	
	public Level3B(Player player, SpriteBatch batch) 
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
		map = load.load("maps/BossRoomThree/ThirdBoss.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		collision = (TiledMapTileLayer)map.getLayers().get(0);

		// load entities
		this.player = player;
		bullets = new ArrayList<Bullet>();
		thehub4 = new Rectangle(collision.getWidth() * collision.getTileWidth() - 10, 32, 32, 64);
		boss = new Boss3(1000, 32);
		
		sr = new ShapeRenderer();
		sr.setProjectionMatrix(camera.combined);
		sr.setColor(0, 0, 0, 1);
	}

	public Screens checkTriggers()
	{
		if(Intersector.overlaps(thehub4, player.bounds)) {
			loaded = false;
			player.bikeUnlocked = true;
			return Screens.SECRET;
		}
		else return Screens.LEVEL3B;
	}
	
	private void update(float delta)
	{
		updateCamera();
		spawnPlayerBullets();
		
		for(int i = 0; i < boss.platforms.length; i++)
		{
			if(Intersector.overlaps(player.bounds, boss.platforms[i]))
				player.preventFall();
		}
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
		
		sr.setColor(0, 0, 0, 1);
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Filled);
		boss.draw(sr, batch);
		sr.end();
		
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
