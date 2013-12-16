package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.Gdx;

public class TouchTrap extends Trap {

	@Override
	protected boolean isPressed() {
		return Gdx.input.isTouched();
	}

}
