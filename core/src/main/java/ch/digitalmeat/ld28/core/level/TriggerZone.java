package ch.digitalmeat.ld28.core.level;

import java.util.List;

import ch.digitalmeat.ld28.core.MapRenderer;
import ch.digitalmeat.ld28.core.person.Person;

import com.badlogic.gdx.math.Rectangle;

public class TriggerZone {
	public Rectangle compareRect;
	public Rectangle zone;
	
	public boolean active;
	public boolean triggeredByPlayer;
	public boolean triggeredByGuard;
	public boolean triggeredByGuest;

	public TriggerListener listener;
	private String name;
	
	public TriggerZone(String name){
		this.name = name;
		zone = new Rectangle();
		compareRect = new Rectangle();
	}
	
	public void execute(MapRenderer renderer){
		if(listener == null){
			return;
		}
		boolean result = true;
		if(triggeredByPlayer){
			result = execute(renderer.players());
		}
		if(result && triggeredByGuard){
			result = execute(renderer.guards());
		}
		if(result && triggeredByGuest){
			result = execute(renderer.guards());
		}
	}
	
	public boolean execute(List<Person> persons){
		for(int index = persons.size() - 1; index >= 0; index--){
			Person person = persons.get(index);
			if(triggers(person)){
				if(!
						listener.trigger(person)
				){
					return false;
				}
			}
		}
		return true;
	}

	private boolean triggers(Person person) {
		compareRect.set(person.getX() + 4, person.getY(), person.getWidth(), person.getHeight() - 8);
		return compareRect.overlaps(zone);
	}
}
