package ch.digitalmeat.ld28.core.person.ai;

import java.util.List;
import java.util.Random;

import ch.digitalmeat.ld28.core.ConcertSmugglers;
import ch.digitalmeat.ld28.core.MapRenderer;
import ch.digitalmeat.ld28.core.person.Person;
import ch.digitalmeat.ld28.core.person.Person.LookingDirection;

public class DetectPlayer extends Node {
	
	private Random random;
	
	public final static String[] GOT_TICKET_MESSAGES = new String[]{
		"Ok"
		, "You're fine"
		, "Next please"
		, "Ticket, ahh ok"
		, "Come in"
	};
	public final static String[] GOT_NO_TICKET_MESSAGES = new String[]{
		"Busted!"
		, "Stop it. Creepy Bastard"
		, "You're not getting in!"
	};
	
	public DetectPlayer(){
		this.random = ConcertSmugglers.instance.random;
	}
	
	
	
	@Override
	public boolean onExecute(Person source) {
		if(!ConcertSmugglers.instance.running){
			return false;
		}
		MapRenderer map = ConcertSmugglers.instance.mapRenderer;
		List<Person> persons = map.players();
		for (Person target : persons) {
			boolean check = true;
			if (source.detectedPlayers.contains(target)) {
				if(target.lastChecked > 2f){
					source.detectedPlayers.remove(target);
				}
				else{
					check = false;
				}
			}
			if (check && isInSight(source, target) && isNearEnough(source, target)) {
				source.detectedPlayers.add(target);
				target.lastChecked = 0;
				if(target.config.hasTicket){
					source.say(GOT_TICKET_MESSAGES[random.nextInt(GOT_TICKET_MESSAGES.length)], "talk-gold");
				}
				else{
					source.say(GOT_NO_TICKET_MESSAGES[random.nextInt(GOT_NO_TICKET_MESSAGES.length)], "talk-red");
					ConcertSmugglers.instance.inGameScreen.lost();
				}
				return false;
			}
			
		}
		return true;
	}

	private boolean isNearEnough(Person source, Person target) {
		float vd = Math.abs(source.getY() - target.getY());
		if (vd > 8) {
			return false;
		}
		float hd = Math.abs(source.getX() - target.getX());
		return hd < 20;
	}

	private boolean isInSight(Person source, Person target) {
		boolean isLeft = source.getX() > target.getX();
		LookingDirection dir = source.getDirection();
		return (isLeft && dir == LookingDirection.Left)
				|| (!isLeft && dir == LookingDirection.Right);
	}
}
