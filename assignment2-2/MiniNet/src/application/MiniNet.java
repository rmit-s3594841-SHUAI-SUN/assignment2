package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MiniNet extends Application {
	/**
	 * The author is Shuai Sun
	 */

	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
            Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
			Scene scene = new Scene(root,685,440);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("MiniNet");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
