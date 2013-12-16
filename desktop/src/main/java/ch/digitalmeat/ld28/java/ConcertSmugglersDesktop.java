package ch.digitalmeat.ld28.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ch.digitalmeat.ld28.core.ConcertSmugglers;

public class ConcertSmugglersDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		new LwjglApplication(new ConcertSmugglers(), config);
	}
}
