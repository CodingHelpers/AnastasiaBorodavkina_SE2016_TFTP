package proto;

import org.apache.commons.lang.ArrayUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.OutputStreamWriter;
import java.net.DatagramPacket;

public class DataPacket extends Packet {
    private int blockNumber;
    private byte[] data;

    public DataPacket(int blockNumber, byte[] data) {
        this.blockNumber = blockNumber;
        this.data = data;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public byte[] serialize() {
        byte[] buffer = new byte[4];
        buffer[1] = (byte) 3;
        buffer[2] = (byte) ((blockNumber >> 8) & 0xFF);
        buffer[3] = (byte) (blockNumber & 0xFF);

        return ArrayUtils.addAll(buffer, data);
    }
}
