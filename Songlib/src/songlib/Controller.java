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
import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<String> songsArrayList;
    ArrayList<String> artistArrayList;

    /*
     * (1) make obsList read from json
     */
    public void initialize() throws IOException, ParseException {
        createJson();
        load();
//        sortListView();

        songList.getSelectionModel().select(0);
    }

    /*
     * (1) take s and search json file for rest of data
     * (2) print data onto the current song data
     * (3) CURRENTLY only works if title + artist are available. FIX THIS
     */
    public void handleSelect() throws IOException, ParseException {
        fillCurrentSongText();

        mainTab.getSelectionModel().select(0);
    }

    void fillCurrentSongText() throws IOException, ParseException {
        String s = songList.getSelectionModel().getSelectedItem();
        System.out.println(s);
        String[] params = s.split(" - ");
        String song = params[0];
        String artist = params[1];

        JSONParser jsonparser = new JSONParser();
        FileReader reader = new FileReader(".\\songdata.json");
        Object obj = jsonparser.parse(reader);

        JSONObject songjsonobj = (JSONObject) obj;
        JSONArray arr = (JSONArray) songjsonobj.get("songs");

        for(int i = 0; i < arr.size(); i++) {
            JSONObject songObj = (JSONObject) arr.get(i);
            if(song.equals((String) songObj.get("song")) && artist.equals(songObj.get("artist"))){
                titleLabel.setText(song);
                artistLabel.setText(artist);
                yearLabel.setText((String) songObj.get("year"));
                albumLabel.setText((String) songObj.get("album"));
            }
        }
    }

    /*
     * saves whatever is in text fields to list. NEEDS STILL:
     * (1) saves to a json
     * (2) album and year should be optional
     */

    public void save(ActionEvent e) throws IOException, ParseException {
        Button b = (Button)e.getSource();
        if (b!=save){return;}



        String songNameP = songName.getText();
        String artistP = artist.getText();

        String albumP = album.getText();
        String yearP = year.getText();

        if(songNameP == "" || artistP == "") {
            System.out.println("empty");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Artist/Song Fields Missing");
            alert.setContentText("Please enter an Artist and a Song");
            alert.show();
            return;
        }

        writeToSongDataJSON(songNameP, artistP, albumP, yearP);
        sortListView();
        songName.setText(null);
        artist.setText(null);
        album.setText(null);
        year.setText(null);
    }

    public void delete(ActionEvent e) throws IOException, ParseException {
        String s = songList.getSelectionModel().getSelectedItem();
        System.out.println(s);
        String[] params = s.split(" - ");
        String song = params[0];
        String artist = params[1];
        System.out.println("delete " + song + " " + artist);
        deleteFromJSON(song, artist);
    }

    void deleteFromJSON(String songToDelete, String artistOfSong) throws IOException, ParseException {
        JSONParser jsonparser = new JSONParser();
        FileReader reader = new FileReader(".\\songdata.json");
        Object obj = jsonparser.parse(reader);

        JSONObject songjsonobj = (JSONObject) obj;
        JSONArray arr = (JSONArray) songjsonobj.get("songs");

        for(int i = 0; i < arr.size(); i++) {
            JSONObject song = (JSONObject) arr.get(i);
            if (songToDelete.equalsIgnoreCase((String) song.get("song")) && artistOfSong.equalsIgnoreCase((String) song.get("artist"))){
                arr.remove(i);
                break;
            }
        }

//        ((JSONArray) songjsonobj.get("songs")).clear();
        songjsonobj.put("songs", arr);

        try (PrintWriter out = new PrintWriter(new FileWriter(".\\songdata.json"))) {
            out.write(songjsonobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sortListView();
    }

    public void edit(ActionEvent e) {

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

        for(int i = 0; i < arr.size(); i++) {
            JSONObject songObj = (JSONObject) arr.get(i);
            if(song.equalsIgnoreCase((String) songObj.get("song")) &&
                    artist.equalsIgnoreCase((String) songObj.get("artist"))){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Song Already Exists");
                alert.setContentText("Song is in list already! Please enter a new Song!");
                alert.show();
                return;
            }
        }

        JSONObject newSong = new JSONObject();
        newSong.put("song", song);
        newSong.put("artist", artist);
        newSong.put("album", album);
        newSong.put("year", year);

        arr.add(newSong);
        JSONArray newArray = arr;
        System.out.println(newArray);
        System.out.println(songjsonobj);

        try (PrintWriter out = new PrintWriter(new FileWriter(".\\songdata.json"))) {
            out.write(songjsonobj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        songList.getSelectionModel().select(song + " - " + artist);
        titleLabel.setText(song);
        artistLabel.setText(artist);
        yearLabel.setText(year);
        albumLabel.setText(album);

        mainTab.getSelectionModel().select(0);

    }

    void sortListView() throws IOException, ParseException {
        songList.getItems().clear();
        songsArrayList = new ArrayList<>();
        artistArrayList = new ArrayList<>();
        obsList.clear();

        JSONParser jsonparser = new JSONParser();
        FileReader reader = new FileReader(".\\songdata.json");
        Object obj = jsonparser.parse(reader);

        JSONObject songsobj = (JSONObject) obj;

        JSONArray array = (JSONArray) songsobj.get("songs");

        for(int i = 0; i < array.size(); i++) {
            JSONObject song = (JSONObject) array.get(i);
            songsArrayList.add((String) song.get("song"));
            artistArrayList.add((String) song.get("artist"));
        }

        Collections.sort(songsArrayList);
        Collections.sort(artistArrayList);

        for(int i = 0; i < songsArrayList.size(); i++) {
            for(int j = 0; j < songsArrayList.size(); j++) {
                JSONObject song = (JSONObject) array.get(j);
                System.out.println(songsArrayList.get(i) + " == " + song.get("song") + ", " + artistArrayList.get(j) + " == " + song.get("artist"));
                if(songsArrayList.get(i).equalsIgnoreCase((String) song.get("song")) && !obsList.contains(songsArrayList.get(i) + " - " + song.get("artist"))){
                    songList.getItems().add(songsArrayList.get(i) + " - " + song.get("artist"));
                    obsList.add(songsArrayList.get(i) + " - " + song.get("artist"));
                    System.out.println("songs and artist check out");
                    break;
                }
            }

        }

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
                }
            }
        }catch(Exception e) {
            System.out.println("File not created properly");
        }

        sortListView();
    }
}
