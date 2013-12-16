package ch.digitalmeat.ld28.core;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ButtonTrap extends Trap{
	private Button button;

	public ButtonTrap(){}
	public ButtonTrap(Button button){
		this.button = button;
	}
	
	public void setButton(Button button){
		this.button = button;
	}
	
	@Override
	protected boolean isPressed() {
		if(button == null){
			return false;
		}
		return button.isPressed();
	}

}
