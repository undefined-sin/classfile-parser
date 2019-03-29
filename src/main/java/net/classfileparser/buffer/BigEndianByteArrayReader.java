package net.classfileparser.buffer;

public class BigEndianByteArrayReader implements BigEndianReader {
    private final byte[] arr;
    private int currentOffset;

    public BigEndianByteArrayReader(byte[] arr) {
        this.arr = arr;
        this.currentOffset = 0;
    }

    @Override
    public byte[] getBuffer() {
        return this.arr;
    }

    @Override
    public int incrementOffset() {
        return currentOffset++;
    }

    @Override
    public void seek(int offset) {
        this.currentOffset = offset;
    }

    @Override
    public int getOffset() {
        return this.currentOffset;
    }

}
