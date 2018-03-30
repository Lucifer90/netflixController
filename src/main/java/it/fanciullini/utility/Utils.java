package it.fanciullini.utility;

import org.joda.time.DateTime;

public class Utils {


    public static Long calculateDifference(DateTime lastSubmissionDate){
        DateTime now = new DateTime();
        return now.getMillis() - lastSubmissionDate.getMillis();
    }

}
