import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;

public class Tests {
  @Test public void testCaching() {
    Passenger p1 = Passenger.make("Frank");
    Passenger p2 = Passenger.make("Frank");
    assertTrue("Passengers should have physical equality", p1 == p2);
    Train t1 = Train.make("Red");
    Train t2 = Train.make("Red");
    assertTrue("Train should have physical equality", t1 == t2);
    Station s1 = Station.make("Hardvard");
    Station s2 = Station.make("Hardvard");
    assertTrue("Passengers should have physical equality", s1 == s2);
  }

  @Test public void testJSon(){
    Gson gson = new Gson();
    JsonConfig JS = new JsonConfig();
    HashMap<String, List<String>> lines = new HashMap<>();
    lines.put("red", List.of("Davis", "Harvard", "Kendall", "Park"));
    lines.put("orange", List.of("Ruggles", "Back Bay", "Tufts Medical Center", "Chinatown"));

    HashMap<String, List<String>> journeys = new HashMap<>();
    journeys.put("Bob", List.of("Park", "Tufts"));
    journeys.put("Alice", List.of("Davis", "Kendall"));
    JS.lines = lines;
    JS.trips = journeys;
    String s = gson.toJson(JS);
    //assertTrue("Printing s: " + s, false);

    MBTA sim = new MBTA();
    sim.loadConfig("sample.json");

  }

  @Test
  public void test1(){
    MBTA sim = new MBTA();
    Log log = new Log();
    List<String> stations = List.of("Davis", "Harvard", "Kendall", "Park", "Downtown Crossing",
    "South Station", "Broadway", "Andrew", "JFK");
    sim.addLine("red", stations);
    Train t = Train.make("red");
    Passenger p = Passenger.make("Bob");
    log.train_moves(t, Station.make("Davis"), Station.make("Hardvard"));
    log.passenger_boards(p, t, Station.make("Hardvard"));
    Verify.verify(sim, log);
  }

}
