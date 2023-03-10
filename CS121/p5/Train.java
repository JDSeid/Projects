import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Train extends Entity {
  private Line line;
  private static HashMap<String, Train> cache = new HashMap<>();
  private Station station = null;
  private List<Passenger> passengers = new ArrayList<>();
  private List<Station> stations = new ArrayList<>();
  private boolean forward = true;

  private Train(String name) { super(name); }

  public static Train make(String name) {
    if(cache.containsKey(name)){
      return cache.get(name);
    }
    Train t = new Train(name);
    cache.put(name, t);
    return t;
  }
  public static void reset(){
    for(String s : cache.keySet()){
      Train t = cache.get(s);
      t.setStation(null);
      t.passengers = new ArrayList<>();
      t.stations = new ArrayList<>();
      t.forward = true;
    }

  }
  

  public void setLine(Line line){
    this.line = line;
  }
  
  public Line getLine(){
    return line;
  }
  

  public void setStations(List<Station> stations){
    this.stations = stations;
    line.setCurrentStation(station);
  }

  public List<Station> getStations(){
    return stations;
  }


  public void setStation(Station station){
    this.station = station;
    for(Passenger p: passengers){
      p.setStation(station);
    }
  }

  public Station getStation(){
    return station;
  }

  public void addPassenger(Passenger p){
    p.setOnTrain(true);
    passengers.add(p);
  }
  
  public boolean removePassenger(Passenger p){
    p.setOnTrain(false);
    return passengers.remove(p);
  }

  public boolean containsPassenger(Passenger p){
    return passengers.contains(p);
  }

  public List<Passenger> getPassengers(){
    return passengers;
  }


  // public void update(MBTA mbta, Log log){
  //   getStation().updateTrain(mbta, log);
  // }

  public boolean isFoward(){
    return forward;
  }

  public void setFoward(boolean dir){
    forward = dir;
  }

  // public void deboardPassenger(Passenger p, Lock l, Condition c, Log log){
  //   l.lock();
  //   Station nextStop = p.getNextLeg();
  //   while(station != nextStop){
  //     try{ c.await(); }

  //     catch (Exception e) { 
  //       e.printStackTrace();
  //       throw new RuntimeException(e.getMessage()); } }

  //     log.passenger_deboards(p, this, station);
  //     removePassenger(p);
  //     station.addPassenger(p);

  //     c.signalAll();
  //     l.unlock();
  // }



  public static void resetCache(){
    cache.clear();
  }

}
