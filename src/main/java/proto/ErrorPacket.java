package proto;

import java.io.OutputStreamWriter;
import java.net.DatagramPacket;

public class ErrorPacket extends Packet {
    private int errorNumber;
    private String errorMessage;

    public ErrorPacket(int errorNumber, String errorMessage) {
        this.errorNumber = errorNumber;
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void serialize(DatagramPacket packet) {

    }
}
