package ch.digitalmeat.ld28.core.level;

import ch.digitalmeat.ld28.core.person.Person;

public abstract class GameAction {
	public String text;
	
	public Runnable action;
	
	public abstract void execute(Person person);
}
