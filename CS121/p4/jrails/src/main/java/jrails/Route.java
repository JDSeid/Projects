package jrails;
public class Route {
    String verb;
    String path;
    Class clazz;
    String method;

    public Route(String verb, String path, Class clazz, String method){
        this.verb = verb;
        this.path = path;
        this.clazz = clazz;
        this.method = method;
    }

    public String toString(){
        return clazz.getCanonicalName() + "#" + method;
    }

    public boolean equals(Route o){
        return verb == o.verb && path == o.path && clazz == o.clazz && method == o.method;
    }
}
