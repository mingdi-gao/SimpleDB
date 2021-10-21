package com.mixer.testapp;

import com.mixer.raw.FileHandler;
import com.mixer.raw.Index;
import com.mixer.raw.Person;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args)  {

        try {
            FileHandler fh = new FileHandler("Dbserver.db");
            fh.add("John", 44, "Berlin", "www-404", "This is a description");
            fh.close();

            fh = new FileHandler("Dbserver.db");
            Person person = fh.readRow(0);
            fh.close();

            System.out.println("Total number of rows in database: " + Index.getInstance().getTotalRowNumber());
            System.out.println(person);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
