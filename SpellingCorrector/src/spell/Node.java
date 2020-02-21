package spell;

public class Node implements INode {


    public int count;
    public Node[] nodesArray;

    public Node()
    {
        count = 0;
        nodesArray = new Node[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    public void incrementCount(){
        count++;
    }
}