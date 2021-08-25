package com.mixer.raw;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileHandler {
    private RandomAccessFile dbFile;
    public FileHandler(final String dbFileName) throws FileNotFoundException {
        this.dbFile = new RandomAccessFile(dbFileName, "rw");
    }

    public boolean add(String name, int age, String address, String carPlateNumber, String description) throws IOException {
        // seek to the end of the file
        this.dbFile.seek(this.dbFile.length());

        // isDeleted byte
        // record length : 1 byte
        // each field and field name

        // calculate record length
        int length = 4 + // name length
                    name.length() +
                    4 + // age
                    4 + // address length
                    address.length() +
                    4 + // carPlate length
                    carPlateNumber.length() +
                    4 + // description length
                    description.length();
        // it is deleted
        this.dbFile.writeBoolean(false);
        // record length
        this.dbFile.writeInt(length);

        // store the name
        this.dbFile.writeInt(name.length());
        this.dbFile.write(name.getBytes());

        // store age
        this.dbFile.writeInt(age);

        // store the address
        this.dbFile.writeInt(address.length());
        this.dbFile.write(address.getBytes());

        // store the carPlateNumber
        this.dbFile.writeInt(carPlateNumber.length());
        this.dbFile.write(carPlateNumber.getBytes());

        // store the description
        this.dbFile.writeInt(description.length());
        this.dbFile.write(description.getBytes());

        return true;
    }

    public Person readRow(int rowNumber) throws IOException {
        byte[] row = this.readRowRecord(rowNumber);
        Person person = new Person();
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(row));

        int nameLength = stream.readInt();
        byte[] b = new byte[nameLength];
        stream.read(b);
        person.name = new String(b);

        // age
        person.age = stream.readInt();

        // address
        b = new byte[stream.readInt()];
        stream.read(b);
        person.address = new String(b);

        // carPlateNumber
        b = new byte[stream.readInt()];
        stream.read(b);
        person.carPlateNumber = new String(b);

        // description
        b = new byte[stream.readInt()];
        stream.read(b);
        person.description = new String(b);

        return person;
    }

    private byte[] readRowRecord(int rowNumber) throws IOException {
        this.dbFile.seek(0);
        if (this.dbFile.readBoolean()) {
            return new byte[0];
        }
        this.dbFile.seek(rowNumber + 1);
        int recordLength = this.dbFile.readInt();
        this.dbFile.seek(rowNumber + 5);

        byte[] data = new byte[recordLength];
        this.dbFile.read(data);

        return data;
    }

    public void close() throws IOException {
        this.dbFile.close();
    }
}
