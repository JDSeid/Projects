package jrails;

import java.util.Map;
import java.util.*;
import java.lang.reflect.*;

public class JRouter {
    ArrayList<Route> routes = new ArrayList<>();
    public void addRoute(String verb, String path, Class clazz, String method) {
        routes.add(new Route(verb, path, clazz, method));
    }

    // Returns "clazz#method" corresponding to verb+URN
    // Null if no such route
    public String getRoute(String verb, String path) {
        Route r = getRouteObject(verb, path);
        if(r != null){
            return r.toString();
        }
        return null;
    }
    private Route getRouteObject(String verb, String path){
        for (Route r : routes){
            if(r.verb.equals(verb) && r.path.equals(path)){
                return r;
            }
        }
        return null;
    }
    // Call the appropriate controller method and
    // return the result
    public Html route(String verb, String path, Map<String, String> params) {
        Route r = getRouteObject(verb, path);
        if(r == null){
            throw new UnsupportedOperationException();
        }
        try{
            Method m = r.clazz.getMethod(r.method, Map.class);
            Object instance = r.clazz.getConstructor().newInstance();
            return (Html) m.invoke(instance, params);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        

    }
}
