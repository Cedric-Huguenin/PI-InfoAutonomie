package controllers;

import model.BasicEvent;
import model.BasicEventOccurrence;
import model.TimeInterval;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Ced on 03/03/2015.
 */
public class BasicEventOccurrenceController {

    public boolean occur(TimeInterval t, BasicEvent basicEvent) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(System.currentTimeMillis());

        GregorianCalendar tmp = new GregorianCalendar();
        tmp.setTimeInMillis(t.getTimestampStart()*1000);

        GregorianCalendar begin = new GregorianCalendar();
        begin.setTimeInMillis(System.currentTimeMillis());
        begin.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
        begin.set(Calendar.SECOND, tmp.get(Calendar.SECOND));

        tmp.setTimeInMillis(t.getTimestampEnd()*1000);

        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(System.currentTimeMillis());
        end.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
        end.set(Calendar.SECOND, tmp.get(Calendar.SECOND));


        begin.add(Calendar.DAY_OF_YEAR, -1);
        end.add(Calendar.DAY_OF_YEAR, -1);

        if(end.before(begin)) { //  if end is 8:00 and begin is 23:00 for example
            end.add(Calendar.DAY_OF_YEAR, 1);
        }

//        System.out.println("From " +  (begin.getTimeInMillis()/1000) + " to " +  (end.getTimeInMillis()/1000));

//        System.out.println("SEARCHING FOR " + basicEvent.getId());
        String basicEventId = basicEvent.getId();
        List<BasicEventOccurrence> basicsEventOccurrences = BasicEventOccurrence.find.where()
                .between("timestamp", begin.getTimeInMillis() / 1000, end.getTimeInMillis() / 1000).eq("basic_event_id", basicEventId).findList();

        System.out.println("Found " + basicsEventOccurrences.size() + " item(s) ");


        return basicsEventOccurrences.size() > 0;
    }
}
