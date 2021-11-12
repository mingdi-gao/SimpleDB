package com.mixer.testapp;

import com.mixer.dbserver.DB;
import com.mixer.dbserver.DBServer;
import com.mixer.raw.FileHandler;
import com.mixer.raw.Index;
import com.mixer.raw.Person;

import java.io.IOException;

public class TestApp {
    public static void main(String[] args)  {

        try {
            final String dbFile = "Dbserver.db";
            DB db = new DBServer(dbFile);
            db.add("John", 44, "Berlin", "www-404", "This is a description");
            db.close();

            db = new DBServer(dbFile);
            Person person = db.read(0);


            System.out.println("Total number of rows in database: " + Index.getInstance().getTotalRowNumber());
            System.out.println(person);

            System.out.println(Index.getInstance().getTotalRowNumber());
            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
