package com.me.thehub.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.thehub.Driver.Screens;

public class Menu implements Screen {
	
	private boolean goToPlay = false;		// Flag that monitors game state
	private boolean goToCredits = false;		// Flag that monitors game state
	
	private Sound menuMusic;
	private long menuMusic_id;
	
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonPlay, buttonCredits, buttonExit;
	private BitmapFont titleFont, buttonFont, hackerFont;
	private Label title;
	
	public Menu() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);
		
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Creating fonts
		titleFont = new BitmapFont(Gdx.files.internal("fonts/title.fnt"), false);
		buttonFont = new BitmapFont(Gdx.files.internal("fonts/button.fnt"), false);
		hackerFont = new BitmapFont(Gdx.files.internal("fonts/console.fnt"), false);
		
		// Creating buttons
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button");
		textButtonStyle.down = skin.getDrawable("button");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = buttonFont;
		textButtonStyle.fontColor = Color.GREEN;
		
		buttonPlay = new TextButton("./play", textButtonStyle);
		buttonPlay.pad(15, 70, 15, 70);
		buttonCredits = new TextButton("./credits", textButtonStyle);
		buttonCredits.pad(15, 57, 15, 57);
		buttonExit = new TextButton("./quit", textButtonStyle);
		buttonExit.pad(15, 71, 15, 71);
		
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToPlay = true;				
			}
		});
		buttonCredits.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				goToCredits = true;
			}
		});
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				menuMusic.stop(menuMusic_id);
				Gdx.app.exit();
			}
		});
		
		
		// Creating heading		
		title = new Label("*The_Hub", new LabelStyle(titleFont, Color.GREEN));
		title.setFontScale(2.15f);	
		
		// Adding things together	
		stage.addActor(table);
		
		table.add(title);
		table.getCell(title).spaceBottom(75);
		
		table.row();
		table.add(buttonPlay);
		table.getCell(buttonPlay).spaceBottom(15);
		
		table.row();
		table.add(buttonCredits);
		table.getCell(buttonCredits).spaceBottom(15);
		
		table.row();
		table.add(buttonExit);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);
		
		menuMusic.resume(menuMusic_id);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		table.setSize(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		menuMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/Finished Tracks/Menu_Credits.wav"));
		menuMusic_id = menuMusic.loop();
		render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		titleFont.dispose();
		buttonFont.dispose();
		hackerFont.dispose();
		skin.dispose();
		
	}
	
	public Screens getState() {
		if(goToPlay) {
			goToPlay = false;
			menuMusic.pause(menuMusic_id);
			return Screens.LEVEL1;
		} else if(goToCredits) {
			menuMusic.pause(menuMusic_id);
			goToCredits = false;
			return Screens.CREDITS;
		}
		return Screens.MENU;
	}

}
