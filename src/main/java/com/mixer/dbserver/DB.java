package com.mixer.dbserver;

import com.mixer.exceptions.DuplicateNameException;
import com.mixer.raw.Person;

import java.io.Closeable;
import java.io.IOException;

public interface DB extends Closeable {
     void add(Person person) throws IOException, DuplicateNameException;
     void delete(long rowNumber) throws IOException;

     void update(String name, final Person person) throws IOException, DuplicateNameException;
     void update(long rowNumber, final Person person) throws IOException, DuplicateNameException;
     Person read(long rowNumber) throws IOException;

     void close() throws IOException;
}
