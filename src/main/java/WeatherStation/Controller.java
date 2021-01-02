package WeatherStation;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Controller {
    private static String token = "";
    public static ObservableList<TimeWeather> getRecentTemps() {
        String encodedMSG;
        BufferedReader reader;
        token = "";
        ObservableList<TimeWeather> timeWeathers;
        try{
            reader = new BufferedReader(new FileReader(Launcher.baseDir.getPath()+"/.token"));
            token = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (token.equals("")) {
                return null;
            }
        }catch(NullPointerException nullPointerException){return null;}

            encodedMSG = URLEncoder.encode(",speed|kts,temp|C", StandardCharsets.UTF_8);
            String host = "https://api.synopticdata.com/v2/stations/timeseries";
            String query = "?STID=KCDC&recent=65&obtimezone=local&units=english" + encodedMSG + "&token="+token;
            HttpResponse<JsonNode> httpResponse = Unirest.get(host + "" + query)
                    .header("accept", "application/json")
                    .asJson();
        if(httpResponse.getStatusText().contains("Forbidden")){
            return null;
        }
            JsonNode jsonResponse = httpResponse.getBody();
            JSONArray jsonArray = jsonResponse.getArray();
            JSONObject observations = jsonArray.getJSONObject(0)
                    .getJSONArray("STATION")
                    .getJSONObject(0)
                    .getJSONObject("OBSERVATIONS");
            JSONArray dateTimes = (JSONArray) observations.get("date_time");
            JSONArray airTemp = (JSONArray) observations.get("air_temp_set_1");
            JSONArray cloudCover = (JSONArray) observations.get("cloud_layer_1_set_1d");
            JSONArray altimeterSettings = (JSONArray) observations.get("altimeter_set_1");
            JSONArray windDirection = (JSONArray) observations.get("wind_cardinal_direction_set_1d");
            JSONArray windSpeed = (JSONArray) observations.get("wind_speed_set_1");
            JSONArray windChill = new JSONArray();
            try {
                windChill = (JSONArray) observations.get("wind_chill_set_1d");
            } catch (Exception e) {
                windChill = new JSONArray();
            }
            JSONArray dewPoint = (JSONArray) observations.get("dew_point_temperature_set_1");
            JSONArray milesVisibility = (JSONArray) observations.get("visibility_set_1");
            JSONArray relHumidity = (JSONArray) observations.get("relative_humidity_set_1");
            JSONArray pressure = (JSONArray) observations.get("relative_humidity_set_1");
            JSONArray seaLevelPressure = new JSONArray();
            try {
                seaLevelPressure = (JSONArray) observations.get("sea_level_pressure_set_1");
            } catch (kong.unirest.json.JSONException jsonException) {
                try {
                    seaLevelPressure = (JSONArray) observations.get("sea_level_pressure_set_1d");
                } catch (JSONException ignored) {
                }
            }
            timeWeathers = FXCollections.observableArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            System.out.println("wind speed units: " + jsonArray.getJSONObject(0).getJSONObject("UNITS").get("wind_speed"));

            int i = dateTimes.length() - 1;
            try {

                TimeWeather aTimeWeather = new TimeWeather();
                try {
                    aTimeWeather.setDate(dateTimes.get(i).toString());
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setTemp((int) Math.round(airTemp.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setDewPoint((int) Math.round(dewPoint.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setRelHumidity(relHumidity.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setWindChill(windChill.getInt(i));
                } catch (Exception ignore) {
                    System.out.println("Wind Chill error for time: " + dateTimes.get(i).toString());
                }
                try {
                    aTimeWeather.setWindDirection(windDirection.getString(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setWindSpeed((int) Math.round(windSpeed.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setMilesVisibility(milesVisibility.getInt(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setClouds(cloudCover.getJSONObject(i).get("sky_condition").toString());
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setStationPressure(pressure.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setSeaLevelPressure(seaLevelPressure.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setAltimeterSetting(altimeterSettings.getDouble(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timeWeathers.add(aTimeWeather);
            } catch (Exception ignored) {}

        return timeWeathers;
    }
    public static void setToken(String newToken){
        try(FileWriter fileWriter = new FileWriter(Launcher.tokenFile)){
            fileWriter.write(newToken);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        token = newToken;
    }
    public static String getToken()
    {
        return token;
    }
}
