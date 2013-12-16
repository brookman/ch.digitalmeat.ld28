package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundRenderer {
	private SpriteBatch batch;
	private Texture groundTexture;
	private OrthographicCamera camera;
	private int groundLevel = 3;
	private int tileHeight = 32;
	private Texture skyTexture;
	public BackgroundRenderer(Texture ground, Texture sky, OrthographicCamera cam){
		this.groundTexture = ground;
		this.skyTexture = sky;
		this.camera = cam;
		batch = new SpriteBatch();
	}
	
	public void render(){
		renderSky();
		renderGround();
	}

	private void renderSky() {
		float w = skyTexture.getWidth();
		Config gc = ConcertSmugglers.instance.config;
		float startX = -camera.position.x;
		while(startX + w < 0){
			startX += w;
		}
		float endX = gc.xResolution;
		float pos = startX;
		float y = gc.yResolution / 2 - camera.position.y + groundLevel * tileHeight;
		batch.begin();
		while(pos < endX){
			batch.draw(skyTexture, pos, y);
			pos += w;
		}
		batch.end();
	}
	private void renderGround() {
		Config gc = ConcertSmugglers.instance.config;
		float w = groundTexture.getWidth() * camera.viewportWidth / gc.xResolution;
		float startX = -camera.position.x;
		while(startX + w < 0){
			startX += w;
		}
		float endX = gc.xResolution;
		float pos = startX;
		float y = gc.yResolution / 2 - camera.position.y + groundLevel * tileHeight - groundTexture.getHeight();
		batch.begin();
		while(pos < endX){
			batch.draw(groundTexture, pos, y);
			pos += w;
		}
		batch.end();
	}
}
