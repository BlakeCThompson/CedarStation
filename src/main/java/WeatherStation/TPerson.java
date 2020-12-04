package WeatherStation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TPerson {
    private final SimpleStringProperty years = new SimpleStringProperty("");

    private final SimpleStringProperty fName = new SimpleStringProperty("");
    public TPerson() {
        this("", "");
    }
    public String getYears() {
        return years.get();
    }
    public StringProperty getYearsProperty(){
        return years;
    }

    public void setYears(int years) {
        this.years.set(Integer.toString(years));
    }
    public void setYears(String years) {
        this.years.set(years);
    }

    public String getFName() {
        return fName.get();
    }
    public StringProperty getFNameProperty(){
        return this.fName;
    }

    public void setFName(String fName) {
        this.fName.set(fName);
    }
    public TPerson(StringProperty years, StringProperty fName){
        this.years.set(years.get());
        this.fName.set(fName.get());
    }

    public TPerson(String years, String fName){
        setFName(fName);
        setYears(years);
    }
}
