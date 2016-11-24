package proto;

import java.io.OutputStreamWriter;
import java.net.DatagramPacket;

public abstract class Packet {
    public abstract void serialize(DatagramPacket packet);
}


