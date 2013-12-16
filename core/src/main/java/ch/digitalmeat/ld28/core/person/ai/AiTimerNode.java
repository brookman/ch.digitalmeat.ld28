package ch.digitalmeat.ld28.core.person.ai;

import com.badlogic.gdx.Gdx;

import ch.digitalmeat.ld28.core.person.Person;

public class AiTimerNode extends Node {
	@Override
	public boolean onExecute(Person person) {
		if (person.aiUpdateTimer > person.aiUpdateTime) {
			Gdx.app.log("", "Update AI");
			person.aiUpdateTimer -= person.aiUpdateTime;
			return true;
		}
		return false;
	}
}
