package com.experiment.comp.application_house_rent_bd;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Created by comp on 7/4/2015.
 */
public class DateTimeHelper {
    public static Timestamp StringToDate(String string)
    {
        Date date;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss");

        try {
            String s = string.replaceAll("\\s", "");
            //Log.d("Shout", s);
            date =  df.parse(s);
        } catch (Exception e) {
            Log.d("Shout","Failed to parse date: ");
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String st = writer.toString();
            Log.d("Shout", st);
            throw new RuntimeException("Failed to parse date: ", e);
        }

        Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        return timestamp;

    }
}
