package WeatherStation;

import it.sauronsoftware.cron4j.Scheduler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


public class WeatherCaller extends Application {
    private static int tester = 0;
    private static ObservableList<TimeWeather> timeWeathers;
    public static TimeWeather timeWeather;
    private static Parent root;
    private boolean tokenSet = false;
    private static Scheduler scheduler = new Scheduler();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Task<Void> tempsGetter = new Task<>() {
            @Override
            public Void call() {
                return null;
            }
        };
        try {
            timeWeather = Controller.getRecentTemps();
            tokenSet = true;
        } catch (NullPointerException nullPointerException) {
            tokenSet = false;
        }


        root = FXMLLoader.load(getClass().getResource("/WeatherReport.fxml"));
        primaryStage.setTitle("Cedar KCDC");
        //if we don't have a valid api token, must retrieve one from user.
        if (tokenSet) {
            initializeApp();
        } else {
            Controller.getTokenDialog();
            timeWeather = Controller.getRecentTemps();
            initializeApp();
        }
        //setReadings();
        //fxmlTimeWeathers.getSortOrder().add(fxmlTimeWeathers.getColumns().get(0));

        primaryStage.setScene(new Scene(root, 1400, 840));
        primaryStage.setMinWidth(1450);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void refreshData() {
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        //gridPane.setMinWidth(1400);
        HBox hBox = (HBox) gridPane.getChildren().get(0);
        VBox readingsHolder = (VBox) hBox.getChildren().get(1);

        setReadings(readingsHolder);
    }


    private static void initializeApp() {
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        HBox hBox = (HBox) gridPane.getChildren().get(0);
        VBox readingsHolder = (VBox) hBox.getChildren().get(1);

        scheduler.schedule("*/6 6-23 * * *", () -> {
            System.out.println("scheduler is working.");
            //wrapping in platform.runlater makes it work on javafx thread.
            Platform.runLater(() -> setReadings(readingsHolder));
        });
        scheduler.start();

        ToolBar toolBar = (ToolBar) root.getChildrenUnmodifiable().get(0);
        CheckBox autoRefresh = new CheckBox();
        Optional<Node> maybeAutoRefresh = toolBar.getItems().stream().filter(e -> {
            try {
                return e.getId().contains("autoRefresh");
            } catch (NullPointerException ignore) {
                return false;
            }
        }).findFirst();
        try {

            if (maybeAutoRefresh.isPresent()) {
                autoRefresh = (CheckBox) maybeAutoRefresh.get();
            }

            autoRefresh.selectedProperty().addListener((observableValue, aBoolean, newValue) -> {
                if (newValue) {
                    if (!scheduler.isStarted()) {
                        scheduler.start();
                    }
                } else {
                    if (scheduler.isStarted()) {
                        scheduler.stop();
                    }
                }
            });
        } catch (Exception ohWell) {}

        setReadings(readingsHolder);
    }

    private static void setReadings(VBox readingsHolder) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(6);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Instant currentDate = timeWeather.getDateObj().toInstant();
        //System.out.println("you're refreshing data");
        //if it has been more than 6 minutes ago, try to get a newer reading.
        if (currentDate.isBefore(offsetDateTime.toInstant())) {
            Controller.getRecentTemps(timeWeather);
            System.out.println("Fresh data was collected at: " + LocalDateTime.now().toString());
        }
        Label labl = (Label) readingsHolder.lookup("#currentTimeLabel");
        labl.textProperty().bind(timeWeather.getDate());

        SplitPane tempBox = (SplitPane) readingsHolder.lookup("#temperatureBox");
        setLabel(tempBox, "#temperatureBox");
        SplitPane dewBox = (SplitPane) readingsHolder.lookup("#dewPointBox");
        setLabel(dewBox, "#dewPointBox");
        SplitPane windSpeedBox = (SplitPane) readingsHolder.lookup("#windSpeedBox");
        setLabel(windSpeedBox, "#windSpeedBox");
        SplitPane altSettings = (SplitPane) readingsHolder.lookup("#altimeterSettingsBox");
        setLabel(altSettings, "#altimeterSettingsBox");
        SplitPane windDirBox = (SplitPane) readingsHolder.lookup("#windDirectionBox");
        setLabel(windDirBox, "#windDirectionBox");
        SplitPane visibilityBox = (SplitPane) readingsHolder.lookup("#visibilityBox");
        setLabel(visibilityBox, "#visibilityBox");
        SplitPane cloudsBox = (SplitPane) readingsHolder.lookup("#cloudsBox");
        setLabel(cloudsBox, "#cloudsBox");
        SplitPane densityAltitudeBox = (SplitPane) readingsHolder.lookup("#densityAltitudeBox");
        setLabel(densityAltitudeBox, "#densityAltitudeBox");
    }

    private static void setLabel(SplitPane weatherItemPane, String identifier) {
        AnchorPane itemBox = (AnchorPane) weatherItemPane.getItems().get(1);
        Label itemLabel = (Label) itemBox.getChildren().get(0);
        initializeValues(itemLabel);
    }

    private static void initializeValues(Label label) {
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
        }else if (label.getId().equalsIgnoreCase("densityAltitudeLabel")) {
            label.textProperty().bind(timeWeather.getDensityAltitude());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
