package com.mycompany.mvvmexample;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.auth.oauth2.GoogleCredentials;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Firestore firestore;
    public static FirebaseAuth firebaseAuth;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        initializeFirebase();
        scene = new Scene(loadFXML("AccessFBView"));
        stage.setScene(scene);
        stage.show();
    }

    private void initializeFirebase() {
        try {
            // Load Firebase configuration from a file or resource
            InputStream serviceAccount = getClass().getResourceAsStream("/path/to/your/firebase/key.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            firestore = FirestoreClient.getFirestore();
            firebaseAuth = FirebaseAuth.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle initialization errors here
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}

