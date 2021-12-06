package dk.cb.dls.studentattendance.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.cb.dls.studentattendance.DTO.LocationDTO;
import dk.cb.dls.studentattendance.errorhandling.LocationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class LocationClient {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final static String accesskey = "2bb4c470b78998b22bfae3b5016602c7";

    public static LocationDTO getLocation(String ip) throws LocationException {
        try {
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

        } catch (IOException e)
        {
            throw new LocationException("Could not retrieve location for ip: " + ip + " with cause: " + e.getMessage());
        }
    }

    public static String getIp() throws LocationException {
        try {
            URL url = new URL("https://api.ipify.org" );
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            Scanner scan = new Scanner(con.getInputStream());
            String ip = null;
            if (scan.hasNext()) {
                ip = scan.nextLine();
            }
            scan.close();
            return ip;

        } catch (IOException e)
        {
            throw new LocationException("Could not retrieve Ip with cause: " + e.getMessage());
        }

    }
}
