package com.me.thehub.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.me.thehub.Driver.Screens;

public class Splash implements Screen {
	
	private boolean splashOver = false;
	
	private Stage stage;
	private Table table;
	private BitmapFont hackerFont;
	private Label line1, line2, line3, line4, line5, line6, line7, line8, line9;
	
	private Sound typing;
	private long typing_id;
	
	private String line1_command = "./game_of_awesome";
	private boolean doneTyping = false;
	private float timeSinceCollision = 0;
	private int lineToPrint = 2;
	private int commandChar = 0;	// Starting at 0 instead of 1 gives a slight pause before the animation starts
	
	public Splash() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		typing = Gdx.audio.newSound(Gdx.files.internal("sounds/Sound Effects/Typing.wav"));
		typing_id = typing.loop();
		
		table = new Table();
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Creating fonts
		hackerFont = new BitmapFont(Gdx.files.internal("fonts/console.fnt"), false);	
		
		// Creating Splash loading screen
		line1 = new Label("root@thehub:~# ", new LabelStyle(hackerFont, Color.GREEN));
		line2 = new Label("Unpacking platforms....", new LabelStyle(hackerFont, Color.GREEN));
		line3 = new Label("Polishing weapons....", new LabelStyle(hackerFont, Color.GREEN));
		line4 = new Label("Setting up baddies....", new LabelStyle(hackerFont, Color.GREEN));
		line5 = new Label("Taking a nap....", new LabelStyle(hackerFont, Color.GREEN));
		line6 = new Label("Resuming....", new LabelStyle(hackerFont, Color.GREEN));
		line7 = new Label("Browsing /hidden/dead_end/school/porn....", new LabelStyle(hackerFont, Color.GREEN));
		line8 = new Label("Cleaning up....", new LabelStyle(hackerFont, Color.GREEN));
		line9 = new Label("Starting game....", new LabelStyle(hackerFont, Color.GREEN));
		
		// Adding things together
		stage.addActor(table);
		
		table.left().top();
		table.add(line1).left();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		if(!doneTyping) {
			timeSinceCollision += delta;
			
			if(timeSinceCollision > .035f) {
				if(commandChar <= line1_command.length()) {
					table.removeActor(line1);
					line1 = new Label("root@thehub:~# "+line1_command.substring(0, commandChar), new LabelStyle(hackerFont, Color.GREEN));
					table.add(line1).left();
					commandChar++;
				} else {
					
					table.row();
					doneTyping = true;
				}
				timeSinceCollision = 0f;
			}
		} else {
			timeSinceCollision += delta;
			
			if(lineToPrint < 10) {
				if(timeSinceCollision > Math.random()) {
					if(lineToPrint == 2)
						table.add(line2).left();
					else if(lineToPrint == 3)
						table.add(line3).left();
					else if(lineToPrint == 4)
						table.add(line4).left();
					else if(lineToPrint == 5)
						table.add(line5).left();
					else if(lineToPrint == 6)
						table.add(line6).left();
					else if(lineToPrint == 7)
						table.add(line7).left();
					else if(lineToPrint == 8)
						table.add(line8).left();
					else if(lineToPrint == 9)
						table.add(line9).left();
					
					table.row();
					lineToPrint++;
					timeSinceCollision = 0f;
				}
			} else
				if(timeSinceCollision > 1f) {
					typing.stop(typing_id);
					splashOver = true;
				}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		table.setSize(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
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
		hackerFont.dispose();
	}
	
	public Screens getState() {
		if(splashOver) {
			splashOver = false;
			return Screens.MENU;
		}
		return Screens.SPLASH;
	}

}
