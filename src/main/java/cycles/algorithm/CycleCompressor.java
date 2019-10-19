package cycles.algorithm;

import java.util.ArrayList;
import java.util.List;

public class CycleCompressor {
    private static final int BLOCK_SIZE = Byte.SIZE;

    private int index;
    private int remain;
    private int nBits;

    public CycleCompressor(int numberOfVertex){
        this.nBits = (int) Math.ceil(Math.log(numberOfVertex)/Math.log(2));
    }

    public byte[] compress(List<Vertex<String>> cycle){
        int bits = this.nBits * cycle.size();
        double columns = bits/(double) BLOCK_SIZE;
        int size = (int) Math.ceil(columns);

        byte[] compressCycle = new byte[size];

        int left = this.nBits;
        this.remain = BLOCK_SIZE;
        this.index = 0;

        for (Vertex vertex : cycle){
            this.compressValue(compressCycle, vertex.getId() , left);
        }

        return compressCycle;
    }

    public long[] decompress(byte[] compressCycle){
        List<Long> values = new ArrayList<>();

        int left = nBits;
        this.index = 0;
        this.remain = BLOCK_SIZE;

        long firstValue = this.decompressValue(compressCycle, left);
        values.add(firstValue);

        long value;
        do {
            value = decompressValue(compressCycle, nBits);
            values.add(value);
        } while ((firstValue != value));

        long [] arrayLong = new long[values.size()];

        int i = 0;
        for(Long l : values) {
            arrayLong[i] = l.longValue();
            i++;
        }
        return arrayLong;
    }

    private void compressValue(byte[] compressCycle, long value, int left){
        long auxValue = value;
        int shift = left - this.remain;

        if (shift < 0)
            auxValue = value << (-shift);

        else if (shift > 0)
            auxValue = value >>> shift;

        compressCycle[index] = (byte) (compressCycle[index] | auxValue);

        int aux = Math.min(this.remain, this.nBits);
        this.remain = this.remain - left;
        left = left - aux;

        if (this.remain <= 0) {
            this.index++;
            this.remain = BLOCK_SIZE;
        }

        if (left > 0) {
            this.compressValue(compressCycle, value, left);
        }
    }

    private long decompressValue(byte[] compressCycle, int left){
        int shift = BLOCK_SIZE - this.remain;

        long auxValue = (long) ((byte) (compressCycle[this.index] << shift));

        if (auxValue < 0) {
            auxValue = ~auxValue;
            auxValue = auxValue ^ 0xFF;
        }

        shift = BLOCK_SIZE - left;

        if (shift < 0) {
            auxValue = auxValue << shift;
        } else if (shift > 0) {
            auxValue = auxValue >>> shift;
        }

        int read = Math.min(left, this.remain);
        left = left - read;
        this.remain = this.remain - read;

        if (this.remain <= 0) {
            this.index++;
            this.remain = BLOCK_SIZE;
        }

        if (left > 0) {
            auxValue = auxValue | this.decompressValue(compressCycle, left);
        }

        return auxValue;
    }
}

