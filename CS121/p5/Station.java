import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Station extends Entity {


  Train t;
  Lock l = new ReentrantLock();
  Condition tc = l.newCondition();

  //Lock pl = new ReentrantLock(true);
  Condition pc = l.newCondition();

  Queue<Event> eventQueue = new PriorityQueue<>();
  public static MBTA sim;

  public static HashMap<String, Station> cache = new HashMap<>();
  private Train train;
  private List<Passenger> passengers = new ArrayList<>();
  private Station(String name) { super(name); }



  public static Station make(String name) {
    if(cache.containsKey(name)){
      return cache.get(name);
    }
    Station s = new Station(name);
    cache.put(name, s);
    return s;
  }

  public static void reset(){
    for(String s : cache.keySet()){
      Station st = cache.get(s);
      st.setTrain(null);
      st.passengers = new ArrayList<>();
    }
    //cache.clear();
  }


    


 
  public void lock(){
    l.lock();
  }

  public void unlock(){
    l.unlock();
  }


  public void signalPassengers(){
    pc.signalAll();
  }
  
  public void signalTrains(){
    tc.signalAll();
  }

  public void await(){
    try{
      pc.await();
    }
    catch (Exception e){
      throw new RuntimeException(e.getMessage());
    }
    
  }

  public void setTrain(Train t){
    if(t != null){
      t.setStation(this);
    }
    this.t = t;
  }

  public Train getTrain(){
    return t;
  }

  public void addPassenger(Passenger p){
    passengers.add(p);
    p.setStation(this);
    p.getJourney().setLeg(this);
    p.getJourney().checkIfDone();
  }

  public void removePassenger(Passenger p){
    passengers.remove(p);
  }

  public List<Passenger> getPassengers(){
    return passengers;
  }


  public static void resetCache(){
    cache.clear();
  }




  
}
