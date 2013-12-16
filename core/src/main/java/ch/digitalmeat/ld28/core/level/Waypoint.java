package ch.digitalmeat.ld28.core.level;

import com.badlogic.gdx.math.Vector2;

public class Waypoint {
	public String key;
	public Vector2 pos;
	
	public Waypoint(String key, float x, float y){
		this.key = key;
		this.pos = new Vector2(x, y - y % 32 + 2);
	}	
}
