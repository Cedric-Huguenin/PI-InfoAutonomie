package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DataNode;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.raw_values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result data() {
        // url to parse: http://iotlab.telecomnancy.eu/rest/data/1/light1/1

        // fetch the json data
        String url = "http://iotlab.telecomnancy.eu/rest/data/1/light1/1";
        StringBuffer result = new StringBuffer();
        DataNode dataNode = new DataNode();
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
            System.out.println(inputLine);
            in.close();

            // map the json data in a DataNode
            ObjectMapper mapper = new ObjectMapper();
            dataNode = mapper.readValue(result.toString(), DataNode.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok(raw_values.render("Your new application is ready.", dataNode.getData()));
    }

}
