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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;


public class TestWorld implements Screen {

	// rendering tools
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer tmr;
	private int[] layer1 = {0, 1};

	// graphics
	private ArrayList<Bullet> bullets;
	private Player player;
	private TiledMap map;
	private TiledMapTileLayer collision;

	// game window attributes
	private float w;
	private float h;

	public TestWorld(SpriteBatch batch) 
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
		player = new Player(32, 32, collision);
		bullets = new ArrayList<Bullet>();
	}

	private void update(float delta)
	{
		// center camera on player
		float newX = player.player.getX();
		float newY = player.player.getY();

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
		
		// shoot shit (one bullet at a time) if gun is unlocked
		if(Gdx.input.isKeyPressed(Keys.X) && player.canShoot && player.gunUnlocked) { 
			bullets.add(player.shoot());
			player.canShoot = false;
		}
		else if(!Gdx.input.isKeyPressed(Keys.X))
			player.canShoot = true;
	}
	
	// get rid of useless objects
	private void cleanUp()
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
	public void show() { render(Gdx.graphics.getDeltaTime()); }

	@Override
	public void hide() {}
}
