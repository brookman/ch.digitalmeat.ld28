package ch.digitalmeat.ld28.core;

public class Config {
	public int xResolution;
	public int yResolution;
	
	public final int xTarget = 240;
	public final int yTarget = 200;
	
	public Config(int xResolution, int yResolution){
		this.xResolution = xResolution;
		this.yResolution = yResolution;
	}
	
	public void set(int xResolution, int yResolution){
		this.xResolution = xResolution;
		this.yResolution = yResolution;
	}
}
