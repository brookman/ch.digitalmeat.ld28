package ch.digitalmeat.ld28.core.person.ai;

public class GuardData {
	public float timeSinceCheckedPlayer = 0f;
	
	public void update(float delta){
		timeSinceCheckedPlayer += delta;
	}
}
