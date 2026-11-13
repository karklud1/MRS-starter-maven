package dk.easv.mrs.GUI.Controller;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {

    @FXML
    private ListView<Movie> lstMovies;

    @FXML
    private Button btnClick;

    @FXML
    private TextField txtTitle, txtYear, txtMovieSearch;

    private MovieModel movieModel;

    public MovieViewController() {

        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstMovies.setItems(movieModel.getObservableMovies());

        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });

        lstMovies.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
        {
            txtTitle.setText(newValue.getTitle());
            txtYear.setText(newValue.getYear() + "");
        }
        ));

    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    public void btnHandleClick(ActionEvent actionEvent) {
        String title = txtTitle.getText();
        int year = Integer.parseInt(txtYear.getText());

        Movie newMovie = new Movie(-1, year, title);

        try {
            movieModel.createMovie(newMovie);
        } catch (Exception err) {
            displayError(err);
        }

        txtTitle.clear();
        txtYear.clear();
    }

    public void btnHandleUpdate(ActionEvent actionEvent) throws Exception {

        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {

            selectedMovie.setTitle(txtTitle.getText());
            selectedMovie.setYear(Integer.parseInt(txtYear.getText()));

            try {
                movieModel.updateMovie(selectedMovie);
                lstMovies.refresh();
            } catch (Exception err) {
                displayError(err);
            }


        }
    }

    public void btnHandleDelete(ActionEvent actionEvent) throws Exception {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            try {
                movieModel.deleteMovie(selectedMovie);
            } catch (Exception err) {
                displayError(err);
            }

        }
    }
}
