package javaSteps.steps.Media;

import akka.http.javadsl.model.MediaType;
import akka.protobuf.ByteString;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import cucumber.api.java.bs.A;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import javafx.util.Pair;
import play.libs.Files;
import play.mvc.Http;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UploadMediaSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();



    @When("I want to upload media")
    public void iWantToUploadMedia() {
        state.getRequest().uri((String.format("http://localhost:9000/api/users/%s/albums/%s", state.getUser().getId(), state.getAlbum().getId())));
        state.getRequest().method("POST");
    }

    @Then("The media exists")
    public void theMediaExists() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("I select a valid media file")
    public void i_select_a_valid_media_file() {
        File new_media = new File("../../../resources/images/testMedia.jpg");

        List<Http.MultipartFormData.Part<Source<ByteString, ?>>> partList = new ArrayList<>();


        partList.add(new Http.MultipartFormData.FilePart<>("picture", "test.jpg", "image/jpg",
                FileIO.fromPath(new_media.toPath()),
                "form-data"));


        state.getRequest().bodyMultipart(partList,
                play.libs.Files.singletonTemporaryFileCreator(),
                );


}
