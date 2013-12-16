package ch.digitalmeat.ld28.core.person;

import java.util.Random;

import ch.digitalmeat.ld28.core.ConcertSmugglers;

import com.badlogic.gdx.graphics.Color;

public class PersonConfig {
	public enum PersonType{
		Player
		, Guard
		, Guest
	}
	
	
	public static float rtf(int value){
		return 1f / 255 * value;
	}
	
	
	public static Color col(int r, int g, int b){
		return col(r, g, b, 255);
	}
	public static Color col(int r, int g, int b, int a){
		return new Color(rtf(r), rtf(g), rtf(b), rtf(a));
	}
	
	public final static Color[] MAIN_COLORS = new Color[]{
		col(255, 0, 255)
		, col(0, 255, 255)
		, col(255, 255, 0)
	};
	
	
	
	public static Color[] SECONDARY_COLORS = new Color[]{
		col(255, 0, 0)
		, col(0, 0, 255)
		, col(128, 0, 255)
		, col(0, 255, 0)
	};
	
	public static Color[] HAIR_COLORS = new Color[]{
		col(0, 0, 0)
	};
	public static Color[] FACE_COLORS = new Color[]{
		col(231, 158, 109)
		, col(229, 194, 152)
		, col(227, 194, 124)
		, col(221, 168, 160)
		, col(203, 132, 66)
		, col(189, 114, 60)
		, col(173, 100, 82)
		, col(48, 46, 46)
	};
	
	public PersonType type;
	public boolean hasTicket;
	
	public Color mainColor;
	public Color secondaryColor;
	public Color hairColor;
	public Color faceColor;
	private static int spawnIndex = 0;
	public PersonConfig(PersonType type, boolean hasTicket, Color main, Color secondary, Color hair, Color face){
		this.type = type;
		this.hasTicket = hasTicket;
		this.mainColor = main;
		this.secondaryColor = secondary;
		this.faceColor = face;
		this.hairColor = hair;
	}
	
	public final static PersonConfig PLAYER_WITH_TICKET(){
		return new PersonConfig(
			PersonType.Player
			, true
			, Color.YELLOW
			, col(SECONDARY_COLORS)
			, col(HAIR_COLORS)
			, col(FACE_COLORS)
		);
}
		
	public final static PersonConfig PLAYER_WITHOUT_TICKET() {
		return new PersonConfig(
			PersonType.Player
			, false
			, Color.GRAY
			, col(SECONDARY_COLORS)
			, col(HAIR_COLORS)
			, col(FACE_COLORS)
		);
	}
	
	public final static PersonConfig NORMAL_GUARD(){
		return new PersonConfig(
			PersonType.Guard
			, true
			, Color.BLUE
			, Color.BLUE
			, col(HAIR_COLORS)
			, col(FACE_COLORS)
		);
	}
	
	public static Color col(Color[] colors) {
		Random random = ConcertSmugglers.instance.random;
		return colors[random.nextInt(colors.length)];
	}
	
	public static PersonConfig Guest(){
		return new PersonConfig(
			PersonType.Guest
			, true
			, col(MAIN_COLORS)
			, col(SECONDARY_COLORS)
			, col(HAIR_COLORS)
			, col(FACE_COLORS)
		);
	}
}
