package WeatherStation;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class WeatherCaller extends Application {
    private static ObservableList<TimeWeather> timeWeathers;
    private boolean tokenSet = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            timeWeathers = Controller.getRecentTemps();
            tokenSet = !timeWeathers.isEmpty();
        }catch(NullPointerException nullPointerException){
            tokenSet=false;
        }
        Parent root =  FXMLLoader.load(getClass().getResource("/WeatherReport.fxml"));
        primaryStage.setTitle("Cedar KCDC");
        if(tokenSet) {
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        //gridPane.setMinWidth(1400);
        HBox hBox = (HBox) gridPane.getChildren().get(0);
        VBox readingsHolder = (VBox) hBox.getChildren().get(1);

            setReadings(readingsHolder);
            //TableView<TimeWeather> fxmlTimeWeathers = (TableView<TimeWeather>) gridPane.getChildren().get(1);
            //fxmlTimeWeathers.setFixedCellSize(60);
            //fxmlTimeWeathers.prefHeightProperty().bind(Bindings.size(fxmlTimeWeathers.getItems()).multiply(fxmlTimeWeathers.getFixedCellSize()).add(80));
            //fxmlTimeWeathers.setItems(timeWeathers);
            //fxmlTimeWeathers.getColumns().get(0).setSortType(TableColumn.SortType.DESCENDING);
        }
        else{
            TextInputDialog tokenReciever=getTokenDialog();
            Optional<String> newToken = tokenReciever.showAndWait();
            newToken.ifPresent(Controller::setToken);
        }
        //setReadings();
        //fxmlTimeWeathers.getSortOrder().add(fxmlTimeWeathers.getColumns().get(0));

        primaryStage.setScene(new Scene(root, 1400, 840));
        primaryStage.setMinWidth(1450);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    private static void setReadings(VBox readingsHolder){
        LocalDateTime sixMinAgo = LocalDateTime.now().minusMinutes(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        TimeWeather recentTimeWeather = timeWeathers.get(0);
        Instant currentDate = recentTimeWeather.getDateObj().toInstant();

        //if it has been more than 6 minutes ago, try to get a newer reading.
        if(currentDate.isBefore(sixMinAgo.toInstant(ZoneOffset.ofTotalSeconds(0)))){
            timeWeathers=Controller.getRecentTemps();
            assert timeWeathers != null;
            recentTimeWeather = timeWeathers.get(0);
        }
        Label labl = (Label)readingsHolder.lookup("#currentTimeLabel");
        labl.setText(recentTimeWeather.getDate());

        SplitPane tempBox = (SplitPane) readingsHolder.lookup("#temperatureBox");
        setLabel(tempBox,"#temperatureBox");
        SplitPane dewBox = (SplitPane) readingsHolder.lookup("#dewPointBox");
        setLabel(dewBox,"#dewPointBox");
        SplitPane windSpeedBox = (SplitPane) readingsHolder.lookup("#windSpeedBox");
        setLabel(windSpeedBox,"#windSpeedBox");
        SplitPane altSettings = (SplitPane) readingsHolder.lookup("#altimeterSettingsBox");
        setLabel(altSettings,"#altimeterSettingsBox");
        SplitPane windDirBox = (SplitPane) readingsHolder.lookup("#windDirectionBox");
        setLabel(windDirBox,"#windDirectionBox");
        SplitPane visibilityBox = (SplitPane) readingsHolder.lookup("#visibilityBox");
        setLabel(visibilityBox,"#visibilityBox");
        SplitPane cloudsBox = (SplitPane) readingsHolder.lookup("#cloudsBox");
        setLabel(cloudsBox,"#cloudsBox");

    }
    private static void setLabel(SplitPane weatherItemPane, String identifier)
    {
        AnchorPane itemBox= (AnchorPane) weatherItemPane.getItems().get(1);
        Label itemLabel = (Label) itemBox.getChildren().get(0);
        itemLabel.setText(retrieveValue(identifier));
    }
    private static String retrieveValue(String itemType){
        Object value = new Object();
        switch(itemType){
            case("#temperatureBox"): {value= timeWeathers.get(0).getTemp(); break;}
            case("#dewPointBox"): {value = timeWeathers.get(0).getDewPoint(); break;}
            case("#windSpeedBox"): {value= timeWeathers.get(0).getWindSpeed(); break;}
            case("#altimeterSettingsBox"): {value = timeWeathers.get(0).getAltimeterSetting(); break;}
            case("#windDirectionBox"): {value = timeWeathers.get(0).getWindDirection(); break;}
            case("#visibilityBox"): {value = timeWeathers.get(0).getMilesVisibility(); break;}
            case("#cloudsBox"): {value = timeWeathers.get(0).getClouds(); break;}
        }
        return value.toString();
    }

    private static TextInputDialog getTokenDialog(){
        TextInputDialog tokenDialog = new TextInputDialog(Controller.getToken());
        tokenDialog.setTitle("Synoptic Data API Token");
        tokenDialog.setHeaderText("Please insert Synoptic data API token.");
        return tokenDialog;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
