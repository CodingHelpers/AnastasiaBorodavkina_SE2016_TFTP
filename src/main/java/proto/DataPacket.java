package proto;

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
    public void serialize(DatagramPacket packet) {

    }
}
