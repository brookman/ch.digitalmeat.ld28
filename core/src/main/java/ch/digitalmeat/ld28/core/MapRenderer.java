package ch.digitalmeat.ld28.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ch.digitalmeat.ld28.core.level.Transport;
import ch.digitalmeat.ld28.core.level.TriggerListener;
import ch.digitalmeat.ld28.core.level.TriggerZone;
import ch.digitalmeat.ld28.core.level.Waypoint;
import ch.digitalmeat.ld28.core.person.Person;
import ch.digitalmeat.ld28.core.person.Person.LookingDirection;
import ch.digitalmeat.ld28.core.person.Person.PersonState;
import ch.digitalmeat.ld28.core.person.PersonConfig;
import ch.digitalmeat.ld28.core.person.PersonConfig.PersonType;
import ch.digitalmeat.ld28.core.person.PersonManager;
import ch.digitalmeat.ld28.core.person.PersonSheet;
import ch.digitalmeat.ld28.core.person.ai.Node;
import ch.digitalmeat.ld28.core.person.ai.PersonAi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MapRenderer {
	private OrthographicCamera camera;
	private TiledMap map;
	private TiledMapRenderer mapRenderer;

	private int[] backgroundLayers = null;
	private int[] foregroundLayers = null;
	private Stage stage;
	private List<Integer> layerList;
	private int[] entityLayers;
	private int mapWidth;
	private int mapHeight;
	private int tilePixelWidth;
	private int tilePixelHeight;
	private int mapPixelWidth;
	private int mapPixelHeight;
	private int focusIndex;
	private List<TriggerZone> triggers;
	public Person focusedPerson = null;
	private List<Person> playerPersons;
	private List<Person> guardPersons;
	private List<Person> guestPersons;
	private PersonManager personManager;
	private MapLayer collisionLayer; 
	public java.util.Map<String, Transport> transports;
	public java.util.Map<String, Waypoint> waypoints;
	public List<Person> players(){ return playerPersons; }
	public List<Person> guards(){ return guardPersons; }
	public List<Person> guests(){ return guestPersons; }
	
	public MapRenderer()
	{
		this.layerList = new ArrayList<Integer>();
		transports = new HashMap<String, Transport>();
		waypoints = new HashMap<String, Waypoint>();
		playerPersons = new ArrayList<Person>();
		guardPersons = new ArrayList<Person>();
		guestPersons = new ArrayList<Person>();
		personManager = new PersonManager();
		triggers = new ArrayList<TriggerZone>();
	}
	
	public void create(OrthographicCamera camera){
		this.camera = camera;
		int w = ConcertSmugglers.instance.config.xResolution;
		int h = ConcertSmugglers.instance.config.yResolution;
		this.stage = new Stage(w, h,true);
		
	}
	
	public void nextPlayer(){
		if(playerPersons.size() == 0){
			return;
		}
//		if(focusedPerson != null){
//			focusedPerson.setEffect(null);
//		}
		focusedPerson.setState(PersonState.Idle);
		focusIndex = (focusIndex + 1) % playerPersons.size();
		focusedPerson = playerPersons.get(focusIndex);
		
//		focusedPerson.setEffect(ConcertSmugglers.instance.assets.playerEffect);
	}
	
	public void loadMap(String file)
	{
		this.mapRenderer = null;
		focusedPerson = null;
		focusIndex = -1;
		playerPersons.clear();
		guardPersons.clear();
		guestPersons.clear();
		map = ConcertSmugglers.instance.assets.loadTilemap(file);
		MapProperties prop = map.getProperties();

		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tilePixelWidth = prop.get("tilewidth", Integer.class);
		tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		
		MapLayers layers = map.getLayers();
		backgroundLayers = getLayers(layers, "bg");
		foregroundLayers = getLayers(layers, "fg");
		entityLayers = getLayers(layers, "entity");
		int[] collision = getLayers(layers, "collision");
		
		if(collision != null && collision.length > 0){
			this.collisionLayer = layers.get(collision[0]);
		}
		//TODO: Load properties
		int w = ConcertSmugglers.instance.config.xResolution;
		int h = ConcertSmugglers.instance.config.yResolution;


		stage.clear();
		for(int layerIndex : entityLayers){
			MapLayer layer = layers.get(layerIndex);
			MapObjects objects = layer.getObjects();
			for(MapObject obj : objects){
				String type = obj.getProperties().get("type", String.class);
				PersonConfig config = null;
				List<Person> list = null;
				if("player_ticket".equals(type)){
					config = PersonConfig.PLAYER_WITH_TICKET();
					list = playerPersons;
				}
				else if("player_without_ticket".equals(type)){
					config = PersonConfig.PLAYER_WITHOUT_TICKET();
					list = playerPersons;
				}
				else if("guard".equals(type)){
					config = PersonConfig.NORMAL_GUARD();
					list = guardPersons;
				}
				else if("guest".equals(type)){
					config = PersonConfig.Guest();
					list = guestPersons;
				}
				else if("goal".equals(type)){
					addGoalTrigger(obj);
				}
				else if("transport".equals(type)){
					addTransport(obj);
				}
				else if("wp".equals(type)){
					addWaypoint(obj);
				}
				if(config != null){
					list.add(spawnPerson(config, obj, list == guestPersons));
				}
			}
		}
		for(Person p : guardPersons){
			stage.addActor(p);
		}
		for(Person p : playerPersons){
			stage.addActor(p);
		}
		if(playerPersons.size() > 0){
			focusedPerson = playerPersons.get(0);
		}
		stage.addActor(ConcertSmugglers.instance.textManager);
		camera.position.x = +mapPixelWidth / 2;
		camera.position.y = +mapPixelHeight / 2;
		mapRenderer = new OrthogonalTiledMapRenderer(map);
		resize(0, 0);
		Config config = ConcertSmugglers.instance.config;
		mapRenderer.setView(camera.combined, 0, 0, config.xTarget, config.yTarget);
		
		nextPlayer();
	}
	
	private void addWaypoint(MapObject obj) {
		RectangleMapObject robj = (RectangleMapObject)obj;
		Rectangle rect = robj.getRectangle();
		String key = obj.getProperties().get("key", String.class);
		Waypoint wp = new Waypoint(key, rect.x, rect.y);
		Gdx.app.log("AddWaypoint", key);
		waypoints.put(key, wp);
	}
	
	private void addTransport(MapObject obj) {
		RectangleMapObject robj = (RectangleMapObject)obj;
		Rectangle rect = robj.getRectangle();
		float x = rect.getX() ;
		float y = rect.getY() ;
		y = y - y % 32 + 2;
		MapProperties props = obj.getProperties();
		String key = props.get("key", String.class);
		String target = props.get("target", String.class);
		final Transport transport = new Transport(key, target, this, x, y);
		transport.text = props.get("text", String.class);
		transport.auto = "true".equals(props.get("auto", String.class));
		transports.put(key, transport);
		String speed = props.get("speed", String.class);
		if(speed != null){
			try{
			transport.speed = Float.parseFloat(speed);
			}
			catch(Exception ex){}
		}
		TriggerZone zone = new TriggerZone("Transport " + key);
		zone.active = true;
		zone.zone = rect;
		zone.triggeredByPlayer = true;
		zone.triggeredByGuard = true;
		zone.triggeredByGuest = true;
		zone.listener = new TriggerListener() {
		
			@Override
			public boolean trigger(Person person) {
				if(!person.isTransporting){
					if(transport.auto){
						transport.transport(person);
					}
					else{
						transportEnabled(transport, person);
					}
				}
				return true;
			}
		};
		triggers.add(zone);
	}
	
	protected void transportEnabled(Transport transport, Person person) {
		person.gameAction = transport;
	}
	
	private void addGoalTrigger(MapObject obj) {
		TriggerZone zone = new TriggerZone("Goal");
		RectangleMapObject rectangle = (RectangleMapObject) obj;		
		zone.zone = rectangle.getRectangle();
		zone.triggeredByPlayer = true;
		zone.active = true;
		zone.listener = new TriggerListener() {
			
			@Override
			public boolean trigger(Person person) {
				playerArrived(person);
				return true;
			}

		};
		triggers.add(zone);
	}
	protected void playerArrived(Person person) {
		if(person.config.hasTicket){
			Gdx.app.log("", "Person already has a ticket");
			return;
		}
		Gdx.app.log("", "Arrived");
		playerPersons.remove(person);
		guestPersons.add(person);
		person.setEffect(null);
		person.setAi(PersonAi.guestAi);
		ConcertSmugglers.instance.inGameScreen.updatePlayersTable();
		if(playerPersons.size() == 1){
			won();
		}
		else{
			nextPlayer();			
		}
	}
	
	private void won() {
		Gdx.app.log("", "Won Game");
		ConcertSmugglers.instance.inGameScreen.won();
	}
	private Person spawnPerson(PersonConfig config, MapObject obj, boolean addToStage) {
		if(!(obj instanceof EllipseMapObject)){
			return null;
		}
		EllipseMapObject eObj = (EllipseMapObject) obj;
		Gdx.app.log("", "Spawning " + config.type);
		PersonSheet[] sheets = ConcertSmugglers.instance.assets.sheets;
		Person person = new Person();
		Node ai = null;
		if(config.type == PersonType.Player){
			ai = new Node();
			person.setEffect(ConcertSmugglers.instance.assets.playerEffect());
		}
		else if(config.type == PersonType.Guard){
			ai = PersonAi.guardAi;
			person.setEffect(ConcertSmugglers.instance.assets.guardEffect());
		}
		else{
			ai = PersonAi.guestAi;
		}
		person.name = ConcertSmugglers.instance.assets.randomName();
		person.init(getRandomPerson(sheets), config, ai);
		person.setSize(16f, 16f);
		String roamTargets = obj.getProperties().get("roam", String.class);
		if(roamTargets != null){
			String[] targets = roamTargets.split(";");
			for(String target : targets){
				Gdx.app.log("SpawnPerson", "Adding Waypoint: " + target);
				person.addWaypoint(target);
			}
		}
		String roamDelay = obj.getProperties().get("roamDelay", String.class);
		try{
			person.roamDelay = Float.parseFloat(roamDelay);
		}
		catch(Exception ex){
			
		}
		float y = eObj.getEllipse().y;
		y -= y % 32 + -2;
		int dir = ConcertSmugglers.instance.random.nextInt(3);
		if(dir == 1){
			person.setDirection(LookingDirection.Left);
		}
		else if(dir == 2){
			person.setDirection(LookingDirection.Right);
		}
		else{
			person.setDirection(LookingDirection.None);
		}

		person.setPosition(eObj.getEllipse().x, y);
		stage.addActor(person);
		return person;
	}

	private PersonSheet getRandomPerson(PersonSheet[] sheets) {
		PersonSheet sheet = new PersonSheet();
		Random r = ConcertSmugglers.instance.random;
		int len = 3;
		int offset = 0;
		if(r.nextBoolean()){
			offset = 4;
			len = 4;
		}
		else{
			if(r.nextInt(5) == 0){
				offset = 3;
				len = 1;
			}
		}
		
		PersonSheet legs = sheets[r.nextInt(len) + offset];
		PersonSheet torso = sheets[r.nextInt(len) + offset];
		PersonSheet face = sheets[r.nextInt(len) + offset];
		PersonSheet hair = sheets[r.nextInt(len) + offset];

		assign(sheet, hair, PersonSheet.SHEET_HAIR);
		assign(sheet, face, PersonSheet.SHEET_HEAD);
		assign(sheet, torso, PersonSheet.SHEET_TORSO);
		assign(sheet, legs, PersonSheet.SHEET_LEGS);
		
		
		return sheet;
	}

	private void assign(PersonSheet sheet, PersonSheet hair, int sheetNr) {
		sheet.front[sheetNr] = hair.front[sheetNr];
		sheet.side_walk[sheetNr] = hair.side_walk[sheetNr];
		sheet.side_idle[sheetNr] = hair.side_idle[sheetNr];
	}

	private int[] getLayers(MapLayers layers, String name){
		layerList.clear();
		int count = layers.getCount();
		for(int index = 0; index < count; index ++){
			MapLayer layer = layers.get(index);
			String type = layer.getProperties().get("type", String.class);
			//Gdx.app.log("", type);
			if(name.equals(type)){
				layerList.add(index);
			}
		}
		if(layerList.size() == 0){
			return null;
		}
		int[] returnValue = new int[layerList.size()];
		for(int index = 0; index < layerList.size(); index++){
			returnValue[index] = layerList.get(index);
		}
		return returnValue;
	}
	
	public void render(){
		if(mapRenderer != null){
			mapRenderer.setView(camera);
			mapRenderer.render();
		}
	}

	public void render(int[] layers){
		if(mapRenderer != null && layers != null){
			mapRenderer.setView(camera);
			mapRenderer.render(layers);
		}
	}
	
	public void renderBackground(){
		render(backgroundLayers);
	}
	
	public void renderForeground(){
		render(foregroundLayers);
	}
	
	public void update(){
		personManager.handleMovement(Gdx.graphics.getDeltaTime(), stage.getActors(), collisionLayer);
		stage.act();
		for(TriggerZone trigger : triggers){
			trigger.execute(this);
		}
	}
	
	public void renderEntities(){
		stage.setCamera(camera);
		stage.draw();

	}

	public void resize(int width, int height) {
		Config config = ConcertSmugglers.instance.config;
		stage.setViewport(config.xTarget, config.yTarget, true, camera.position.x, camera.position.y, config.xResolution, config.yResolution);
		stage.setCamera(camera);
	}
}
