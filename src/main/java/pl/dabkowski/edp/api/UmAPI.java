package pl.dabkowski.edp.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pl.dabkowski.edp.database.SqlManager;
import pl.dabkowski.edp.database.entities.Busstop;
import pl.dabkowski.edp.database.entities.Location;
import pl.dabkowski.edp.database.entities.ZtmRide;
import pl.dabkowski.edp.exceptions.LocationException;
import pl.dabkowski.edp.utils.Config;
import pl.dabkowski.edp.utils.JsonUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UmAPI implements UmApiInterface {

    private static UmAPI instance;
    private final String apiKey;

    private UmAPI() {
        this.apiKey = Config.getInstance().getProperty("UM_API_KEY");
    }

    public static UmAPI getInstance() {
        if (instance == null) {
            instance = new UmAPI();
        }
        return instance;
    }

    @Override
    public void loadAllBusstops() {
        final String URL_ALL_STOPS = Config.getInstance().getProperty("ALL_STOPS_URL").replace("{%apiKey%}", apiKey);
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(URL_ALL_STOPS).getAsJsonArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                JsonArray array = object.getAsJsonArray("values");
                List<String> stringList = new ArrayList<>();
                for (int j = 0; j < array.size(); j++) {
                    stringList.add(array.get(j).getAsJsonObject().get("value").getAsString());
                }
                Busstop busstop = new Busstop();
                try {
                    busstop.setLocation(new Location(stringList.get(4), stringList.get(5)));
                    busstop.setStreet_id(stringList.get(3));
                    busstop.setStreet(stringList.get(2));
                    busstop.setStop_id(stringList.get(0));
                    busstop.setStop_nr(stringList.get(1));
                    busstop.setDirection(stringList.get(6));

                } catch (LocationException e) {
                    e.printStackTrace();
                }
                if (!busstop.getStreet_id().contains("null")){
                    Busstop.addBusstopToList(busstop);
                    SqlManager.getInst().saveObject(busstop);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStopIdFromName(String name) {
        name = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        final String STOPID_FROM_NAME_URL = Config.getInstance().getProperty("STOP_ID_FROM_NAME_URL").replace("{%name%}", name).replace("{%apiKey%}", apiKey);
        System.out.println("STOPID_FROM_NAME_URL: " + STOPID_FROM_NAME_URL);
        String ret = "";

        try {
            return ret = JsonUtils.getJsonObjectFromURL(STOPID_FROM_NAME_URL).get("result").getAsJsonArray().get(0).
                    getAsJsonObject().get("values").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "1152";
    }

    @Override
    public List<Busstop> getAllStops() {
        final String URL_ALL_STOPS = Config.getInstance().getProperty("ALL_STOPS_URL").replace("{%apiKey%}", apiKey);
        System.out.println(URL_ALL_STOPS);
        List<Busstop> stops = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(URL_ALL_STOPS).getAsJsonArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray array = jsonArray.get(i).getAsJsonObject().getAsJsonArray("values");
                List<String> stringList = new ArrayList<>();
                for (int j = 0; j < array.size(); j++) {
                    stringList.add(array.get(j).getAsJsonObject().get("value").getAsString());
                }
                Busstop busstop = new Busstop();
                try {
                    busstop.setLocation(new Location(stringList.get(4), stringList.get(5)));
                    busstop.setStreet_id(stringList.get(3));
                    busstop.setStreet(stringList.get(2));
                    busstop.setStop_id(stringList.get(0));
                    busstop.setStop_nr(stringList.get(1));
                    busstop.setDirection(stringList.get(6));
                } catch (LocationException e) {
                    e.printStackTrace();
                }
                if (!busstop.getStreet_id().contains("null")){
                    stops.add(busstop);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stops;
    }

    @Override
    public List<Busstop> getAllStopsFromDatabase() {
        return SqlManager.getInst().selectAllBusstops();
    }

    @Override
    public List<String> getLinesForStop(String stopId, String stopNr) {
        final String ALL_LINES = Config.getInstance().getProperty("ALL_LINES_FROM_BUSSTOP_URL").replace("{%apiKey%}", apiKey).replace("{%stopId%}", stopId).replace("{%stopNr%}", stopNr);
        List<String> lines = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(ALL_LINES).getAsJsonArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                lines.add(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(0).getAsJsonObject().get("value").getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public List<String> getAllBusLines() {
        final String ALL_BUS_LINES = Config.getInstance().getProperty("ALL_BUSES_URL").replace("{%apiKey%}", apiKey);
        Set<String> buses = new HashSet<>();
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(ALL_BUS_LINES).getAsJsonArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                buses.add(jsonObject.get("Lines").getAsString());
            }
            } catch(IOException e){
                e.printStackTrace();
            }
            return buses.stream().toList();
        }

        @Override
        public List<String> getAllTramLines () {
            final String ALL_TRAM_LINES = Config.getInstance().getProperty("ALL_TRAMS_URL").replace("{%apiKey%}", apiKey);
            Set<String> trams = new HashSet<>();
            try {
                JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(ALL_TRAM_LINES).getAsJsonArray("result");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    trams.add(jsonObject.get("Lines").getAsString());
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            return trams.stream().toList();
        }

    @Override
    public String getNameFromId(String id, String nr) {
        return SqlManager.getInst().getNameFromId(id, nr);
    }

    @Override
        public void loadLineScheduleForBusstop (String stopId, String stopNr, String line){
            final String DEPARTURES_URL = Config.getInstance().getProperty("LINE_DEPARTURES_FROM_BUSSTOP_URL")
                    .replace("{%apiKey%}", apiKey).replace("{%stopId%}", stopId)
                    .replace("{%stopNr%}", stopNr).replace("{%line%}", line);
            List<ZtmRide> departures = new ArrayList<>();
            try {
                JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(DEPARTURES_URL).getAsJsonArray("result");
                for (int i = 0; i < jsonArray.size(); i++) {
                    ZtmRide ztmRide = new ZtmRide();
                    ztmRide.setLine(line);
                    ztmRide.setSymbol_1(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(0).getAsJsonObject().get("value").getAsString());
                    ztmRide.setSymbol_2(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(1).getAsJsonObject().get("value").getAsString());
                    ztmRide.setBrigade(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(2).getAsJsonObject().get("value").getAsString());
                    ztmRide.setDirection(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(3).getAsJsonObject().get("value").getAsString());
                    ztmRide.setPath(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(4).getAsJsonObject().get("value").getAsString());
                    ztmRide.setTime(Time.valueOf(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(5).getAsJsonObject().get("value").getAsString()));

                    ZtmRide.addDepartureToList(ztmRide);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
