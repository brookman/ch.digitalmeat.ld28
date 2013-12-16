package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class IntroScreen implements Screen {
	private Stage stage;
	private ParticleEffect effect;
	private SpriteBatch batch;
	private boolean started = false;
	public IntroScreen(){
		this.batch = new SpriteBatch();
	}
	
	@Override
	public void render(float delta) {
		PlayerController c = ConcertSmugglers.instance.controller;
		c.update();
		if(c.any){
			Gdx.app.log("", "Update");
			ConcertSmugglers.instance.game();
			return;
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(started){
			effect.update(delta);
			batch.begin();
			effect.draw(batch);
			batch.end();
		}
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		Config cfg = ConcertSmugglers.instance.config;
		stage.setViewport(cfg.xTarget, cfg.yTarget, false);
	}

	@Override
	public void show() {
		Config cfg = ConcertSmugglers.instance.config;
		stage = new Stage(cfg.xTarget, cfg.yTarget, true);
		Assets assets = ConcertSmugglers.instance.assets;
		Image image = new Image(assets.trance);
		this.effect = assets.introEffect();
		this.effect.setPosition(cfg.xResolution / 2, 0);
		Skin skin = assets.skin;
		image.setWidth(64);
		image.setHeight(128);
		float x = cfg.xTarget / 2 - image.getWidth() / 2 ;
		image.setPosition(x, - image.getHeight());
		image.addAction(Actions.alpha(0));
		image.act(10);
		
		image.addAction(
				Actions.sequence(Actions.delay(2.5f),
				Actions.parallel(
					Actions.sequence(
						Actions.parallel(
							Actions.moveTo(x, 0, 8f)
							, Actions.sequence(Actions.alpha(0.25f, 6f), Actions.alpha(1f, 2f))
						)
						, Actions.forever(
							Actions.sequence(
								Actions.moveBy(0, 30, 3f)
								, Actions.moveBy(0, -30, 3f)
							)
						)

					)
					, Actions.forever(
						Actions.sequence(
							Actions.rotateTo(-4, 5f)
							, Actions.rotateTo(4, 5f)
						)
					)
				))
		);
		Label label = new Label("You only\nget one...", skin);
		label.setPosition(20, cfg.yTarget / 2 - 5 - label.getHeight() / 2);
		label.addAction(Actions.alpha(0));
		label.act(10f);
		label.addAction(
			Actions.sequence(
				Actions.delay(10f)
				, Actions.parallel(
					Actions.fadeIn(2f)
					, Actions.run(new Runnable() {							
						@Override
						public void run() {
							effect.start();
							started = true;
						}
					})
					, Actions.forever(
						Actions.sequence(
							Actions.moveBy(0, 50f, 2f)
							, Actions.moveBy(0, -50f, 2f)
						)
					)
					, Actions.forever(
							Actions.sequence(
								Actions.rotateBy(10f, 2f)
								, Actions.rotateBy(-10f, 2f)
							)
						)
				)
			)
		);
		Label label2 = new Label("Ticket", skin);
		label2.setPosition(cfg.xTarget - label2.getWidth() - 30, cfg.yTarget / 2 - 5 - label2.getHeight() / 2);
		label2.addAction(Actions.alpha(0));
		label2.act(10f);
		label2.addAction(
			Actions.sequence(
				Actions.delay(10f)
				, Actions.parallel(
					Actions.fadeIn(2f)
					, Actions.forever(
						Actions.sequence(
							Actions.moveBy(0, 50f, 2f)
							, Actions.moveBy(0, -50f, 2f)
						)
					)
					, Actions.forever(
							Actions.sequence(
								Actions.rotateBy(10f, 2f)
								, Actions.rotateBy(-10f, 2f)
							)
						)
				)
			)
		);
		
		Label title = new Label("Concert Crashers", skin, "title");
		title.setPosition(cfg.xTarget / 2 - title.getWidth() / 2, cfg.yTarget - title.getHeight() - 20);
		title.addAction(Actions.alpha(0));
		title.act(10);
		title.addAction(
			Actions.sequence(
				Actions.delay(11f)
				, Actions.parallel(
					Actions.fadeIn(3f)
					, Actions.forever(
						Actions.sequence(
							Actions.moveBy(0f, 10f, 2f)
							, Actions.moveBy(0f, -10f, 2f)
						)
					)
				)
			)
		);
		stage.addActor(image);
		stage.addActor(label);
		stage.addActor(label2);
		stage.addActor(title);
		float textX = cfg.xTarget / 2;
		float textY = cfg.yTarget / 2 + 20;
		createIntroFadeText(skin, 0.5f, "You only get one...", textX, textY);
		createIntroFadeText(skin, 2.5f, "A ludum dare 28 game", textX, textY);
		createIntroFadeText(skin, 4.5f, "Design & GFX: dhy", textX, textY);
		createIntroFadeText(skin, 5.2f, "www.facebook.com/DhyanaFairytales", textX, textY);
		createIntroFadeText(skin, 6.5f, "Design & Code: atombrot", textX, textY);
		createIntroFadeText(skin, 7.2f, "www.atombrot.com", textX, textY);
		createIntroFadeText(skin, 8.5f, "Music: mentex", textX, textY);
		createIntroFadeText(skin, 9.2f, "www.mentex.ch", textX, textY);


		createPressAnyKeyText(skin, 12, "Press Any Key to Start", textX, 20);
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
	private void createPressAnyKeyText(Skin skin, float delay, String text,
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
					Actions.forever(
						Actions.sequence(
							Actions.moveBy(0, 20, 1)
							, Actions.moveBy(0, -20, 1)
						)
					)
					, Actions.sequence(
						Actions.fadeIn(0.5f)
						, Actions.delay(1f)
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
