package WeatherStation;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class TimeWeather {
    private StringProperty date = new SimpleStringProperty();
    private StringProperty temp = new SimpleStringProperty();
    private  StringProperty  dewPoint = new SimpleStringProperty();
    private  StringProperty  relHumidity = new SimpleStringProperty();
    private  StringProperty  windChill = new SimpleStringProperty();
    private StringProperty windDirection = new SimpleStringProperty();
    private StringProperty windSpeed = new SimpleStringProperty();
    private StringProperty milesVisibility = new SimpleStringProperty();
    private StringProperty clouds = new SimpleStringProperty();
    private StringProperty stationPressure = new SimpleStringProperty();
    private StringProperty seaLevelPressure = new SimpleStringProperty();
    private StringProperty altimeterSetting = new SimpleStringProperty();
    private StringProperty densityAltitude = new SimpleStringProperty();

    public StringProperty getDensityAltitude() {
        return densityAltitude;
    }

    public TimeWeather(String date, int temp, int dewPoint, double relHumidity,
                       double windChill, String windDirection, int windSpeed, int milesVisibility,
                       String clouds, double stationPressure, double seaLevelPressure, double altimeterSetting) {
        try{setDate(date);
        setTemp(temp);
        setDewPoint(dewPoint);
        setRelHumidity(relHumidity);}catch(Exception ignored){}
        try{setWindChill(windChill);}catch (Exception ignored){}
        setWindDirection(windDirection);
        setWindSpeed(windSpeed);
        setMilesVisibility(milesVisibility);
        setClouds(clouds);
        setStationPressure(stationPressure);
        try{setSeaLevelPressure(seaLevelPressure);}catch(Exception ignored){}
        setAltimeterSetting(altimeterSetting);
    }


    public StringProperty getDate() {
        return date;
    }
    public Date getDateObj() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{
            return(sdf.parse(date.getValue()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StringProperty getTemp() {
        return temp;
    }

    public StringProperty getDewPoint() {
        return dewPoint;
    }

    public double getRelHumidity() {
        return(double) Math.round(Double.valueOf(String.valueOf(relHumidity)));
    }

    public StringProperty getWindChill() {return windChill;}

    public StringProperty getWindDirection() {
        return windDirection;
    }

    public StringProperty getWindSpeed() {
        return windSpeed;
    }

    public StringProperty getMilesVisibility() {
        return milesVisibility;
    }

    public StringProperty getClouds() {
        return clouds;
    }

    public StringProperty getStationPressure() {
        return stationPressure;
    }

    public Object getSeaLevelPressure() {
        try{
            return seaLevelPressure;
        }catch(NullPointerException ignored){}
        return 0;
    }

    public StringProperty getAltimeterSetting() {return altimeterSetting;}
    public void setDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.date.setValue(sdf.format(date));
    }
    public void setDate(String date) {
        this.date.setValue(date);
    }

    public void setTemp(int temp) {
        this.temp.setValue(String.valueOf(temp));
        try {
            setDensityAltitude();
        }catch(Exception ohWell){}
    }
    public void setTemp(String temp) {
        this.temp.setValue(temp);
        try {
            setDensityAltitude();
        }catch(Exception ohWell){}
    }

    public void setDewPoint(int dewPoint) {
        this.dewPoint.set(String.valueOf(dewPoint));
    }
    public void setDewPoint(String dewPoint) {
        this.dewPoint.set(dewPoint);
    }


    public void setWindChill(double windChill) {
        this.windChill.set(String.valueOf(windChill));
    }
    public void setWindChill(String windChill) {
        this.windChill.set(windChill);
    }

    public void setWindDirection(String windDirection) {
        this.windDirection.set(windDirection);
    }
    private void setDensityAltitude()
    {
        int temp = Integer.parseInt(this.temp.get());
        double altSetting = Double.parseDouble(this.altimeterSetting.get());
        double densAlt = (((29.92-altSetting)*1000)+5600)+(temp-4)*120;
        this.densityAltitude.set(String.valueOf(densAlt));
    }
    public void setWindSpeed(int windSpeed) {
        this.windSpeed.set(String.valueOf(windSpeed));
    }
    public void setWindSpeed(String windSpeed) {
        this.windSpeed.set(windSpeed);
    }

    public void setMilesVisibility(int milesVisibility) {
        this.milesVisibility.set(String.valueOf(milesVisibility));
    }

    public void setMilesVisibility(String milesVisibility) {
        this.milesVisibility.set(milesVisibility);
    }

    public void setClouds(String clouds) {
        this.clouds.set(clouds);
    }
    public void setRelHumidity(double relHumidity) {
        this.relHumidity.set(String.valueOf(relHumidity));
    }
    public void setRelHumidity(String relHumidity) {
        this.relHumidity.set(relHumidity);
    }

    public void setStationPressure(double stationPressure) {
        this.stationPressure.set(String.valueOf(stationPressure));
    }
    public void setStationPressure(String stationPressure) {
        this.stationPressure.set(stationPressure);
    }

    public void setSeaLevelPressure(double seaLevelPressure){
        this.seaLevelPressure.set(String.valueOf(seaLevelPressure));
    }
    public void setSeaLevelPressure(String seaLevelPressure){
        this.seaLevelPressure.set(seaLevelPressure);
    }
    public void setSeaLevelPressure(Optional<Double> seaLevelPressure) {
        seaLevelPressure.ifPresent(aDouble -> this.seaLevelPressure.set(String.valueOf(aDouble)));
    }

    public void setAltimeterSetting(double altimeterSetting) {
        this.altimeterSetting.set(String.valueOf(altimeterSetting));
        try {
            setDensityAltitude();
        }catch(Exception ohWell){}
    }
    public void setAltimeterSetting(String altimeterSetting) {
        this.altimeterSetting.set(altimeterSetting);
        try {
            setDensityAltitude();
        }catch(Exception ohWell){}
    }

  /*  public void setWindChill(int temp, int windSpeed)
    {
        this.windChill.set(35.74 + (0.6215*temp) - (35.75*Math.pow(windSpeed,0.16)));
    }*/

    public TimeWeather(){
        setDate(new Date());
        setTemp("N/A");
        setDewPoint("N/A");
        setRelHumidity("N/A");
        setWindDirection("N/A");
        setWindSpeed("N/A");
        setMilesVisibility("N/A");
        setClouds("N/A");
        setStationPressure("N/A");
        setSeaLevelPressure("N/A");
        setAltimeterSetting("N/A");
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
        setWindDirection(windDirection);
        setWindSpeed(windSpeed);
        setMilesVisibility(milesVisibility);
        setClouds(clouds);
        setStationPressure(stationPressure);
        setSeaLevelPressure(seaLevelPressure);
        setAltimeterSetting(altimeterSetting);
    }
}
