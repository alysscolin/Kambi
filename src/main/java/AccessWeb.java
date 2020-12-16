import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/* access the web to get thd odds information */
public class AccessWeb {
    URL url;
    String oddStr = "";

    public AccessWeb(String link) throws MalformedURLException {
        this.url = new URL(link);
    }

    public String readOddsStrFromWeb() {
        //Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //Set the request to GET as per the requirements
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException protocolException) {
            protocolException.printStackTrace();
        }
        //Use the connect method to create the connection bridge
        try {
            conn.connect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        //Get the response status of the Rest API
        int responsecode = 0;
        try {
            responsecode = conn.getResponseCode();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " +responsecode);
        else
        {
            //Scanner functionality will read the JSON data from the stream
            Scanner sc = null;
            try {
                sc = new Scanner(url.openStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            while(sc.hasNext())
            {
                oddStr+=sc.nextLine();
            }
            //Close the stream when reading the data has been finished
            sc.close();
        }
        return oddStr;
    }

    public ArrayList<LiveEvent> getJsonObjs(String oddStr) throws ParseException {
        //JSONParser reads the data from string object and break each data into key value pairs
        JSONParser parse = new JSONParser();
        //Type caste the parsed json data in json object
        JSONObject jobj = (JSONObject)parse.parse(oddStr);
        //Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
        JSONArray jsonarr = (JSONArray) jobj.get("liveEvents");
        ArrayList<LiveEvent> list = new ArrayList<>();

        //get data for liveEvents array
        for (int i = 0; i < jsonarr.size(); i++) {
            LiveEvent liveEvent = new LiveEvent();
            Event event = new Event();
            JSONObject json_liveEvent = (JSONObject)jsonarr.get(i);
            //get event object data
            JSONObject json_event = (JSONObject)json_liveEvent.get("event");
            long id = (long)json_event.get("id");
            event.setId(id);
            String name = (String)json_event.get("name");
            event.setName(name);
            JSONArray tagsArr = (JSONArray)json_event.get("tags");
            String[] tags = new String[tagsArr.size()];
            if (tagsArr != null) {
                for (int k = 0; k < tagsArr.size(); k++) {
                    tags[k] = (String)tagsArr.get(k);
                }
            }
            event.setTags(tags);
            liveEvent.setEvent(event);

            //get mainBetOffer data
            JSONObject json_mainBetOffer = (JSONObject)json_liveEvent.get("mainBetOffer");
            //there are some liveEvent items without mainBetOffer, just skip them
            if (json_mainBetOffer == null)
                continue;
            MainBetOffer mainBetOffer = new MainBetOffer();
            long mainBetOffer_id = (long)json_mainBetOffer.get("id");
            mainBetOffer.setId(mainBetOffer_id);
            //get outcomes data
            JSONArray json_outcomes = (JSONArray)json_mainBetOffer.get("outcomes");
            OutCome[] outComes = new OutCome[json_outcomes.size()];
            for(int j = 0; j < json_outcomes.size(); j++) {
                OutCome outCome = new OutCome();
                JSONObject json_outcome = (JSONObject)json_outcomes.get(j);
                if (json_outcome == null)
                    continue;
                long outcome_id = (long)json_outcome.get("id");
                outCome.setId(outcome_id);
                String label = json_outcome.get("label").toString();
                outCome.setLabel(label);
                if (json_outcome.get("odds") == null)
                    continue;
                long odds = (long)json_outcome.get("odds");
                outCome.setOdds(odds);

                outComes[j] = outCome;
            }
            mainBetOffer.setOutComes(outComes);
            liveEvent.setMainBetOffer(mainBetOffer);

            list.add(liveEvent);
        }
        return list;
    }
}
