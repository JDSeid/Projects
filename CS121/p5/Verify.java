import java.io.*;

public class Verify {

  public static void verify(MBTA mbta, Log log) {
    mbta.checkStart();
    for (Event e : log.events()) {
      e.replayAndCheck(mbta);
    }
    mbta.checkEnd();
  }

  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 2) {
      System.out.println("usage: ./verify <config file> <log file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);
    Reader r = new BufferedReader(new FileReader(args[1]));
    LogJson LJ = LogJson.fromJson(r);



    Log log = LJ.toLog();
    verify(mbta, log);
  }
}
