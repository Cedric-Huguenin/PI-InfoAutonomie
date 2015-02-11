package model;

import com.avaje.ebean.Ebean;
import model.BasicEvent;
import models.*;
import org.junit.*;
import static org.junit.Assert.*;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.List;

import static play.test.Helpers.*;

public class ApplicationTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
    }

    @Test
    public void createAndRetrieveUser() {
        Ebean.save((List) Yaml.load("test-data.yml"));

        BasicEvent basicEvent = BasicEvent.find.byId("Saut de luminosité");
        assertEquals("Bob", "Bob");
        assertEquals("161.24", basicEvent.getSensor().getName());
        assertEquals("161", basicEvent.getSensor().getName());
    }
}