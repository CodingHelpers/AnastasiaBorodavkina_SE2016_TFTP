package proto;
import misc.ArrayIterator;
import org.apache.commons.lang.ArrayUtils;
import state.TransferMode;

import java.net.DatagramPacket;
import java.util.*;

public class PacketParser {
    public Packet parse(DatagramPacket packet) {
        return parse(packet.getData(), packet.getOffset(), packet.getLength());
    }

    public Packet parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    public Packet parse(byte[] buffer, int i, int ii) {
        Byte[] data = ArrayUtils.toObject(buffer);
        return parsePacket(
                new ArrayIterator<>(data, i, ii));
    }

    private Packet parsePacket(ArrayIterator<Byte> message) {
        int opcode = (message.next() << 8) | message.next();

        switch (opcode) {
            case 1: return parseReadRequest(message);
            case 2: return parseWriteRequest(message);
            case 3: return parseData(message);
            case 4: return parseAck(message);
            case 5: return parseError(message);
            default:
                throw new RuntimeException("Unknown packet type " + opcode);
        }
    }

    private Packet parseReadRequest(ArrayIterator<Byte> message) {
        return parseRequest(RequestPacket.Type.Read, message);
    }

    private Packet parseWriteRequest(ArrayIterator<Byte> message) {
        return parseRequest(RequestPacket.Type.Write, message);
    }

    private Packet parseRequest(RequestPacket.Type type, ArrayIterator<Byte> message) {
        String filename = parseCStr(message);
        String mode = parseCStr(message).toUpperCase();
        Map<String, String> options = new TreeMap<String, String>();

        while(message.hasNext()) {
            String option = parseCStr(message);
            String value = parseCStr(message);

            options.put(option, value);
        }

        return new RequestPacket(type, filename, TransferMode.valueOf(mode), options.get("login"), options.get("passwd"));
    }

    private Packet parseData(ArrayIterator<Byte> message) {
        int blockNum = (message.next() << 8) | message.next();
        Byte[] block = Arrays.copyOfRange(message.array, message.begin, message.end);
        return new DataPacket(blockNum, ArrayUtils.toPrimitive(block));
    }

    private Packet parseAck(ArrayIterator<Byte> message) {
        int blockNum = (message.next() << 8) | message.next();
        return new AckPacket(blockNum);
    }

    private Packet parseError(ArrayIterator<Byte> message) {
        int errorNum = (message.next() << 8) | message.next();
        String errorMsg = parseCStr(message);
        return new ErrorPacket(errorNum, errorMsg);
    }


    private String parseCStr(ArrayIterator<Byte> message) {
        byte[] buffer = new byte[128];
        int idx = 0;

        byte b;
        while(true) {
            b = message.next();

            if(b == 0x0) {
                break;
            }

            buffer[idx++] = b;
        }

        return new String(buffer, 0, idx);
    }

}
