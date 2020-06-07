package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Position;

public class EmptyNode extends Node {

	public EmptyNode(Position position) {
		super(position, position);
	}

}
