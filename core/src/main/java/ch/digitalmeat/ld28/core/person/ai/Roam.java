package ch.digitalmeat.ld28.core.person.ai;

import java.util.Random;

import com.badlogic.gdx.Gdx;

import ch.digitalmeat.ld28.core.ConcertSmugglers;
import ch.digitalmeat.ld28.core.level.Transport;
import ch.digitalmeat.ld28.core.person.Person;
import ch.digitalmeat.ld28.core.person.Person.LookingDirection;
import ch.digitalmeat.ld28.core.person.Person.PersonState;

public class Roam extends Node {
	private Random random;

	public Roam(){
		this.random = ConcertSmugglers.instance.random;
	}
	
	@Override
	public boolean onExecute(Person person){
		if(person.gameAction instanceof Transport){
			Gdx.app.log("", "Transport Action Found");
			int tpick = random.nextInt(4);
			if(tpick >= 0){
				Transport transport = (Transport) person.gameAction;
				transport.transport(person);
				return true;
			}
		}
		
		int pick = random.nextInt(4);
		
		
		LookingDirection dir = LookingDirection.None;
		if(pick == 1){
			dir = LookingDirection.Left;
		}
		else if(pick == 2){
			dir = LookingDirection.Right;
		}
		person.setDirection(dir);
		if(dir == LookingDirection.None){
			person.setState(PersonState.Idle);
		}
		else{
			person.setState(random.nextBoolean() ? PersonState.Idle : PersonState.Walking);
		}
		person.actionTimer = 0f;
		person.actionTime = random.nextFloat() * 1 + 0.3f;
		person.actionRunning = true;
		return true;
	}
}
