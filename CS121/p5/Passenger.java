import java.util.*;

public class Passenger extends Entity {
  private static HashMap<String, Passenger> cache = new HashMap<>();
  private Station station;
  private Journey journey;
  boolean onTrain;
  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if(cache.containsKey(name)){
      return cache.get(name);
    }
    Passenger p = new Passenger(name);
    p.onTrain = false;
    cache.put(name, p);
    
    return p;
  }

    public static void reset(){
      for(String s : cache.keySet()){
        Passenger p = cache.get(s);
        p.station = null;
        p.onTrain = false;
        p.journey = null;
      }

      //cache.clear();

      
    }

  public void setJourney(Journey journey){
    this.journey = journey;
  }

  public Journey getJourney(){
    return journey;
  }


  public void setStation(Station station){
    this.station = station;
  }

  public Station getStation(){
    return station;
  }

  public Station getNextLeg(){
    return journey.getNextLeg();
  }

  public void setOnTrain(boolean onTrain){
    this.onTrain = onTrain;
  }

  public boolean onTrain(){
    return onTrain;
  }


  public static void resetCache(){
    cache.clear();
  }

  
}
