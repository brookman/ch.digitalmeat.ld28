package ch.digitalmeat.ld28.core.html;

import ch.digitalmeat.ld28.core.core.ConcertSmugglers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class ConcertSmugglersHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new ConcertSmugglers();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
