package ch.digitalmeat.ld28.core.person.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.digitalmeat.ld28.core.ConcertSmugglers;
import ch.digitalmeat.ld28.core.MapRenderer;
import ch.digitalmeat.ld28.core.level.Waypoint;
import ch.digitalmeat.ld28.core.person.Person;
import ch.digitalmeat.ld28.core.person.Person.LookingDirection;
import ch.digitalmeat.ld28.core.person.Person.PersonState;

public class RoamWaypoints extends Node{
	
	private Random random;
	private List<String> candidates;

	public RoamWaypoints(){
		this.random = ConcertSmugglers.instance.random;
		candidates = new ArrayList<String>();
	}
	
	@Override
	public boolean onExecute(Person person){
		if(person.waypoints == null ||  person.waypoints.size() == 0){
			return true;
		}
		int size = person.waypoints.size();
		if(size == 1){
			return goTo(person, person.waypoints.get(0));
		}
		candidates.clear();
		candidates.addAll(person.waypoints);
		while(candidates.size() > 1){
			
			String candidate = candidates.get(random.nextInt(candidates.size()));
			if(!candidate.equals(person.lastWaypoint)){
				return goTo(person, candidate);
			}
		}
		return goTo(person, person.waypoints.get(0));
	}

	private boolean goTo(Person person, String key) {
		MapRenderer renderer = ConcertSmugglers.instance.mapRenderer;
		Waypoint wp = renderer.waypoints.get(key);
		if(wp == null){
			return true;
		}
		person.actionRunning = true;
		person.targetingWaypoint = true;
		if(wp.pos.x < person.getX() + person.getWidth()/2){
			person.setDirection(LookingDirection.Left);
		}
		else{
			person.setDirection(LookingDirection.Right);
		}
		person.waypoint = wp;
		person.lastWaypoint = key;
		person.setState(PersonState.Walking);
		return false;
	}
}
