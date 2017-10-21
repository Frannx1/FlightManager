package Model.Graph.GraphStructures;

public class Arc<T,V> {

    private Node<T,V> origin;
    private Node<T,V> target;
    private V data;

    public Arc(Node<T,V> origin, Node<T,V> target, V data) {
        this.origin = origin;
        this.target = target;
        this.data = data;
    }

    public Node<T,V> getOrigin() {
        return origin;
    }

    public Node<T,V> getTarget() {
        return target;
    }

    public V getData() {
        return data;
    }

}
