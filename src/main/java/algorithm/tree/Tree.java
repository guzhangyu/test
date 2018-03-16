package algorithm.tree;

public interface Tree<Node, Key>{

    Node find(Key key);

    Node insert(Key key);

    void remove(Key key);
}
