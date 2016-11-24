package proto;

import java.io.OutputStreamWriter;
import java.net.DatagramPacket;

public class AckPacket extends Packet {
    private int blockNumber;

    public AckPacket(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    @Override
    public void serialize(DatagramPacket packet) {

    }
}
