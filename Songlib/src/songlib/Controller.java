package songlib;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Controller {

    @FXML Button save;
    @FXML TextField songName; 
    @FXML TextField artist; 
    @FXML TextField album;
    @FXML TextField year; 

    @FXML ListView<String> songList; //keep seperate in case we want to refactor into diff controller file
    ObservableList<String> obsList;

    public void initialize() {
        obsList = FXCollections.observableArrayList();
        songList.setItems(obsList);
    }

    public void save(ActionEvent e){
        Button b = (Button)e.getSource();
        if (b!=save){return;}

        String songNameP = songName.getText();
        String artistP = artist.getText();

        String albumP = album.getText(); 
        int yearP = Integer.parseInt(year.getText());

        songList.getItems().add(songNameP + " - "+ artistP);

        System.out.println(yearP + songNameP + artistP + albumP);
    }
}