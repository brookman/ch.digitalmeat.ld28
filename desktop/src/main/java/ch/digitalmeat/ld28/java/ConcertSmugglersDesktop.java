package ch.digitalmeat.ld28.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ch.digitalmeat.ld28.core.Config;
import ch.digitalmeat.ld28.core.ConcertSmugglers;

public class ConcertSmugglersDesktop {
	public static void main(String[] args) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ld28";
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 480;

		new LwjglApplication(new ConcertSmugglers(), cfg);
	}
}