package com.netcracker.education.cache.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimeZone;

/**
 * Scheduler
 */
//
public class TimeCorrector {
    public String getTimeZone(){
        String timezone = "";
        try {
            Process p = Runtime.getRuntime().exec("wmic.exe TimeZone get /value");
            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=reader.readLine();
            while(line!=null)
            {
                line = reader.readLine();
                if (line.contains("Caption")) {
                    timezone = line;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timezone;
    }

    private void setCorrectTime() {
        String timezone = getTimeZone();
        if (timezone.contains("RTZ"))
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
    }
}
