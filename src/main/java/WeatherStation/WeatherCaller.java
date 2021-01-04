package WeatherStation;

import it.sauronsoftware.cron4j.Scheduler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class WeatherCaller extends Application {
    private static int tester = 0;
    private static ObservableList<TimeWeather> timeWeathers;
    public static TimeWeather timeWeather;
    private static Parent root;
    private boolean tokenSet = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Task<Void> tempsGetter = new Task<>() {
            @Override
            public Void call() {
            return null;
            }
        };
        try {
            timeWeather = Controller.getRecentTemps();
            tokenSet=true;
        }catch(NullPointerException nullPointerException){
            tokenSet=false;
        }


        root =  FXMLLoader.load(getClass().getResource("/WeatherReport.fxml"));
        primaryStage.setTitle("Cedar KCDC");
        //if we don't have a valid api token, must retrieve one from user.
        if(tokenSet) {
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        HBox hBox = (HBox) gridPane.getChildren().get(0);
        VBox readingsHolder = (VBox) hBox.getChildren().get(1);
            Scheduler scheduler = new Scheduler();
            scheduler.schedule("0/6 6-23 * * 1-5", () -> {
                System.out.println("scheduler is working.");
                //wrapping in platform.runlater makes it work on javafx thread.
                Platform.runLater(() -> setReadings(readingsHolder));

            });
            scheduler.start();

            setReadings(readingsHolder);
            //TableView<TimeWeather> fxmlTimeWeathers = (TableView<TimeWeather>) gridPane.getChildren().get(1);
            //fxmlTimeWeathers.setFixedCellSize(60);
            //fxmlTimeWeathers.prefHeightProperty().bind(Bindings.size(fxmlTimeWeathers.getItems()).multiply(fxmlTimeWeathers.getFixedCellSize()).add(80));
            //fxmlTimeWeathers.setItems(timeWeathers);
            //fxmlTimeWeathers.getColumns().get(0).setSortType(TableColumn.SortType.DESCENDING);
        }
        else{
            Controller.getTokenDialog();
        }
        //setReadings();
        //fxmlTimeWeathers.getSortOrder().add(fxmlTimeWeathers.getColumns().get(0));

        primaryStage.setScene(new Scene(root, 1400, 840));
        primaryStage.setMinWidth(1450);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }
    public static void refreshData()
    {
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        //gridPane.setMinWidth(1400);
        HBox hBox = (HBox) gridPane.getChildren().get(0);
        VBox readingsHolder = (VBox) hBox.getChildren().get(1);

        setReadings(readingsHolder);
    }
    private static void setReadings(VBox readingsHolder){
        //Confusing, but to make calculation work, have to change to UTC time zone.
        LocalDateTime sixMinAgo = LocalDateTime.now().minusMinutes(6);
        sixMinAgo = sixMinAgo.plusHours(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Instant currentDate = timeWeather.getDateObj().toInstant();
        //System.out.println("you're refreshing data");
        //if it has been more than 6 minutes ago, try to get a newer reading.
        if(currentDate.isBefore(sixMinAgo.toInstant(ZoneOffset.UTC))){
            Controller.getRecentTemps(timeWeather);
            System.out.println("Fresh data was collected. D:");
        }
        Label labl = (Label)readingsHolder.lookup("#currentTimeLabel");
        labl.textProperty().bind(timeWeather.getDate());

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
        initializeValues(itemLabel);
    }
    private static void initializeValues(Label label){
        if (label.getId().equalsIgnoreCase("temperatureLabel")) {
            label.textProperty().bind(timeWeather.getTemp());
        } else if (label.getId().equalsIgnoreCase("dewPointLabel")) {
            label.textProperty().bind(timeWeather.getDewPoint());
        } else if (label.getId().equalsIgnoreCase("windSpeedLabel")) {
            label.textProperty().bind(timeWeather.getWindSpeed());
        } else if (label.getId().equalsIgnoreCase("altimeterSettingsLabel")) {
            label.textProperty().bind(timeWeather.getAltimeterSetting());
        } else if (label.getId().equalsIgnoreCase("windDirectionLabel")) {
            label.textProperty().bind(timeWeather.getWindDirection());
        } else if (label.getId().equalsIgnoreCase("visibilityLabel")) {
            label.textProperty().bind(timeWeather.getMilesVisibility());
        } else if (label.getId().equalsIgnoreCase("cloudsLabel")) {
            label.textProperty().bind(timeWeather.getClouds());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
