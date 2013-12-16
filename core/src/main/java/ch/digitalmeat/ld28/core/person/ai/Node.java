package ch.digitalmeat.ld28.core.person.ai;

import java.util.ArrayList;
import java.util.List;

import ch.digitalmeat.ld28.core.person.Person;

public class Node {
	
	private List<Node> children;
	
	public Node(){
		children = new ArrayList<Node>();
	}
	
	protected boolean onExecute(Person person){
		return true;
	}
	
	public boolean execute(Person person)
	{
		if(!onExecute(person)){
			return false;
		}
		for(int x = 0; x < children.size(); x++){
			if(!children.get(x).execute(person)){
				return false;
			}
		}
		return true;
	}
	
	public void add(Node node){
		children.add(node);
	}
}
