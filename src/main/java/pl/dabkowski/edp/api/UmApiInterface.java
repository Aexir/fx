package pl.dabkowski.edp.api;

import pl.dabkowski.edp.database.entities.Busstop;

import java.util.List;

public interface UmApiInterface {
//    void updateBusLocation();
//    void updateTramLocation();
    void loadAllBusstops();
    void loadLineScheduleForBusstop(String stopId, String stopNr, String line);

    String getStopIdFromName(String name);
    List<Busstop> getAllStops();
    List<Busstop> getAllStopsFromDatabase();
    List<String> getLinesForStop(String stopId, String stopNr);
//    List<String> getAllBusLines();
//    List<String> getAllTramLines();
//    List<String> getStopsForStreet(String street);
}
