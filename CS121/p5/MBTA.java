import java.util.*;
import java.io.*;
import com.google.gson.*;
import com.google.gson.reflect.*;

public class MBTA {

  public List<Line> lines = new ArrayList<>();
  private List<Journey> journeys = new ArrayList<>();
  //private HashMap<Passenger, List<Station>> journeys = new HashMap<>();
  private Log log;
  
  private int passengersDone = 0;

  private static Gson gson = new Gson();

  // Creates an initially empty simulation
  public MBTA() { }


  public boolean simDone(){
    return passengersDone == journeys.size();
  }

  public void passengerDone(){    
    passengersDone++;
  }
  //Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    List<Station> s = new ArrayList<>();
    for(String station : stations){
      s.add(Station.make(station));
    }
    Train t = Train.make(name);
    Line line = new Line(t, s, this, log);
    t.setLine(line);
    lines.add(line);
    t.setStation(s.get(0));
    s.get(0).setTrain(t);
    t.setStations(s);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    List<Station> s = new ArrayList<>();
    for(String station : stations){
      s.add(Station.make(station));
    }
    Passenger p = Passenger.make(name);
    journeys.add(new Journey(p, s, this, log));
    s.get(0).addPassenger(p);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    for(Journey j : journeys){
      if(j.getPassenger().getStation() != j.getStations().get(0)){
        throw new RuntimeException("Invalid passenger start position");
      }
    }
    for(Line line : lines){
      if(line.getTrain().getStation() != line.getCurrentStation()){
        throw new RuntimeException("Invalid train start position");
      }
    }
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
      for(Journey j : journeys){
        if(j.getPassenger().getStation() != j.getDestination()){
          throw new RuntimeException("Not all passengers ended up at their destination");
        }
      }
    }


  // reset to an empty simulation
  public void reset() {
    Passenger.reset();
    Train.reset();
    Station.reset();
    lines.clear();
    journeys.clear();
    passengersDone = 0;
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    reset();
    JsonConfig JS = parseJson(filename);
    for(String line : JS.lines.keySet()){
      addLine(line, JS.lines.get(line));
    }
    for(String name : JS.trips.keySet()){
      addJourney(name, JS.trips.get(name));
    }


  }

  private JsonConfig parseJson(String filename){
    String s = "";
    String line;
    try{
      BufferedReader br = new BufferedReader(new FileReader(filename));
      while((line = br.readLine()) != null){
          s += line;
      }
      br.close();
    }
    catch (Exception e){
      throw new RuntimeException(e.getMessage());
    }
    return gson.fromJson(s, JsonConfig.class);
  }



  public void run(Log log){
    this.log = log;

    for(Journey j: journeys){
      j.start();
    }
    for(Line line: lines){
      line.start();
    }
    for(Journey j: journeys){
      try{ j.join(); }
      catch (Exception e){ throw new RuntimeException(e.getMessage()); }
    }
    for(Line line: lines){
      try{ line.join(); }
      catch (Exception e){ throw new RuntimeException(e.getMessage()); }
    }
    
  }

  public Log getLog(){
    return log;
  }

  public Station getNextStation(Train t, Station curr){
    List<Station> line = t.getStations();
    int currIndex = line.indexOf(curr);
    if(currIndex == line.size() - 1){
      t.setFoward(false);
      return line.get(line.size() - 2);
    }
    if(currIndex == 0){
      t.setFoward(true);
      return line.get(1);
    }
    if(t.isFoward()){
      return line.get(currIndex + 1);
    }
    else{
      return line.get(currIndex - 1);
    }
  }

  public void updatePassenger(Passenger p){
    //Deboarding
    if(p.onTrain()){ 
      //Hold the lock of the destination station
      p.getNextLeg().lock();
      Station nextStop = p.getNextLeg(); 
      
      //while I'm not at my destination, wait
      while(p.getStation() != nextStop){
        try{ 
          nextStop.await(); 
        }
        catch (Exception e) { throw new RuntimeException(e.getMessage()); } }
        Station currStation = p.getStation();
        log.passenger_deboards(p, currStation.getTrain(), currStation);
        currStation.getTrain().removePassenger(p);
        currStation.addPassenger(p);
        p.setOnTrain(false);
        nextStop.unlock();
    }
    //Boarding
    else{
      p.getStation().lock();
      Station currStation = p.getStation();
      //while the train isn't at my station, or the train that is here doesn't have my stop, wait
      while(currStation.getTrain() == null ||  !currStation.getTrain().getStations().contains(p.getNextLeg())){
        try{ 
          currStation.await(); 
        }
        catch (Exception e) { throw new RuntimeException(e.getMessage()); }
      }
      log.passenger_boards(p, currStation.getTrain(), currStation);
      currStation.getTrain().addPassenger(p);
      currStation.removePassenger(p);
      currStation.unlock();
    }

  }

  public void updateTrain(Train t){
    Station currStation = t.getStation();
    currStation.lock();
    Station nextStation = getNextStation(t, currStation);
    nextStation.lock();
    currStation.signalPassengers();
    while(nextStation.getTrain() != null){  
      try{ nextStation.await(); }
      catch (Exception e) { throw new RuntimeException(e.getMessage()); } }  
    nextStation.setTrain(t);
    currStation.setTrain(null);
    log.train_moves(t, currStation, nextStation);
    nextStation.signalPassengers();
    nextStation.signalTrains();
    nextStation.unlock();
    currStation.signalTrains();

    currStation.unlock();
    try{ Thread.sleep(500); }
    catch(Exception e){ throw new RuntimeException(e.getMessage()); }
  }
  
    
}
