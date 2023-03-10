import java.util.*;

public class Line extends Thread {
    private Train train;
    private List<Station> stations;
    private Station currStation;
    MBTA mbta;
    Log log;


    public Line(Train train, List<Station> stations, MBTA mbta, Log log){
        this.train = train;
        this.stations = stations;
        this.mbta = mbta;
        this.log = log;
        currStation = stations.get(0);
    }

    public Station getCurrentStation(){
        return currStation;
    }

    public void setCurrentStation(Station station){
        currStation = station;
    }


    public Train getTrain(){
        return train;
    }

    public List<Station> getStations(){
        return stations;
    }


    public void run(){
        while(!mbta.simDone()){
            mbta.updateTrain(train);
        }
        
    }



}
