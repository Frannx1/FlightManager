package Model.Graph.GraphStructures;

public class Arc<T,V> {

    private Node<T,V> origin;
    private Node<T,V> target;
    private V data;

    public Arc(V data, Node<T,V> origin, Node<T,V> target) {
        this.origin = origin;
        this.target = target;
        this.data = data;
        origin.addOutArc(this);
        target.addInArc(this);
    }

    public Arc(V data, T origin, T target) {
        this.origin = new Node<>(origin);
        this.target = new Node<>(target);
        this.data = data;
        this.origin.addOutArc(this);
        this.target.addInArc(this);
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
        Arc arc = (Arc) obj;
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

