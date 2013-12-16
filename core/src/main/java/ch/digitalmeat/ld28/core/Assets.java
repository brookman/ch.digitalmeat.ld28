package ch.digitalmeat.ld28.core;

import java.util.Random;

import ch.digitalmeat.ld28.core.person.PersonSheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	public static final int PERSON_SPRITES = 8;
	public static final int PERSON_HEIGHT = 16;
	public static final int PERSON_WIDTH = 16;
	private final AssetManager manager;
	public PersonSheet[] sheets;
	public Texture ground;
	public Texture sky;
	public ParticleEffect playerEffect;
	public BitmapFont font_visitor_10;
	public BitmapFont font_visitor_25;
	public Skin skin;
	public Texture androidButtons;
	private Music music;
	public String[] names;
	public Texture trance;
	public Texture blank;
	public String[] levels;
	
	public static Color gold = new Color(1f, 0.8f, 0f, 1f);
	private ParticleEffect introEffect;
	
	public Assets()
	{
		this.manager = new AssetManager();
	}
	public String randomName(){
		Random random = ConcertSmugglers.instance.random;
		return names[random.nextInt(names.length)];
	}
	
	public void create(){
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));		
		manager.load("data/Persons.png", Texture.class);
		manager.load("data/PeopleParts.png", Texture.class);
		manager.load("data/ground.png", Texture.class);
		manager.load("data/blank.png", Texture.class);
		manager.load("data/trance.png", Texture.class);
		manager.load("data/sky.png", Texture.class);
		manager.load("data/visitor_10.fnt", BitmapFont.class);
		manager.load("data/visitor_25.fnt", BitmapFont.class);
		manager.load("data/uiskin.json", Skin.class);
		manager.load("data/android_controls.png", Texture.class);
		manager.load("data/mentex_track_01.wav", Music.class);
		manager.finishLoading();
		this.skin = manager.get("data/uiskin.json");
		ground = manager.get("data/ground.png");
		trance = manager.get("data/trance.png");
		blank = manager.get("data/blank.png");
		sky = manager.get("data/sky.png");
		font_visitor_10 = manager.get("data/visitor_10.fnt");
		font_visitor_25 = manager.get("data/visitor_25.fnt");
		sheets = new PersonSheet[PERSON_SPRITES];
		music = manager.get("data/mentex_track_01.wav");
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();
		String namesString = Gdx.files.internal("data/names.txt").readString();
		this.names = namesString.split("\n");
		String levelsString = Gdx.files.internal("data/levels.txt").readString();
		this.levels = levelsString.split(";");
		playerEffect = playerEffect();		
		Texture personsTexture = manager.get("data/PeopleParts.png");
		this.androidButtons = manager.get("data/android_controls.png");
		int persons = 8;
		for(int index = 0; index < persons; index++){
			PersonSheet sheet = new PersonSheet();
			sheet.front[PersonSheet.SHEET_LEGS] = new TextureRegion[1];			
			sheet.front[PersonSheet.SHEET_LEGS][0] = pr(personsTexture, px(0), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_LEGS] = new TextureRegion[2];			
			sheet.side_idle[PersonSheet.SHEET_LEGS][0] = pr(personsTexture, px(1), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_LEGS][1] = pr(personsTexture, px(2), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_LEGS] = new TextureRegion[4];			
			sheet.side_walk[PersonSheet.SHEET_LEGS][0] = pr(personsTexture, px(3), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_LEGS][1] = pr(personsTexture, px(1), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_LEGS][2] = pr(personsTexture, px(4), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_LEGS][3] = pr(personsTexture, px(2), py(index)); 
			
			sheet.front[PersonSheet.SHEET_TORSO] = new TextureRegion[1];
			sheet.front[PersonSheet.SHEET_TORSO][0] = pr(personsTexture, px(5), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_TORSO] = new TextureRegion[2];			
			sheet.side_idle[PersonSheet.SHEET_TORSO][0] = pr(personsTexture, px(6), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_TORSO][1] = pr(personsTexture, px(7), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_TORSO] = new TextureRegion[4];			
			sheet.side_walk[PersonSheet.SHEET_TORSO][0] = pr(personsTexture, px(8), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_TORSO][1] = pr(personsTexture, px(6), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_TORSO][2] = pr(personsTexture, px(9), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_TORSO][3] = pr(personsTexture, px(7), py(index)); 
			
			sheet.front[PersonSheet.SHEET_HEAD] = new TextureRegion[1];
			sheet.front[PersonSheet.SHEET_HEAD][0] = pr(personsTexture, px(10), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_HEAD] = new TextureRegion[2];			
			sheet.side_idle[PersonSheet.SHEET_HEAD][0] = pr(personsTexture, px(11), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_HEAD][1] = pr(personsTexture, px(12), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HEAD] = new TextureRegion[4];			
			sheet.side_walk[PersonSheet.SHEET_HEAD][0] = pr(personsTexture, px(13), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HEAD][1] = pr(personsTexture, px(11), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HEAD][2] = pr(personsTexture, px(14), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HEAD][3] = pr(personsTexture, px(12), py(index)); 
			
			sheet.front[PersonSheet.SHEET_HAIR] = new TextureRegion[1];
			sheet.front[PersonSheet.SHEET_HAIR][0]  = pr(personsTexture, px(15), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_HAIR] = new TextureRegion[2];			
			sheet.side_idle[PersonSheet.SHEET_HAIR][0] = pr(personsTexture, px(16), py(index)); 
			sheet.side_idle[PersonSheet.SHEET_HAIR][1] = pr(personsTexture, px(17), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HAIR] = new TextureRegion[4];			
			sheet.side_walk[PersonSheet.SHEET_HAIR][0] = pr(personsTexture, px(18), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HAIR][1] = pr(personsTexture, px(16), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HAIR][2] = pr(personsTexture, px(19), py(index)); 
			sheet.side_walk[PersonSheet.SHEET_HAIR][3] = pr(personsTexture, px(17), py(index)); 
			
			
			sheets[index] = sheet;
		}
	}

	public ParticleEffect playerEffect() {
		ParticleEffect playerEffect = new ParticleEffect();
		playerEffect.load(Gdx.files.internal("data/player.p"), Gdx.files.internal("data"));
		return playerEffect;
	}
	public ParticleEffect introEffect() {
		if(introEffect == null){
			introEffect = new ParticleEffect();
			introEffect.load(Gdx.files.internal("data/intro.p"), Gdx.files.internal("data"));
		}
		return introEffect;
	}
	public ParticleEffect guardEffect(){
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/guard.p"), Gdx.files.internal("data"));
		return effect;
	}
	private TextureRegion pr(Texture texture, int x, int y){
		return new TextureRegion(texture, x, y, PERSON_WIDTH, PERSON_HEIGHT);
	}
	
	private int px(int position){
		return position * PERSON_WIDTH;
	}
	private int py(int position){
		return position * PERSON_HEIGHT;
	}
	
	public TiledMap loadTilemap(String file){
		manager.load(file, TiledMap.class);
		manager.finishLoading();
		return manager.get(file);
	}
	
	public void dispose(){
		music.dispose();
		music = null;
	}
}
