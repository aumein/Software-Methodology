package songlib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Controller {

    @FXML Button save;
    @FXML Button delete;
    @FXML Button edit;
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

    // read songs from json and load into arraylist
    public void initialize() throws IOException, ParseException {
        load();

        songList.getSelectionModel().select(0);
    }

    /*
     * (1) take s and search json file for rest of data
     * (2) print data onto the current song data
     * (3) CURRENTLY only works if title + artist are available. FIX THIS
     */
    public void handleSelect(){
        String s = songList.getSelectionModel().getSelectedItem();
        System.out.println(s);
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

    // steps:
    // 1: save text into variables
    // 2: build one big string with stringbuilder separating each column with ','
    // 3: put that string into an array list
    // 4: sort array list alphabetically by song and find position of new item in array list
    // index of array list element = index it will be in songList pushing old songs up an index
    // 5: store new string into json file
    public void save(ActionEvent e){
        Button b = (Button)e.getSource();
        if (b!=save){return;}

        String songNameP = songName.getText();
        String artistP = artist.getText();

        String albumP = album.getText();
        int yearP = Integer.parseInt(year.getText());

        songList.getItems().add(songNameP + " - " + artistP);

        System.out.println(yearP + " " + songNameP + " " + artistP + " " + albumP);

        mainTab.getSelectionModel().select(0);
    }

    public void delete(ActionEvent e){
        test();
    }

    public void edit(ActionEvent e) {

    }

    // delete this when finished just using for debugging purposes
    void test() {
        System.out.println("I am a test");
    }

    // call when checking to see if song not already in list, if so then pop out saying song is on the list shows up
    boolean isInSongList(String song, String artist) {
        return false;
    }

    void readJSON(){
        JSONParser jsonparser = new JSONParser();
    }
    
    void createJson() {
        
    }
    void load() throws IOException, ParseException {
        JSONParser jsonparser = new JSONParser();
        FileReader reader = new FileReader(".\\songdata.json");
        Object obj = jsonparser.parse(reader);

        JSONObject songjsonobj = (JSONObject) obj;
        JSONArray jArray = (JSONArray) songjsonobj.get("songs");

        obsList = FXCollections.observableArrayList();
        if(jArray != null) {
            for (int i = 0; i < jArray.size(); i++) {
                JSONObject songs = (JSONObject) jArray.get(i);
                String song = (String) songs.get("song");
                String artist = (String) songs.get("artist");
                String album = (String) songs.get("album");
                String year = (String) songs.get("year");

                obsList.add(song + " - " + artist);
            }
        }


        songList.setItems(obsList);
    }
}
