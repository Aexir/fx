import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import pl.dabkowski.edp.database.entities.ZtmRide;
import pl.dabkowski.edp.utils.Config;
import pl.dabkowski.edp.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tests {

    @Test
    public void scheduleTest(){
        final String LYNK = "https://api.um.warszawa.pl/api/action/dbtimetable_get?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238&busstopId=1848&busstopNr=01&line=L43&apikey=f24cce85-da3c-412b-a0a5-371cf19e3a7f";
        List<ZtmRide> departures = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(LYNK).getAsJsonArray("result");
            System.out.println(jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++){

                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(0).getAsJsonObject().get("value").getAsString());
                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(1).getAsJsonObject().get("value").getAsString());
                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(2).getAsJsonObject().get("value").getAsString());
                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(3).getAsJsonObject().get("value").getAsString());
                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(4).getAsJsonObject().get("value").getAsString());
                System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(5).getAsJsonObject().get("value").getAsString());


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void line(){
        String apiKey = "f24cce85-da3c-412b-a0a5-371cf19e3a7f";
        String[] stopIds = {"1215"};
        String[] stopNrs = {"03"};
        String stopId = "1215";
        String stopNr= "03";
        //final String ALL_LINES = Config.getInstance().getProperty("ALL_LINES_FROM_BUSSTOP_URL").replace("{%apiKey%}", apiKey).replace("{%stopId%}", stopId).replace("{%stopNr%}", stopNr);
        final String XD = "https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e927d-4ad3-9500-4ab9e55deb59&apikey=f24cce85-da3c-412b-a0a5-371cf19e3a7f&type=1";
        for (int j = 0; j < 100; j++){
            new Thread(()->{
                try {
                    JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(XD).getAsJsonArray("result");
                    Set<String> trams = new HashSet<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        trams.add(jsonObject.get("Lines").getAsString());
                    }
                    System.out.println(trams);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }
        /*
        List<String> lines = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonUtils.getJsonObjectFromURL(ALL_LINES).getAsJsonArray("result");
            for (int i = 0; i < jsonArray.size(); i++) {
                lines.add(jsonArray.get(i).getAsJsonObject().getAsJsonArray("values").get(0).getAsJsonObject().get("value").getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.forEach(System.out::println);*/
    }
}
