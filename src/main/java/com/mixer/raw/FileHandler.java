package com.mixer.raw;

import com.mixer.exceptions.DuplicateNameException;

import java.io.*;

public class FileHandler extends BaseFileHandler {

    public FileHandler(final String dbFileName) throws FileNotFoundException {
        super(dbFileName);
    }

    // add record, column by column
    public boolean add(String name, int age, String address, String carPlateNumber, String description) throws IOException, DuplicateNameException {
        if (Index.getInstance().hasNameInIndex(name)) {
            throw new DuplicateNameException(String.format("Name '%s' already exists!", name));
        }

        // seek to the end of the file
        long currentPositionToInsert = this.dbFile.length();
        this.dbFile.seek(currentPositionToInsert);

        // isDeleted byte
        // record length : 1 byte
        // each field and field name

        /**
         * SO THE RECORD IS COMPOSED LIKE:
         * 4, name, age, address, carPlage, carPlateNumber, dex
         */
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

        // add start Position of current record into Index
        Index.getInstance().add(currentPositionToInsert);
        Index.getInstance().addNameToIndex(name, Index.getInstance().getTotalRowNumber() - 1);

        return true;
    }
    // read a row based on row number, return as Person Object
    public Person readRow(long rowNumber) throws IOException {
        long bytePosition = Index.getInstance().getBytePosition(rowNumber);
        // no such rowNumber in map
        if (bytePosition == -1) {
            return null;
        }
        // get raw record
        byte[] row = this.readRawRecord(bytePosition);

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(row));

        return this.readFromByteStream(stream);
    }

    public void deleteRow(long rowNumber) throws IOException{
        long bytePositionOfRecord = Index.getInstance().getBytePosition(rowNumber);
        if (bytePositionOfRecord == -1) {
            throw new IOException("Row does not exist in Index");
        }
        this.dbFile.seek(bytePositionOfRecord);
        this.dbFile.writeBoolean(true);

        // update the index since record is deleted;
        Index.getInstance().remove(rowNumber);
    }

    public void update(long rowNumber,
                       String name,
                       int age,
                       String address,
                       String carPlateNumber,
                       String description) throws IOException, DuplicateNameException{
        this.deleteRow(rowNumber);
        this.add(name, age, address, carPlateNumber, description);
    }

    public void update(final String nameToModify, String name,
                       int age,
                       String address,
                       String carPlateNumber,
                       String description ) throws IOException, DuplicateNameException{
        long rowNumber = Index.getInstance().getRowNumberByName(nameToModify);
        this.update(rowNumber, name, age, address, carPlateNumber, description);
    }
}
