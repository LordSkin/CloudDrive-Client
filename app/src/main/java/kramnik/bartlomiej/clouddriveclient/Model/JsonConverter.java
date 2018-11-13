package kramnik.bartlomiej.clouddriveclient.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kramnik.bartlomiej.clouddriveclient.Model.DataModels.Event;
import kramnik.bartlomiej.clouddriveclient.Model.DataModels.FileDetails;

/**
 * Mapping jsons to objects or structures
 */

public class JsonConverter {

    private Gson gson;

    public JsonConverter(Gson gson) {
        this.gson = gson;
    }

    public List<FileDetails> getFilesList(String json){

        if (json==null) throw new NullPointerException();

        List<FileDetails> result = new ArrayList<FileDetails>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                result.add(gson.fromJson(array.get(i).toString(), FileDetails.class));
            }
        }
        catch (JSONException e) {
            Log.d("Json Converter:", "can't convert string to json "+json);
        }
        return result;
    }

    public List<Event> getEventsList(String json){
        if (json==null) throw new NullPointerException();

        List<Event> result = new ArrayList<Event>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                result.add(gson.fromJson(array.get(i).toString(), Event.class));
            }
        }
        catch (JSONException e) {
            Log.d("Json Converter:", "can't convert string to json "+json);
        }
        return result;
    }

}
