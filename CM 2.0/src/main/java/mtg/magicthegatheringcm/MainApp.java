package mtg.magicthegatheringcm;

import db.dbmanager.IPersistence;
import db.dbmanager.SQLITEPersistence;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application
{

    private static final ExecutorService THREADPOOL_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static Properties settings;

    public static ExecutorService getThreadpool()
    {
        return THREADPOOL_EXECUTOR_SERVICE;
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/CardOverview.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Magic: The Gathering - Collection Manager");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                File propertyFile = new File(Constants.PROPERTY_SETTINGS_PATH);
                propertyFile.mkdir();
                propertyFile = new File(Constants.PROPERTY_SETTINGS_PATH + Constants.PROPERTY_SETTINGS_NAME);
                try (FileOutputStream fos = new FileOutputStream(propertyFile))
                {
                    settings.store(fos, "User settings");
                    System.out.println("User settings saved...");
                }
                catch (Exception ex)
                {
                    System.err.println("User settings not saved...");
                }

                System.exit(0);
            }
        });

        getPropertySettings();
        initializeDatabase();

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    public static Properties getPropertySettings() throws IOException
    {
        // Try to read property file
        // Else create a propery file
        try
        {
            File propertyFile = new File(Constants.PROPERTY_SETTINGS_PATH + Constants.PROPERTY_SETTINGS_NAME);
            FileInputStream fis = new FileInputStream(propertyFile);
            settings = new Properties();
            settings.load(fis);
            System.out.println("Loaded user settings...");
            return settings;
        }
        catch (FileNotFoundException FNFE)
        {
            // Creating new Properties file
            System.out.println("No user settings found...");
            System.out.println("Creating default user settings");
            settings = new Properties();
            settings.setProperty("cashImages", "false");
            return settings;
        }
        catch (IOException IOE)
        {
            throw IOE;
        }
    }

    private void initializeDatabase() throws SQLException, IOException, FileNotFoundException, ClassNotFoundException
    {
        IPersistence database = new SQLITEPersistence();
        database.init(null);
    }
}
