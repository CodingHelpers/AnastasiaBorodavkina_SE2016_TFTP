package state;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.SeekableByteChannel;
import java.util.List;
import java.util.Map;

public class ClientState {
    public enum State {
        None,
        WaitingData,
        WaitingAck,
    }

    public State state;
    public String filename;
    public RandomAccessFile file;
    public int lastBlock;
    public boolean isLast;
    public TransferMode mode;
    public boolean closeConnection;
}
