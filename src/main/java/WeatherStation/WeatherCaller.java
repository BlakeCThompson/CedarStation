package WeatherStation;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WeatherCaller extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ObservableList<TimeWeather> timeWeathers = Controller.getRecentTemps();
        Parent root =  FXMLLoader.load(getClass().getResource("/WeatherReport.fxml"));
        primaryStage.setTitle("Cedar KCDC");
        GridPane gridPane = (GridPane) root.getChildrenUnmodifiable().get(1);
        //gridPane.setMinWidth(1400);
        TableView<TimeWeather> fxmlTimeWeathers = (TableView<TimeWeather>) gridPane.getChildren().get(1);
        fxmlTimeWeathers.setFixedCellSize(60);
        fxmlTimeWeathers.prefHeightProperty().bind(Bindings.size(fxmlTimeWeathers.getItems()).multiply(fxmlTimeWeathers.getFixedCellSize()).add(80));
        fxmlTimeWeathers.setItems(timeWeathers);
        fxmlTimeWeathers.getColumns().get(0).setSortType(TableColumn.SortType.DESCENDING);

        //fxmlTimeWeathers.getSortOrder().add(fxmlTimeWeathers.getColumns().get(0));

        primaryStage.setScene(new Scene(root, 1400, 840));
        primaryStage.setMinWidth(1150);
        primaryStage.setMinHeight(565);
        primaryStage.show();
    }

    private static void setReadings(){

    }


    public static void main(String[] args) {
        launch(args);
    }
}
