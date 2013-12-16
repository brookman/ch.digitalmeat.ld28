package ch.digitalmeat.ld28.core.level;

import ch.digitalmeat.ld28.core.MapRenderer;
import ch.digitalmeat.ld28.core.person.Person;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Transport extends GameAction{

	public String key;
	public String targetKey;
	public Transport target;
	private MapRenderer map;
	public Vector2 pos;
	public Vector2 tmp;
	public boolean auto;
	public float speed = 64f;
	public Transport(String key, String targetKey, MapRenderer map, float x, float y){
		this.key = key;
		this.targetKey = targetKey;
		this.map = map;
		pos = new Vector2(x, y);
		tmp = new Vector2();
	}
	
	
	public boolean transport(final Person person) {
		if(target == null){
			target = map.transports.get(targetKey);
		}
		Gdx.app.log("", "Transporting " + person.name);
		if(target != null){
			person.isTransporting = true;
			tmp.set(target.pos);
			float distance = tmp.sub(pos).len();
			person.addAction(
				Actions.sequence(
					Actions.moveTo(target.pos.x, target.pos.y, distance / speed)
					, Actions.run(new Runnable() {
						@Override
						public void run() {
							person.isTransporting = false;
						}
					})
				)
			);
		}
		
		return true;
	}


	@Override
	public void execute(Person person) {
		transport(person);
	}

}
