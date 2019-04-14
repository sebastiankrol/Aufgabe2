import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")



@Path("text/analyze/{json}")
public class Response extends Application {

    @GET
    @Produces(MediaType.APPLICATION_JSON)


    public String produceJSON(@PathParam("json") String json) {


        return  response(json).toString();

    }


    public String getMax(String arr, String filter, String resultName) {
        JSONArray result = new JSONArray(arr);
        String max = "";
        double maxPrice = 0;

        //Schleife durch das JSON Objekt
        for (int i = 0; i < result.length(); i++) {

            JSONObject item_i = result.getJSONObject(i);
            //Setze den maxPreis, wenn das aktuelle Objekt größer als das vorherige Max ist
            if (maxPrice < item_i.getInt(filter)) {
                maxPrice = item_i.getInt(filter);
                max = item_i.getString(resultName);

            }


        }
        return max;
    }

    public String getMin(String arr, String filter, String resultName) {
        JSONArray result = new JSONArray(arr);
        String min = "";
        double minPrice = result.getJSONObject(0).getInt(filter);

        //Schleife durch das JSON Objekt
        for (int i = 0; i < result.length(); i++) {

            JSONObject item_i = result.getJSONObject(i);


            //Setze den minPreis, wenn das aktuelle Objekt kleiner als das vorherige Min ist
            if (minPrice > item_i.getInt(filter) || item_i.getInt(filter) < 0) {
                minPrice = item_i.getInt(filter);
                min = item_i.getString(resultName);

            }


        }
        return min;
    }
    //Gibt true aus, wenn sich ein JSON Objekt mit true in dem JSON Array befindet
    public boolean checkBool(String arr, String filter) {
        boolean status = false;
        JSONArray result = new JSONArray(arr);
        //Schleife durch das JSON Objekt
        for (int i = 0; i < result.length(); i++) {

            JSONObject item_i = result.getJSONObject(i);
            if (item_i.getBoolean(filter) == true) {
                status = true;
            }

        }


        return status;
    }


    //Schreibt die Produkte aus dem Land in eine Liste
    public List<String> countryOfOrigin(String arr,String country) {

        JSONArray result = new JSONArray(arr);
        List<String> countryOfOrigin = new ArrayList<>();


        for (int i = 0; i < result.length(); i++) {


            JSONObject item_i = result.getJSONObject(i);
            if(item_i.getString("countryOfOrigin").equals(country)) {
                countryOfOrigin.add(item_i.getString("name"));
            }

        }


        return countryOfOrigin;
    }
    //Hier werden die Funktionen genutzt und die Antwort wird als JSON Objekt ausgegeben
    public JSONObject response(String json){
        String mostExpensiveProduct = getMax(json, "price", "name");
        String cheapestProduct = getMin(json, "price", "name");
        String mostPopularProduct = getMax(json, "timesPurchased", "name");
        boolean containsFragileProducts = checkBool(json, "isFragile");
        List<String> countryOfOriginDE = countryOfOrigin(json,"DE");
        List<String> countryOfOriginCN = countryOfOrigin(json,"CN");

        JSONObject response = new JSONObject();
        response.put("mostExpensiveProduct",mostExpensiveProduct);
        response.put("cheapestProduct",cheapestProduct);
        response.put("mostPopularProduct",mostPopularProduct);
        response.put("germanProducts",countryOfOriginDE);
        response.put("chineseProducts",countryOfOriginCN);
        response.put("containsFragileProducts",containsFragileProducts);

        return response;
    }
}


