import com.fasterxml.jackson.databind.ObjectMapper;
import model.BasicEvent;
import model.Event;
import model.json.Data;
import model.json.DataNode;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mathieu on 22/02/2015.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {

        // Execute once
        Akka.system().scheduler().scheduleOnce(
                Duration.create(5, TimeUnit.SECONDS),
                () -> {
                    Logger.info("ON START ---    " + System.currentTimeMillis());
                    System.out.println("ON START ---    " + System.currentTimeMillis());
//                    try {
//                        URL url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/light1/300/1425740196/1425826596");
//                        ObjectMapper mapper = new ObjectMapper();
//                        DataNode rawDataNode = mapper.readValue(url, DataNode.class);
//                        for(Data data : rawDataNode.getData()) {
//                            model.Data persistData = new model.Data(data.getTimestamp(), data.getValue(), data.getLabel(), data.getMote()+".LIGHT");
//
//                            System.out.println(persistData.toString());
//                            model.Data.create(persistData);
//                        }
//                    } catch (IOException e) {
//
//                    }
                },
                Akka.system().dispatcher()
        );

        // Execute every x seconds
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
                Duration.create(10, TimeUnit.MINUTES),     //Frequency 10 seconds
//                    Duration.create(nextExecutionInSeconds(8, 0), TimeUnit.SECONDS),
//                    Duration.create(24, TimeUnit.HOURS),
                () -> { // Runnable

                    // check for new BasicEvent occurrences
                    System.out.println("BasicEvent : " + BasicEvent.all().size());
                    BasicEvent.all().forEach(model.BasicEvent::check);

                    // based on BasicEventOccurrences, check if Event occurs
                    Event.all().forEach(model.Event::check);

                },
                Akka.system().dispatcher()
        );
    }

//    public static int nextExecutionInSeconds(int hour, int minute) {
//        return Seconds.secondsBetween(
//                new DateTime(),
//                nextExecution(hour, minute)
//        ).getSeconds();
//    }
//
//    public static DateTime nextExecution(int hour, int minute) {
//        DateTime next = new DateTime()
//                .withHourOfDay(hour)
//                .withMinuteOfHour(minute)
//                .withSecondOfMinute(0)
//                .withMillisOfSecond(0);
//
//        return (next.isBeforeNow())
//                ? next.plusHours(24)
//                : next;
//    }
}
