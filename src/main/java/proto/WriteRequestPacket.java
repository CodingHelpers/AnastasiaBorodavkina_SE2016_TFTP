package proto;

import state.TransferMode;

import java.io.OutputStreamWriter;
import java.net.DatagramPacket;

public class WriteRequestPacket extends Packet {
    private String filename;
    private TransferMode mode;
    private String login;
    private String passwd;

    public WriteRequestPacket(String filename, TransferMode mode) {
        this.filename = filename;
        this.mode = mode;
    }

    public WriteRequestPacket(String filename, TransferMode mode, String login, String passwd) {
        this.filename = filename;
        this.mode = mode;
        this.login = login;
        this.passwd = passwd;
    }

    public String getFilename() {
        return filename;
    }

    public TransferMode getMode() {
        return mode;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswd() {
        return passwd;
    }

    @Override
    public void serialize(DatagramPacket packet) {

    }
}
