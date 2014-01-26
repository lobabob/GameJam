package com.me.thehub;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class PlayerInputProcessor implements InputProcessor {

	public PlayerInputProcessor() {}
	
	@Override
	public boolean keyDown(int keycode) {
		if(Gdx.input.isKeyPressed(keycode))
			return true;
		
		else return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(!Gdx.input.isKeyPressed(keycode))
			return true;
		
		else return false;
	}
	
	/************************************************************************/
	/*********** Rest is not needed, implement later if I'm wrong ***********/
	/************************************************************************/

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }
}
