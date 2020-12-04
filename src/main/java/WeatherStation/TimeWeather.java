package WeatherStation;

import javafx.beans.property.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class TimeWeather {
    private ObjectProperty<Date> date = new SimpleObjectProperty<>(this,"date");
    private SimpleIntegerProperty temp = new SimpleIntegerProperty(0);
    private  SimpleIntegerProperty  dewPoint = new SimpleIntegerProperty(0);
    private  SimpleDoubleProperty  relHumidity = new SimpleDoubleProperty(0);
    private  SimpleDoubleProperty  windChill = new SimpleDoubleProperty(0);
    private SimpleStringProperty windDirection = new SimpleStringProperty("");
    private SimpleIntegerProperty windSpeed = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty milesVisibility = new SimpleIntegerProperty(0);
    private SimpleStringProperty clouds = new SimpleStringProperty("");
    private SimpleDoubleProperty stationPressure= new SimpleDoubleProperty(0);
    private SimpleDoubleProperty seaLevelPressure= new SimpleDoubleProperty(0);
    private SimpleDoubleProperty altimeterSetting = new SimpleDoubleProperty(0);

    public TimeWeather(String date, int temp, int dewPoint, double relHumidity,
                       double windChill, String windDirection, int windSpeed, int milesVisibility,
                       String clouds, double stationPressure, double seaLevelPressure, double altimeterSetting) {
        try{setDate(date);
        setTemp(temp);
        setDewPoint(dewPoint);
        setRelHumidity(relHumidity);}catch(Exception e){}
        try{setWindChill(windChill);}catch (Exception e){}
        setWindDirection(windDirection);
        setWindSpeed(windSpeed);
        setMilesVisibility(milesVisibility);
        setClouds(clouds);
        setStationPressure(stationPressure);
        try{setSeaLevelPressure(seaLevelPressure);}catch(Exception e){}
        setAltimeterSetting(altimeterSetting);
    }


    public String getDate() {
        return date.get().toString();
    }

    public int getTemp() {
        return temp.get();
    }

    public int getDewPoint() {
        return dewPoint.get();
    }

    public double getRelHumidity() {
        return(double) Math.round(relHumidity.get());
    }

    public double getWindChill() {
        return windChill.get();
    }

    public String getWindDirection() {
        return windDirection.get();
    }

    public int getWindSpeed() {
        return windSpeed.get();
    }

    public int getMilesVisibility() {
        return milesVisibility.get();
    }

    public String getClouds() {
        return clouds.get();
    }

    public double getStationPressure() {
        return stationPressure.get();
    }

    public double getSeaLevelPressure() {
        if(seaLevelPressure.isBound()) {
            return seaLevelPressure.get();
        }
        else return 0;
    }

    public double getAltimeterSetting() {
        return altimeterSetting.get();
    }
    public void setDate(Date date) {
        this.date.setValue(date);
    }
    public void setDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{
            this.date.setValue(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setTemp(int temp) {
        this.temp.set(temp);
    }

    public void setDewPoint(int dewPoint) {
        this.dewPoint.set(dewPoint);
    }


    public void setWindChill(double windChill) {
        this.windChill.set(windChill);
    }

    public void setWindDirection(String windDirection) {
        this.windDirection.set(windDirection);
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed.set(windSpeed);
        if(this.temp.get()!=0){
            setWindChill(temp.get(),this.windSpeed.get());
        }
    }

    public void setMilesVisibility(int milesVisibility) {
        this.milesVisibility.set(milesVisibility);
    }

    public void setClouds(String clouds) {
        this.clouds.set(clouds);
    }
    public void setRelHumidity(double relHumidity) {
        this.relHumidity.set(relHumidity);
    }

    public void setStationPressure(double stationPressure) {
        this.stationPressure.set(stationPressure);
    }

    public void setSeaLevelPressure(double seaLevelPressure){
        this.seaLevelPressure.set(seaLevelPressure);
    }
    public void setSeaLevelPressure(Optional<Double> seaLevelPressure) {
        this.seaLevelPressure.set(seaLevelPressure.get());
    }

    public void setAltimeterSetting(double altimeterSetting) {
        this.altimeterSetting.set(altimeterSetting);
    }
    public void setWindChill(int temp, int windSpeed)
    {
        this.windChill.set(35.74 + (0.6215*temp) - (35.75*Math.pow(windSpeed,0.16)));
    }

    public TimeWeather(){

    }
    public TimeWeather(String date, int temp, int dewPoint,
                       int relHumidity, String windDirection,
                       int windSpeed, int milesVisibility,
                       String clouds, double stationPressure,
                       double altimeterSetting) {
        this(date,temp,dewPoint, relHumidity,windDirection,windSpeed,milesVisibility,clouds,stationPressure,0,altimeterSetting);
    }

    public TimeWeather(String date, int temp,
                       int dewPoint, double relHumidity,
                       String windDirection,
                       int windSpeed,
                       int milesVisibility,
                       String clouds,
                       double stationPressure,
                       double seaLevelPressure,
                       double altimeterSetting) {
        setDate(date);
        setTemp(temp);
        setDewPoint(dewPoint);
        setRelHumidity(relHumidity);
        setWindChill(temp,windSpeed);
        setWindDirection(windDirection);
        setWindSpeed(windSpeed);
        setMilesVisibility(milesVisibility);
        setClouds(clouds);
        setStationPressure(stationPressure);
        setSeaLevelPressure(seaLevelPressure);
        setAltimeterSetting(altimeterSetting);
    }
    public TimeWeather(Date date, int temp, int dewPoint, double relHumidity, String windDirection, int windSpeed, int milesVisibility, String clouds, double stationPressure, Optional<Double> seaLevelPressure, double altimeterSetting) {
        setDate(date);
        setTemp(temp);
        setDewPoint(dewPoint);
        setRelHumidity(relHumidity);
        setWindChill(temp,windSpeed);
        setWindDirection(windDirection);
        setWindSpeed(windSpeed);
        setMilesVisibility(milesVisibility);
        setClouds(clouds);
        setStationPressure(stationPressure);
        setSeaLevelPressure(seaLevelPressure);
        setAltimeterSetting(altimeterSetting);
    }
}
