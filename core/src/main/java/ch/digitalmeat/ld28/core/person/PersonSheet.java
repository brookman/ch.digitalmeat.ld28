package ch.digitalmeat.ld28.core.person;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class PersonSheet {
	public static int SHEET_LEGS = 0;
	public static int SHEET_TORSO = 1;
	public static int SHEET_HEAD = 2;
	public static int SHEET_HAIR = 3;
	
	public TextureRegion[][] front;
	public TextureRegion[][] side_idle;
	public TextureRegion[][] side_walk;
	
	public PersonSheet(){
		front = new TextureRegion[4][];
		side_idle = new TextureRegion[4][];
		side_walk = new TextureRegion[4][];
	}
}
