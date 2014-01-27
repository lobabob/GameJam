package com.me.thehub;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.thehub.screens.Credits;
import com.me.thehub.screens.Final;
import com.me.thehub.screens.Hub1;
import com.me.thehub.screens.Hub2;
import com.me.thehub.screens.Hub3;
import com.me.thehub.screens.Level1;
import com.me.thehub.screens.Level1B;
import com.me.thehub.screens.Level2;
import com.me.thehub.screens.Level2B;
import com.me.thehub.screens.Level3;
import com.me.thehub.screens.Level3B;
import com.me.thehub.screens.Menu;
import com.me.thehub.screens.Splash;

public class Driver implements ApplicationListener {

	// rendering tools
	private SpriteBatch batch;
	private Player player;
	
	// screens
	public enum Screens { SPLASH, MENU, HUB1, LEVEL1, LEVEL1B, HUB2, LEVEL2, LEVEL2B, HUB3, LEVEL3, 
		LEVEL3B, HUB4, FINAL, SECRET, CREDITS };
		
	private Screen splash;
	private Screen menu;
	private Screen credits;
	private Screen hub1;
	private Screen level1;
	private Screen level1b;
	private Screen hub2;
	private Screen level2;
	private Screen level2b;
	private Screen hub3;
	private Screen level3;
	private Screen level3b;
	private Screen finalboss;
	private Screen secret;
	
	// songs
	Music hubsong;
	Music level1song;
	Music level1bsong;
	Music level2song;
	Music level2bsong;
	Music level3song;
	Music level3bsong;
	Music finalsong;
	Sound congrats;
	
	private Screens currentScreen = Screens.SPLASH;
	private boolean clipPlayed =  false;
	
	@Override
	public void create() 
	{	
		batch = new SpriteBatch();
		player = new Player();
		splash = new Splash();
		hub1 = new Hub1(player, batch);
		level1 = new Level1(player, batch); 
		level1b = new Level1B(player, batch);
		hub2 = new Hub2(player, batch);
		level2 = new Level2(player, batch);
		level2b = new Level2B(player, batch);
		hub3 = new Hub3(player, batch);
		level3 = new Level3(player, batch);
		level3b = new Level3B(player, batch);
		finalboss = new Final(player, batch);
		secret = new TestWorld(player, batch);

		hubsong =     Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Hub.wav"));
		level1song =  Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Level_A.wav"));
		level1bsong = Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Boss_A.wav"));
		level2song =  Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Level_B.wav"));
		level2bsong = Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Boss_B.wav"));
		level3song =  Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Level_C.wav"));
		level3bsong = Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Boss_C.wav"));
		finalsong =   Gdx.audio.newMusic(Gdx.files.internal("sounds/Finished Tracks/Boss_D.wav"));
		congrats =    Gdx.audio.newSound(Gdx.files.internal("sounds/congrats.wav"));
		
		hubsong.setLooping(true);
		level1song.setLooping(true);
		level1bsong.setLooping(true);
		level2song.setLooping(true);
		level2bsong.setLooping(true);
		level3song.setLooping(true);
		level3bsong.setLooping(true);
		finalsong.setLooping(true);
		
		splash.show();
	}

	@Override
	public void render() 
	{		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		switch(currentScreen)
		{
		case SPLASH:
			splash.render(Gdx.graphics.getDeltaTime());
			currentScreen = ((Splash) splash).getState();
			break;
		case MENU:
			if(menu == null) {
				menu = new Menu();
				menu.show();
			}
			menu.render(Gdx.graphics.getDeltaTime());
			currentScreen = ((Menu) menu).getState();
			break;
		case CREDITS:
			if(credits == null) {
				credits = new Credits(batch);
				credits.show();
			}
			credits.render(Gdx.graphics.getDeltaTime());
			currentScreen = ((Credits) credits).getState();
			break;
		case HUB1:
			hub1.show();
			if(!hubsong.isPlaying())
				hubsong.play();
			currentScreen = ((Hub1) hub1).checkTriggers();
			break;
		case LEVEL1:
			level1.show();
			hubsong.pause();
			if(!level1song.isPlaying())
				level1song.play();
			currentScreen = ((Level1) level1).checkTriggers();
			break;
		case LEVEL1B:
			level1b.show();
			level1song.pause();
			if(!level1bsong.isPlaying())
				level1bsong.play();
			currentScreen = ((Level1B) level1b).checkTriggers();
			break;
		case HUB2:
			hub2.show();
			level1bsong.pause();
			if(!hubsong.isPlaying())
				hubsong.play();
			currentScreen = ((Hub2) hub2).checkTriggers();
			break;
		case LEVEL2:
			level2.show();
			hubsong.pause();
			if(!level2song.isPlaying())
				level2song.play();
			currentScreen = ((Level2) level2).checkTriggers();
			break;
		case LEVEL2B:
			level2b.show();
			level2song.pause();
			if(!level2bsong.isPlaying())
				level2bsong.play();
			currentScreen = ((Level2B) level2b).checkTriggers();
			break;
		case HUB3:
			hub3.show();
			level2bsong.pause();
			if(!hubsong.isPlaying())
				hubsong.play();
			currentScreen = ((Hub3) hub3).checkTriggers();
			break;
		case LEVEL3:
			level3.show();
			hubsong.pause();
			if(!level3song.isPlaying())
				level3song.play();
			currentScreen = ((Level3) level3).checkTriggers();
			break;
		case LEVEL3B:
			level3b.show();
			level3song.pause();
			if(!level3bsong.isPlaying())
				level3bsong.play();
			currentScreen = ((Level3B) level3b).checkTriggers();
			break;
		case SECRET:
			secret.show();
			if(!clipPlayed)
				congrats.play(1.0f);
			
			clipPlayed = true;
			
			level3bsong.pause();
			if(!hubsong.isPlaying())
				hubsong.play();
			break;
		case FINAL:
			finalboss.show();
			level3bsong.pause();
			if(!finalsong.isPlaying())
				finalsong.play();
			currentScreen = ((Final) finalboss).checkTriggers();
			break;
		default:
			break;
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
