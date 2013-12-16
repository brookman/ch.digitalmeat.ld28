package ch.digitalmeat.ld28.core.person;


import ch.digitalmeat.ld28.core.person.Person.LookingDirection;
import ch.digitalmeat.ld28.core.person.Person.PersonState;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public class PersonManager {
	public void handleMovement(float delta, Array<Actor> actors, MapLayer layer)
	{
		for(Actor actor : actors){
			if(actor instanceof Person){
				handlePerson(delta, layer, (Person)actor);
			}
		}
	}

	private void handlePerson(float delta, MapLayer layer, Person p) {
		if(p.isTransporting){
			return;
		}
		if(p.getState() == PersonState.Walking && p.getDirection() != LookingDirection.None){
			float x = p.getX();
			float moveBy = delta * (p.running ? 128f : 64f);
			if(p.getDirection() == LookingDirection.Left){
				moveBy *= -1;
			}
			float newX = x + moveBy;
			TiledMapTileLayer tl = null;
			if(layer != null){
				int px = tp(p.getX());
				int py = tp(p.getY());
				float offset = p.getDirection() == LookingDirection.Right ? + 5 : - 5;
				int pnx = tp(newX + offset);

				if(pnx != px){
					tl = (TiledMapTileLayer) layer;
					Cell cell = tl.getCell(px, py);
					if(cell != null){
						String collision = cell.getTile().getProperties().get("collision", String.class);
						if(collision != null){
							if(p.getDirection() == LookingDirection.Left && "left".equals(collision)){
								newX = x;
							}
							else if(p.getDirection() == LookingDirection.Right && "right".equals(collision)){
								newX = x;
							}
						}
					}
				}
				
				
			}
			p.setX(newX);
		}
	}
	
	private int tp(float val){
		return (int)((val + 8) / 32f);
	}
}
