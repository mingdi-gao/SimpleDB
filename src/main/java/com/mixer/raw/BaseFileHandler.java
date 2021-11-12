package com.mixer.raw;

import com.mixer.util.DebugRowInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BaseFileHandler {
    RandomAccessFile dbFile;

    public BaseFileHandler(final String dbFileName) throws FileNotFoundException {
        this.dbFile = new RandomAccessFile(dbFileName, "rw");
    }

    public void loadAllDataToIndex() throws IOException {
        if (this.dbFile.length() == 0) {
            return ;
        }
        // count unit is byte
        long currentPos = 0;
        long rowNum = 0;
        long deletedRows = 0;

        while(currentPos < this.dbFile.length()) {
            // move filePtr to start position of next record
            this.dbFile.seek(currentPos);
            // first char represents whether this record is deleted or not
            boolean isDeleted = this.dbFile.readBoolean();
            if (!isDeleted) {
                Index.getInstance().add(currentPos);
            } else {
                deletedRows++;
            }
            currentPos += 1;
            this.dbFile.seek(currentPos);
            int recordLength = this.dbFile.readInt();
            currentPos +=4;
            this.dbFile.seek(currentPos);

            if (!isDeleted) {
                byte[] b = new byte[recordLength];
                this.dbFile.read(b);
                Person p = this.readFromByteStream(new DataInputStream(new ByteArrayInputStream(b)));
                Index.getInstance().addNameToIndex(p.name, rowNum);
                rowNum++;
            }

            currentPos += recordLength;
        }

        System.out.println("After startup: total row number in Database and Index: " + rowNum);
        System.out.println("After startup: total deleted row number in Database and Index: " + deletedRows);
    }
    // read raw data, return as bytes[]
    public byte[] readRawRecord(long bytePositionOfRow) throws IOException {
        this.dbFile.seek(bytePositionOfRow);
        if (this.dbFile.readBoolean()) {
            return new byte[0];
        }
        this.dbFile.seek(bytePositionOfRow + 1);
        int recordLength = this.dbFile.readInt();
        this.dbFile.seek(bytePositionOfRow + 5);

        byte[] data = new byte[recordLength];
        this.dbFile.read(data);

        return data;
    }

    public Person readFromByteStream(final DataInputStream stream) throws IOException{
        Person person = new Person();

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


    public void close() throws IOException {
        this.dbFile.close();
    }

    public List<DebugRowInfo> loadAllDataFromFile() throws IOException{
        if (this.dbFile.length() == 0) {
            return new ArrayList<>();
        }
        ArrayList<DebugRowInfo> result = new ArrayList<>();
        long currentPosition = 0;

        while (currentPosition < this.dbFile.length()) {
            this.dbFile.seek(currentPosition);
            boolean isDeleted = this.dbFile.readBoolean();
            currentPosition += 1;
            int recordLength = this.dbFile.readInt();
            currentPosition += 4;
            byte[] b = new byte[recordLength];
            this.dbFile.read(b);
            Person p = this.readFromByteStream(new DataInputStream(new ByteArrayInputStream(b)));
            result.add(new DebugRowInfo(p, isDeleted));
            currentPosition += recordLength;
        }
        return result;
    }
}
