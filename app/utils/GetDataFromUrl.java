package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.json.DataNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

/**
 * Created by Ced on 01/02/2015.
 */
public class GetDataFromUrl {

    public static DataNode getFromUrl(String url) throws IOException {
        StringBuffer result = new StringBuffer();
        DataNode dataNode = new DataNode();
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

        Collections.reverse(dataNode.getData());

        return dataNode;
    }
}
