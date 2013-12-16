package ch.digitalmeat.ld28.core;

import java.util.List;

import ch.digitalmeat.ld28.core.person.Person;
import ch.digitalmeat.ld28.core.person.Person.LookingDirection;
import ch.digitalmeat.ld28.core.person.Person.PersonState;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class InGameScreen implements Screen{
	private MapRenderer mapRenderer;
	private OrthographicCamera camera;
	private BackgroundRenderer backgroundRenderer;
	private ConcertSmugglers cs;
	
	private Stage androidControls;
	private ImageButton leftButton;
	private ImageButton rightButton;
	private ImageButton switchButton;
	private ImageButton actionButton;
	private Stage uiStage;
	private Table playersTable;
	private Image sightBlocker;
	private Label wonLabel;
	private Label lostLabel;
	private Label lostKeyLabel;
	private Label wonKeyLabel;
	private float stoppedTimer;
	private boolean stopped;
	private boolean won;
	private int level = 0;
	
	public InGameScreen(){
		this.cs = ConcertSmugglers.instance;
		int w = cs.config.xResolution;
		int h = cs.config.yResolution;
		this.camera = new OrthographicCamera(w, h);
		mapRenderer = new MapRenderer();
		cs.mapRenderer = mapRenderer;
		mapRenderer.create(camera);
		camera.update();
		uiStage = new Stage(cs.config.xTarget, cs.config.yTarget, true);
		Assets assets = cs.assets;
		this.backgroundRenderer = new BackgroundRenderer(assets.ground, assets.sky, camera);
		androidControls = new Stage(cs.config.xTarget, cs.config.yTarget, true);
	}
	
	@Override
	public void render(float delta) {
		if(stopped){
			stoppedTimer += delta;
			if(stoppedTimer > 1f){
				PlayerController controller = ConcertSmugglers.instance.controller;
				controller.update();
				if(controller.any){
					if(won){
						next();
					}
					else{
						restart();
					}
				}
				else if(controller.restart){
					restart();
				}
				else if(controller.exit){
					Gdx.app.exit();
				}
			}
		}
		clearActions(mapRenderer.players());
		clearActions(mapRenderer.guards());
		clearActions(mapRenderer.guests());
		mapRenderer.update();		
		if(!stopped){updatePlayer();}
		updateCamera();
		renderMap();
		renderUI();
	}

	private void next() {
		level++;
		if(level >= ConcertSmugglers.instance.assets.levels.length){
			ConcertSmugglers.instance.menu();
		}
		restart();
	}

	private void clearActions(List<Person> list) {
		for(Person p : list){
			p.gameAction = null;
		}
	}

	private void updateCamera() {
		Person focus = mapRenderer.focusedPerson;		
		if(focus != null){
			camera.position.set(focus.getX(), focus.getY(), 0);
		}
		camera.update();
	}

	private void renderUI() {
		androidControls.act();
		androidControls.draw();
		uiStage.act();
		uiStage.draw();
	}

	private void renderMap() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		backgroundRenderer.render();
		mapRenderer.renderBackground();
		mapRenderer.renderEntities();
		mapRenderer.renderForeground();
	}

	private void updatePlayer() {
		PlayerController c = cs.controller;
		c.clear();
		c.update();
		if(c.switchPlayer){
			mapRenderer.nextPlayer();
			updatePlayersTable();
		}
		Person focus = mapRenderer.focusedPerson;		
		if(ConcertSmugglers.instance.running && focus != null && !focus.isTransporting){
			if(c.restart){
				restart();
			}
			else if(c.exit){
				Gdx.app.exit();
				return;
			}
			else if(focus.gameAction != null && c.use){
				focus.gameAction.execute(focus);
			}
			else if(c.left && !c.right){
				focus.setDirection(LookingDirection.Left);
				focus.setState(PersonState.Walking);
			}
			else if(!c.left && c.right){
				focus.setDirection(LookingDirection.Right);
				focus.setState(PersonState.Walking);
			}
			else if(c.left && c.right){
				focus.setDirection(LookingDirection.None);
				focus.setState(PersonState.Idle);
			}
			else{
				focus.setState(PersonState.Idle);
			}
			
		}
		
		c.clear();
	}

	private void restart() {
		ConcertSmugglers.instance.running = false;
		mapRenderer.focusedPerson.setState(PersonState.Idle);
		
		sightBlocker.addAction(
			Actions.sequence(
				Actions.alpha(1f, 1f)
				, Actions.run(new Runnable() {
					
					@Override
					public void run() {
						//TODO: Replace with restart
						ConcertSmugglers.instance.game();
					}
				})
			)
		);
	}

	@Override
	public void resize(int width, int height) {
		mapRenderer.resize(width, height);
		androidControls.setViewport(ConcertSmugglers.instance.config.xTarget, ConcertSmugglers.instance.config.yTarget,true);
		uiStage.setViewport(ConcertSmugglers.instance.config.xTarget, ConcertSmugglers.instance.config.yTarget,true);
	}

	@Override
	public void show() {
		this.won = false;
		stopped = false;
		ConcertSmugglers.instance.running = false;
//		camera.setToOrtho(false, 500, 500);
		Assets assets = ConcertSmugglers.instance.assets;
		String levelFile = assets.levels[level];
		mapRenderer.loadMap(levelFile);
		Config config = ConcertSmugglers.instance.config;
		
		if(Gdx.app.getType() == ApplicationType.Android){
			createAndroidButtons(assets, config);
		}
		uiStage.clear();
		Table ui = new Table(assets.skin);
		ui.align(Align.top);
		ui.setSize(config.xTarget, config.yTarget);
		playersTable = new Table();
		
		updatePlayersTable();
		
		ui.add(playersTable);
		
		
		
		uiStage.addActor(ui);
		
		wonLabel = new Label("Yay!!! All got in", assets.skin, "title");
		lostLabel = new Label("You got caught", assets.skin, "title");
		lostKeyLabel  = new Label("Press any key to restart", assets.skin);
		wonKeyLabel  = new Label("Press any key to continue", assets.skin);
		setProperties(wonLabel);
		setProperties(lostLabel);
		setProperties(wonKeyLabel);
		wonKeyLabel.setY(wonKeyLabel.getY() - 30);
		setProperties(lostKeyLabel);
		lostKeyLabel.setY(lostKeyLabel.getY() - 30);
		wonKeyLabel.addAction(Actions.alpha(0));
		lostKeyLabel.addAction(Actions.alpha(0));
		wonKeyLabel.act(10);
		lostKeyLabel.act(10);
		wonLabel.addAction(Actions.alpha(0));
		wonLabel.act(10);
		lostLabel.addAction(Actions.alpha(0));
		lostLabel.act(10);
		uiStage.addActor(wonKeyLabel);
		uiStage.addActor(lostKeyLabel);
		uiStage.addActor(wonLabel);
		uiStage.addActor(lostLabel);
		
		sightBlocker = new Image(assets.blank);
		sightBlocker.setColor(Color.BLACK);
		sightBlocker.setSize(config.xTarget, config.yTarget);
		sightBlocker.addAction(
			Actions.sequence(
				Actions.fadeOut(1f)
				, Actions.run(new Runnable() {
					@Override
					public void run() {
						ConcertSmugglers.instance.running = true;						
					}
				})
			)
		);
		uiStage.addActor(sightBlocker);
	}
	
	public void won(){
		stop();
		fadeIn(wonLabel);
		fadeIn(wonKeyLabel);
		won = true;
	}
	
	public void lost(){
		stop();
		fadeIn(lostLabel);	
		fadeIn(lostKeyLabel);
		won = false;
	}
	
	public void stop(){
		ConcertSmugglers.instance.running = false;
		this.stopped = true;
		this.stoppedTimer = 0;
		List<Person> persons = ConcertSmugglers.instance.mapRenderer.players();
		for(Person person : persons){
			person.setState(PersonState.Idle);
		}
	}

	private void fadeIn(Label label) {
		label.addAction(
			Actions.sequence(
				Actions.parallel(				
					Actions.fadeIn(0.5f)
					, Actions.moveBy(0, 10, 0.5f)
				)
				, Actions.forever(
					Actions.sequence(
						Actions.moveBy(0, -20, 1f)						
						, Actions.moveBy(0, 20, 1f)
					)
				)
			)
		);
	}

	private void setProperties(Label label) {
		Config cfg = ConcertSmugglers.instance.config;
		label.setPosition(cfg.xTarget / 2 - label.getWidth() / 2, cfg.yTarget / 2 - label.getHeight() / 2);
	}

	private void createAndroidButtons(Assets assets, Config config) {
		androidControls = new Stage(cs.config.xTarget, cs.config.yTarget, true);
		Table table = new Table(assets.skin);
		table.setSize(config.xTarget, config.yTarget);
		table.setFillParent(true);
		
		Texture tex = assets.androidButtons;
		PlayerController controller = ConcertSmugglers.instance.controller;
		leftButton = new ImageButton(
				new Image(new TextureRegion(tex, 0, 0, 32, 32)).getDrawable()
				, new Image(new TextureRegion(tex, 0, 32, 32, 32)).getDrawable()
		);
		rightButton = new ImageButton(
				new Image(new TextureRegion(tex, 32, 0, 32, 32)).getDrawable()
				, new Image(new TextureRegion(tex, 32, 32, 32, 32)).getDrawable()
		);
		switchButton = new ImageButton(
				new Image(new TextureRegion(tex, 64, 0, 32, 32)).getDrawable()
				, new Image(new TextureRegion(tex, 64, 32, 32, 32)).getDrawable()
		);
		actionButton = new ImageButton(
				new Image(new TextureRegion(tex, 96, 0, 32, 32)).getDrawable()
				, new Image(new TextureRegion(tex, 96, 32, 32, 32)).getDrawable()
		);
		controller.leftButton.setButton(leftButton);
		controller.rightButton.setButton(rightButton);
		controller.switchButton.setButton(switchButton);
		controller.useButton.setButton(actionButton);
		table.row().align(Align.bottom);
		table.add(leftButton).expand();
		table.add(switchButton).expand();
		table.add(actionButton).expand();
		table.add(rightButton).expand();
		androidControls.addActor(table);
		Gdx.input.setInputProcessor(androidControls);
	}


	public void updatePlayersTable() {
		Assets assets = ConcertSmugglers.instance.assets;
		playersTable.clear();
		for(Person player : mapRenderer.players()){
			String skin = "player-unselected";
			if(player == mapRenderer.focusedPerson){
				skin = "player-selected";
			}
			Label label = new Label(player.name, assets.skin, skin);
			playersTable.row();
			playersTable.add(label);
		}
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
		// TODO Auto-generated method stub
		
	}

}
