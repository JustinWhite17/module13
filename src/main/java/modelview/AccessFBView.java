package modelview;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.mycompany.mvvmexample.App;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Person;

public class AccessFBView {
    @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;

    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

    @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

    @FXML
    private void regRecord(ActionEvent event) {
        registerUser();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("WebContainer.fxml");
    }

    public void addData() {
        Firestore firestore = App.fstore;
        DocumentReference docRef = firestore.collection("References").document(UUID.randomUUID().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));

        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public boolean readFirebase() {
        Firestore firestore = App.fstore;
        ApiFuture<QuerySnapshot> future = firestore.collection("References").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                outputField.appendText(document.getData().get("Name") + " , Major: " +
                        document.getData().get("Major") + " , Age: " +
                        document.getData().get("Age") + "\n");
                person = new Person(String.valueOf(document.getData().get("Name")),
                        document.getData().get("Major").toString(),
                        Integer.parseInt(document.getData().get("Age").toString()));
                listOfUsers.add(person);
            }
            return true;
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean registerUser() {
        FirebaseAuth fauth = App.fauth;
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@example.com") // Replace with actual user input
                .setEmailVerified(false)
                .setPassword("secretPassword") // Replace with actual user input
                .setDisplayName("John Doe") // Replace with actual user input
                .setDisabled(false);

        try {
            UserRecord userRecord = fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

