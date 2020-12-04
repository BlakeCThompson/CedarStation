package WeatherStation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Controller {
    @FXML
    static private TableView<TimeWeather> timeWeathers;

    public static ObservableList<TimeWeather> getRecentTemps() throws ParseException {
        String encodedMSG = "";
        try {
            encodedMSG = URLEncoder.encode(",speed|mph", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String host = "https://api.synopticdata.com/v2/stations/timeseries";
        String query = "?STID=KCDC&recent=65&obtimezone=local&units=english" + encodedMSG + "&token=be4d2c633f72452d8c612a35f6f75b1b";
        HttpResponse<JsonNode> httpResponse = Unirest.get(host + "" + query)
                .header("accept", "application/json")
                .asJson();
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
        JSONArray windChill = (JSONArray) observations.get("wind_chill_set_1d");
        JSONArray dewPoint = (JSONArray) observations.get("dew_point_temperature_set_1");
        JSONArray milesVisibility = (JSONArray) observations.get("visibility_set_1");
        JSONArray relHumidity = (JSONArray) observations.get("relative_humidity_set_1");
        JSONArray pressure = (JSONArray) observations.get("relative_humidity_set_1");
        JSONArray seaLevelPressure = (JSONArray) observations.get("sea_level_pressure_set_1");
        ObservableList<TimeWeather> timeWeathers = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        System.out.println("wind speed units: " + jsonArray.getJSONObject(0).getJSONObject("UNITS").get("wind_speed"));

        for (int i = 0; i < dateTimes.length(); i++) {
            try {

                TimeWeather aTimeWeather = new TimeWeather();
                try {
                    aTimeWeather.setDate(dateTimes.get(i).toString());
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setTemp((int)Math.round(airTemp.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setDewPoint((int)Math.round(dewPoint.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setRelHumidity(    relHumidity.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setWindChill(windChill.getInt(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setWindDirection(windDirection.getString(i));
                } catch (Exception ignore) {
                }
                try {
                    aTimeWeather.setWindSpeed((int)Math.round(windSpeed.getDouble(i)));
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
            } catch (Exception e) {}
            for (TimeWeather timeWeather : timeWeathers) {
                System.out.println("Time: " + timeWeather.getDate() + " Temp: " + timeWeather.getTemp());
            }

        }return timeWeathers;
    }
}
