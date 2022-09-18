import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import pl.dabkowski.edp.database.entities.ZtmRide;
import pl.dabkowski.edp.utils.Config;
import pl.dabkowski.edp.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
