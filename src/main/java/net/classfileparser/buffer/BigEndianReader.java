package net.classfileparser.buffer;

public interface BigEndianReader {

    default byte readByte() {
        byte arr[] = getBuffer();
        return arr[incrementOffset()];
    }


    default short readShort() {
        byte arr[] = getBuffer();
        short magic = (short) ((arr[incrementOffset()] & 0xFF) << 8);
        magic |= ((arr[incrementOffset()]) & 0xFF);
        return magic;
    }

    default int readInt() {
        byte arr[] = getBuffer();
        int magic = (arr[incrementOffset()] << 24);
        magic |= ((arr[incrementOffset()]) & 0xFF) << 16;
        magic |= ((arr[incrementOffset()]) & 0xFF) << 8;
        return magic | ((arr[incrementOffset()]) & 0xFF);
    }

    default byte[] readByteArr(int length) {
        byte buffer[] = new byte[length];
        int offset = getOffset();
        System.arraycopy(getBuffer(), offset, buffer, 0, length);
        seek(offset + length);
        return buffer;
    }

    byte[] getBuffer();

    int incrementOffset();

    void seek(int offset);

    int getOffset();
}
