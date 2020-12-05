package WeatherStation;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherCaller extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ObservableList<TimeWeather> timeWeathers = Controller.getRecentTemps();

        Parent root =  FXMLLoader.load(getClass().getResource("/WeatherReport.fxml"));

        primaryStage.setTitle("Cedar KCDC");
        TableView<TimeWeather> fxmlTimeWeathers = (TableView<TimeWeather>)root.getChildrenUnmodifiable().get(1);
        fxmlTimeWeathers.setItems(timeWeathers);
        fxmlTimeWeathers.getColumns().get(0).setSortType(TableColumn.SortType.DESCENDING);

        fxmlTimeWeathers.getSortOrder().add(fxmlTimeWeathers.getColumns().get(0));
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
