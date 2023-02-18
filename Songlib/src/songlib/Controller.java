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

import java.io.*;
import java.util.List;

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
        createJson();
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
    public void save(ActionEvent e) throws IOException, ParseException {
        Button b = (Button)e.getSource();
        if (b!=save){return;}

        String songNameP = songName.getText();
        String artistP = artist.getText();

        String albumP = album.getText();
        String yearP = year.getText();

        writeToSongDataJSON(artistP, songNameP, albumP, yearP);
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

    // call when checking to see if song is already in list, if so then display pop oup saying song is already on the list
    boolean isInSongList(String song, String artist) {
        return false;
    }

    void createJson() throws IOException {
        File songDataFile = new File(".\\songdata.json");
        boolean isNewFile = songDataFile.createNewFile();

        if(isNewFile) {
            JSONObject json = new JSONObject();

            try {
                json.put("songs", List.of());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try (PrintWriter out = new PrintWriter(new FileWriter(".\\songdata.json"))) {
                out.write(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    void writeToSongDataJSON(String song, String artist, String album, String year) throws IOException, ParseException {
        JSONParser jsonparser = new JSONParser();
        FileReader reader = new FileReader(".\\songdata.json");
        Object obj = jsonparser.parse(reader);

        JSONObject songjsonobj = (JSONObject) obj;
        JSONArray arr = (JSONArray) songjsonobj.get("songs");

        JSONObject newSong = new JSONObject();
        newSong.put("song", song);
        newSong.put("artist", artist);
        newSong.put("album", album);
        newSong.put("year", year);

        arr.add(newSong);
        JSONArray newArray = arr;
        JSONObject newJson = (JSONObject) songjsonobj.put("songs", newArray);

//        String newJsonStr = newJson.toString();

//        try (PrintWriter out = new PrintWriter(new FileWriter(".\\songdata.json"))) {
//            out.write(newJsonStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
    void load() throws IOException, ParseException {
        try {
            JSONParser jsonparser = new JSONParser();
            FileReader reader = new FileReader(".\\songdata.json");
            Object obj = jsonparser.parse(reader);

            JSONObject songjsonobj = (JSONObject) obj;
            JSONArray jArray = (JSONArray) songjsonobj.get("songs");

            obsList = FXCollections.observableArrayList();
            if (jArray != null) {
                for (int i = 0; i < jArray.size(); i++) {
                    JSONObject songs = (JSONObject) jArray.get(i);
                    String song = (String) songs.get("song");
                    String artist = (String) songs.get("artist");
                    String album = (String) songs.get("album");
                    String year = (String) songs.get("year");

                    obsList.add(song + " - " + artist);
                }
            }
        }catch(Exception e) {
            System.out.println("File not created properly");
        }


        songList.setItems(obsList);
    }
}

class Song {

    private String song;
    private String artist;
    private String album;
    private String year;

    Song(String song, String artist, String album, String year) {
        this.song = song;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
