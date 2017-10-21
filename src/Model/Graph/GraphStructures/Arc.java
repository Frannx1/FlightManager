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

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;
        if(!getClass().equals(obj.getClass())) return false;
        Arc<T,V> arc = (Arc) obj;
        return this.data.equals(arc.getData());
    }

    @Override
    public int hashCode(){
        return this.data.hashCode();
    }

    @Override
    public String toString(){
        return this.data.toString();
    }
}
