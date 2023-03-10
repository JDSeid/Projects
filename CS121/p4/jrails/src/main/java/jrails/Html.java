package jrails;
import java.util.*;

public class Html {
    String tag;
    Object child;
    ArrayList<String> attributes = new ArrayList<>();

    public String toString() {
        if(tag == "br"){
            return "<br/>";
        }
        if (tag == "input"){
            String res = "<" + tag;
            for(String s : attributes){
                res += " " + s;
            }
            return res + "/>";
        }
        if(tag == null){
            return child.toString();
        }
        if(attributes.isEmpty()){
            return "<" + tag + ">" + child.toString() + "</" + tag + ">";
        }
        else{
            String res = "<" + tag;
            for(String s : attributes){
                res += " " + s;
            }
           return res + ">" + child.toString() + "</" + tag + ">";
        }
    }

    public  Html(String tag, Object child){
        this.tag = tag;
        this.child = child;
    }

    public Html() {
        this.tag = null;
        this.child = "";
     }

    public void addAttribute(String attributeName, String attribute){
        attributes.add(attributeName + "=\"" + attribute + "\"");
        //this.attributes.put(attributeName, attribute);
    }


    public Html seq(Html h) {
        return new Html(null, this.toString() + h.toString());
    }

    public Html br() {
        return seq(new Html("br", ""));
    }

    public Html t(Object o) {
        return seq(new Html (null, o.toString()));
    }

    public Html p(Html child) {
        return seq(new Html("p", child));
    }

    public Html div(Html child) {
        return seq(new Html("div", child));
    }

    public Html strong(Html child) {
        return seq(new Html("strong", child));
    }

    public Html h1(Html child) {
        return seq(new Html("h1", child));
    }

    public Html tr(Html child) {
        return seq(new Html("tr", child));
    }

    public Html th(Html child) {
        return seq(new Html("th", child));
    }

    public Html td(Html child) {
        return seq(new Html("td", child));
    }

    public Html table(Html child) {
        return seq(new Html("table", child));
    }

    public Html thead(Html child) {
        return seq(new Html("thead", child));
    }

    public Html tbody(Html child) {
        return seq(new Html("tbody", child));
    }

    public Html textarea(String name, Html child) {
        Html h = new Html("textarea", child);
        h.addAttribute("name", name);
        return seq(h);
    }

    public Html link_to(String text, String url) {
        Html h = new Html("a", text);
        h.addAttribute("href", url);
        return seq(h);
    }

    public Html form(String action, Html child) {
        Html h = new Html("form", child);
        h.addAttribute("action", action);
        h.addAttribute("accept-charset", "UTF-8");
        h.addAttribute("method", "post");
        return seq(h);
    }

    public Html submit(String value) {
        Html h = new Html("input", "");
        h.addAttribute("type", "submit");
        h.addAttribute("value", value);
        return seq(h);
    }
}