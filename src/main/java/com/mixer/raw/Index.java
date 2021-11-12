package com.mixer.raw;

import java.util.HashMap;
import java.util.Map;

// this class is Singleton
public final class Index {
    private static Index index;
    private long totalRowNumber;
    // Long row number -> Long byte position
    private Map<Long, Long> rowIndex;

    // String name -> Long row number
    private HashMap<String, Long> nameIndex;

    private Index() {
        this.rowIndex = new HashMap<>();
        this.nameIndex = new HashMap<>();
    }

    public static Index getInstance() {
        if (index == null) {
            index = new Index();
        }
        return index;
    }

    // add a entry to the map
    // At this moment this index is not able to remove deleted Index record
    // Neither there are any delete method in file handler
    public void add(long bytePosition) {
        this.rowIndex.put(this.totalRowNumber, bytePosition);
        this.totalRowNumber++;
    }

    // get Byte position from row number
    public long getBytePosition(long rowNumber) {
        return this.rowIndex.getOrDefault(rowNumber, -1L);
    }
    public void remove(long row) {
        this.rowIndex.remove(row);
        this.totalRowNumber--;

//        String nameTODelete= this.nameIndex.search(2, (k, v) -> v == row ? k : null);
//        if (nameTODelete != null) {
//            this.nameIndex.remove(nameTODelete);
//        }
    }

    public long getTotalRowNumber() {
        return this.totalRowNumber;
    }

    public void addNameToIndex(final String name, long rowIndex) {
        this.nameIndex.put(name, rowIndex);
    }

    public boolean hasNameInIndex(final String name) {
        return this.nameIndex.containsKey(name);
    }

    public long getRowNumberByName(final String name) {
        return this.nameIndex.get(name);
    }

    public void clear() {
        this.totalRowNumber = 0;
        this.rowIndex.clear();
        this.nameIndex.clear();
    }
}
