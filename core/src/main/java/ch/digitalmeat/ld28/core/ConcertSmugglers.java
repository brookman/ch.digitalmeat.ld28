package ch.digitalmeat.ld28.core;

import java.util.Random;

import ch.digitalmeat.ld28.core.person.ai.PersonAi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class ConcertSmugglers extends Game {
	public static ConcertSmugglers instance;
	
	public final Config config;
	public final Assets assets;
	public final Random random;
	public final PlayerController controller;
	public MapRenderer mapRenderer;
	
	
	public InGameScreen inGameScreen;

	private IntroScreen introScreen;

	public TextManager textManager;

	private MenuScreen menuScreen;
	
	public boolean running;
	
	public ConcertSmugglers(Config config){
		
		instance = this;
		random = new Random();
		this.config = config;
		this.assets = new Assets();
		this.controller = new PlayerController();
		controller.addTrapsToList();
	}
	
	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		config.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		assets.create();
		this.textManager = new TextManager();
		this.inGameScreen = new InGameScreen();
		this.introScreen = new IntroScreen();
		this.menuScreen = new MenuScreen();
		PersonAi.buildAi();
		intro();
		//game();
	}

	public void intro() {
		setScreen(introScreen);
	}

	public void menu() {
		setScreen(menuScreen);
	}

	@Override
	public void dispose(){
		assets.dispose();
	}

	@Override
	public void render() {		
		super.render();		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public void game(){
		setScreen(inGameScreen);
	}
}
