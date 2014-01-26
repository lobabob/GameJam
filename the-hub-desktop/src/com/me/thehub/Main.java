 package com.me.thehub;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "The Hub";
		cfg.vSyncEnabled = true;
		cfg.useGL20 = true;
		cfg.width = 1000;
		cfg.height = 562;
		
		new LwjglApplication(new Driver(), cfg);
	}
}
