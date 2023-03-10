import java.util.*;

public class Journey extends Thread {
    Passenger p;
    List<Station> stations;
    Station leg;
    MBTA mbta;
    Log log;
    boolean done;

    public Journey(Passenger p, List<Station> stations, MBTA mbta, Log log){
        p.setStation(stations.get(0));
        this.p = p;
        p.setJourney(this);
        this.stations = stations;
        leg = stations.get(0);
        this.mbta = mbta;
        this.log = log;
        done = false;
    }

    public void setLeg(Station leg){
        this.leg = leg;
    }

    public List<Station> getStations(){
        return stations;
    }

    public Station getDestination(){
        return stations.get(stations.size() - 1);
    }

    public boolean done(){
        return done;
    } 

    public void checkIfDone(){
        if(p.getStation() == stations.get(stations.size() - 1)){
            done = true;
        }
    }

    public Station getNextLeg(){
        if(!done){
            return stations.get(stations.indexOf(leg) + 1);
        }
        else{
            return null;
        }
        
    }

    public Passenger getPassenger(){
        return p;
    }

    public void run(){
        while(!done()){
            mbta.updatePassenger(p);
        }
        mbta.passengerDone();
    }


}
