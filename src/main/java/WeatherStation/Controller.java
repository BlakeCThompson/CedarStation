package WeatherStation;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
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
import java.util.*;


public class Controller {
    private static String token = "";

    public static TimeWeather getRecentTemps() {
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
                throw new NullPointerException("no token");
            }
        }catch(NullPointerException nullPointerException){throw nullPointerException;}

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
            } catch (JSONException jsonException) {
                try {
                    seaLevelPressure = (JSONArray) observations.get("sea_level_pressure_set_1d");
                } catch (JSONException ignored) {
                }
            }
            timeWeathers = FXCollections.observableArrayList();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            System.out.println("wind speed units: " + jsonArray.getJSONObject(0).getJSONObject("UNITS").get("wind_speed"));
        TimeWeather timeWeather = new TimeWeather();
            int i = dateTimes.length() - 1;
            try {


                try {
                    timeWeather.setDate(dateTimes.get(i).toString());
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setTemp((int) Math.round(airTemp.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setDewPoint((int)Math.round(dewPoint.getDouble(i)));
                } catch (Exception noDewPoint) {
                    boolean errored = true;
                    int incrementor = i;
                    while (errored && incrementor > 0) {
                        try {
                            timeWeather.setDewPoint((int)Math.round(dewPoint.getDouble(incrementor)));
                            errored=false;
                        } catch (Exception ignore) {
                            incrementor--;
                            System.out.println("Dew Point error for time:" + dateTimes.get(i).toString());
                        }

                    }
                }
                try {
                    timeWeather.setRelHumidity(relHumidity.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setWindChill(windChill.getInt(i));
                } catch (Exception ignore) {
                    System.out.println("Wind Chill error for time: " + dateTimes.get(i).toString());
                }
                try {
                    timeWeather.setWindDirection(windDirection.getString(i));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setWindSpeed((int) Math.round(windSpeed.getDouble(i)));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setMilesVisibility(milesVisibility.getInt(i));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setClouds(cloudCover.getJSONObject(i).get("sky_condition").toString());
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setStationPressure(pressure.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setSeaLevelPressure(seaLevelPressure.getDouble(i));
                } catch (Exception ignore) {
                }
                try {
                    timeWeather.setAltimeterSetting(altimeterSettings.getDouble(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timeWeathers.add(timeWeather);
            } catch (Exception ignored) {}

        return timeWeather;
    }


    public static Void getRecentTemps(TimeWeather givenTWeather) {
        String encodedMSG;
        BufferedReader reader;

        token = "";
        try{
            reader = new BufferedReader(new FileReader(Launcher.baseDir.getPath()+"/.token"));
            token = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (token.equals("")) {

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
        } catch (JSONException jsonException) {
            try {
                seaLevelPressure = (JSONArray) observations.get("sea_level_pressure_set_1d");
            } catch (JSONException ignored) {
            }
        }

        System.out.println("wind speed units: " + jsonArray.getJSONObject(0).getJSONObject("UNITS").get("wind_speed"));

        int i = dateTimes.length() - 1;
        try {


            try {
                givenTWeather.setDate(dateTimes.get(i).toString());
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setTemp((int) Math.round(airTemp.getDouble(i)));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setDewPoint((int) Math.round(dewPoint.getDouble(i)));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setRelHumidity(relHumidity.getDouble(i));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setWindChill(windChill.getInt(i));
            } catch (Exception ignore) {
                System.out.println("Wind Chill error for time: " + dateTimes.get(i).toString());
            }
            try {
                givenTWeather.setWindDirection(windDirection.getString(i));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setWindSpeed((int) Math.round(windSpeed.getDouble(i)));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setMilesVisibility(milesVisibility.getInt(i));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setClouds(cloudCover.getJSONObject(i).get("height_agl").toString());
            } catch (Exception noClouds) {
                givenTWeather.setClouds(cloudCover.getJSONObject(i).get("sky_condition").toString());
            }
            try {
                givenTWeather.setStationPressure(pressure.getDouble(i));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setSeaLevelPressure(seaLevelPressure.getDouble(i));
            } catch (Exception ignore) {
            }
            try {
                givenTWeather.setAltimeterSetting(altimeterSettings.getDouble(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ignored) {}
        return null;
    }




    public static void setToken(String newToken){
        try(FileWriter fileWriter = new FileWriter(Launcher.tokenFile)){
            fileWriter.write(newToken);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        token = newToken;
    }
    @FXML
    public static void getTokenDialog(){
        TextInputDialog tokenDialog = new TextInputDialog(Controller.getToken());
        tokenDialog.setTitle("Synoptic Data API Token");
        tokenDialog.setHeaderText("Please insert Synoptic data API token.");
        Optional<String> newToken = tokenDialog.showAndWait();
        newToken.ifPresent(Controller::setToken);
    }
    public static String getToken()
    {
        return token;
    }

    public void getTokenDialog(MouseEvent mouseEvent) {
        getTokenDialog();
    }

    public void refreshData(MouseEvent mouseEvent) {
        WeatherCaller.refreshData();
        System.out.println("Refreshed");
    }

}
