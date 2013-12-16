package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyTrap extends Trap {
	private int key;
	
	public KeyTrap(int key){
		this.key = key;
	}
	
	@Override
	protected boolean isPressed() {
		return Gdx.input.isKeyPressed(key);
	}
}
