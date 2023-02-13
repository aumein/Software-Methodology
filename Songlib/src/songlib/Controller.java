package songlib;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class Controller {

    @FXML Button save;
    @FXML TextField songName; 
    @FXML TextField artist; 
    @FXML TextField album;
    @FXML TextField year; 

    @FXML Label titleLabel;
    @FXML Label artistLabel;
    @FXML Label yearLabel;
    @FXML Label albumLabel;

    @FXML TabPane mainTab;

    @FXML ListView<String> songList;
    ObservableList<String> obsList;

    /*
     * (1) make obsList read from json
     */
    public void initialize() {
        obsList = FXCollections.observableArrayList("hello - world");
        songList.setItems(obsList);

        songList.getSelectionModel().select(0);
    }

    /*
     * (1) take s and search json file for rest of data
     * (2) print data onto the current song data
     * (3) CURRENTLY only works if title + artist are available. FIX THIS
     */
    public void handleSelect(){
        String s = songList.getSelectionModel().getSelectedItem();
        String[] params = s.split(" - ");
        String title = params[0];
        String name = params[1];

        //get year and album if available
        titleLabel.setText(title);
        artistLabel.setText(name);

        mainTab.getSelectionModel().select(0);
    }

    /*
     * saves whatever is in text fields to list. NEEDS STILL:
     * (1) saves to a json
     * (2) album and year should be optional
     */
    public void save(ActionEvent e){
        Button b = (Button)e.getSource();
        if (b!=save){return;}

        String songNameP = songName.getText();
        String artistP = artist.getText();

        String albumP = album.getText(); 
        int yearP = Integer.parseInt(year.getText());

        songList.getItems().add(songNameP + " - "+ artistP);

        System.out.println(yearP + songNameP + artistP + albumP);

        mainTab.getSelectionModel().select(0);
    }
}