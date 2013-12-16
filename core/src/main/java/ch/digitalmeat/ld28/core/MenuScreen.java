package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen implements Screen{
	private Stage stage;
	
	public MenuScreen(){
		Config cfg = ConcertSmugglers.instance.config;
		stage = new Stage(cfg.xTarget, cfg.yTarget,true);
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
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
		
	}

}
