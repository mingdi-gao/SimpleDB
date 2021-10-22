package com.mixer.raw;

import java.util.HashMap;
import java.util.Map;

// this class is Singleton
public final class Index {
    private static Index index;
    private long totalRowNumber;
    // row number -> byte position
    private Map<Long, Long> rowIndex;

    private Index() {
        this.rowIndex = new HashMap<>();
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
    public void remove(int row) {
        this.rowIndex.remove(row);
        this.totalRowNumber--;
    }

    public long getTotalRowNumber() {
        return this.totalRowNumber;
    }
}
