package com.me.thehub;

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
import com.me.thehub.Driver.Screens;


public class TestWorld extends AbstractLevel {

	// which layers to render
	private int[] layer1 = {0, 1};
	
	// level triggers
	public Rectangle level2;
	
	/*****DEBUG****/
	private ShapeRenderer sr;
	/*****DEBUG****/
	
	// entities
	private Boss1 boss;

	public TestWorld(Player player, SpriteBatch batch) 
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
		map = load.load("maps/testroom/testroom.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		collision = (TiledMapTileLayer)map.getLayers().get(1);

		// load entities
		this.player = player;
		bullets = new ArrayList<Bullet>();
		level2 = new Rectangle(collision.getWidth() * collision.getTileWidth() - 4, 32, 32, 64);
		
		/****DEBUG****/
		sr = new ShapeRenderer();
		/*****DEBUG***/
		
		boss = new Boss1(128, 32);
	}

	public Screens checkTriggers()
	{
		if(Intersector.overlaps(level2, player.bounds)) {
			loaded = false;
			return Screens.LEVEL2;
		}
		
		else return Screens.LEVEL1;
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

		/****DEBUG****/
		sr.setProjectionMatrix(camera.combined);
		sr.setColor(1, 1, 1, 1);
		sr.begin(ShapeType.Filled);
		sr.rect(level2.x, level2.y, level2.width, level2.height);
		sr.end();
		/*****DEBUG****/
		
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
	
	@Override
	public void dispose() { 
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}
}
