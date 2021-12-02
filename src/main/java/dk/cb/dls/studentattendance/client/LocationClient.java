package dk.cb.dls.studentattendance.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.cb.dls.studentattendance.DTO.LocationDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LocationClient {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String accesskey = "2bb4c470b78998b22bfae3b5016602c7";

    public static LocationDTO getLocation(String ip) throws IOException {
        URL url = new URL("http://api.ipstack.com/" + ip + "?access_key=" + accesskey );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
            jsonStr = scan.nextLine();
        }
        scan.close();
        LocationDTO location = GSON.fromJson(jsonStr, LocationDTO.class);
        return location;
    }
}
