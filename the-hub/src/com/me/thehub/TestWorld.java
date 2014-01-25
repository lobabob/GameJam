package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class TestWorld implements Screen {

	// rendering tools
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private OrthogonalTiledMapRenderer tmr;
	private int[] layer1 = {0, 1};

	// graphics
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
		batch.end();
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
