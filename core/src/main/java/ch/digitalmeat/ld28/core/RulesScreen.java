package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class RulesScreen implements Screen{
	private Stage stage;
	private ParticleEffect effect;
	private SpriteBatch batch;
	
	public RulesScreen(){
		this.batch = new SpriteBatch();
		Config cfg = ConcertSmugglers.instance.config;
		stage = new Stage(cfg.xTarget, cfg.yTarget,true);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		effect.update(delta);
		batch.begin();
		effect.draw(batch);
		batch.end();
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Config cfg = ConcertSmugglers.instance.config;
		Assets assets = ConcertSmugglers.instance.assets;
		this.effect = assets.introEffect();
		this.effect.setPosition(cfg.xResolution / 2, 0);
	}
	
	private void createIntroFadeText(Skin skin, float delay, String text,
			float textX, float textY) {
		Label introFadeText = new Label(text, skin);
		textX -= introFadeText.getWidth() / 2;
		textY -= introFadeText.getHeight() / 2;
		introFadeText.addAction(Actions.alpha(0));
		introFadeText.act(10);
		introFadeText.addAction(
			Actions.sequence(
				Actions.delay(delay)
				, Actions.parallel(
					Actions.moveBy(0, 200, 15)
					, Actions.sequence(
						Actions.fadeIn(1.5f)
						, Actions.delay(1f)
						, Actions.fadeOut(2.5f)
					)
				)
			)
		);
		introFadeText.setPosition(textX, textY);
		stage.addActor(introFadeText);
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
