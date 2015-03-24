package model;

import com.avaje.ebean.Ebean;
import play.db.ebean.*;
import model.BasicEvent;
import models.*;
import org.junit.*;
import static org.junit.Assert.*;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.List;

import static play.test.Helpers.*;

public class ApplicationTest extends WithApplication {

    @Test
    public void testDatabase() {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

//            BasicEvent basicEvent = BasicEvent.find.byId("light_delta_15");
//            assertEquals("Light Sensor", basicEvent.getSensor().getName());
        });
    }
}