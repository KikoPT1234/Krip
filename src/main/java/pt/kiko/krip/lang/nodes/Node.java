package pt.kiko.krip.lang.nodes;

import pt.kiko.krip.lang.Position;

abstract public class Node {

    public Position startPosition;
    public Position endPosition;

    public Node(Position startPosition, Position endPosition) {
        this.startPosition = startPosition.copy();
        this.endPosition = endPosition.copy();
    }

    public Node(Position startPosition) {
        this.startPosition = startPosition.copy();
        this.endPosition = startPosition.copy();
    }

    @Override
    public String toString() {
        return "Node {\n" +
                "startPosition: " + startPosition + "\n" +
                "endPosition: " + endPosition + "\n" +
                '}';
    }
}
