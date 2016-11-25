import proto.*;
import state.TransferMode;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    private InetAddress addr;
    private int port;
    private String login;
    private String passwd;
    private DatagramSocket sock;

    public Client(InetAddress addr, int port, String login, String passwd) throws SocketException {
        this.addr = addr;
        this.login = login;
        this.passwd = passwd;
        this.port = port;

        sock = new DatagramSocket();
    }

    public void writeFile(String localFilename, String remoteFilemname) throws IOException {
        byte[] recvBuffer = new byte[516];
        byte[] block = new byte[512];
        byte[] buffer;

        RandomAccessFile file = new RandomAccessFile(localFilename, "r");

        DatagramPacket outDatagram;
        DatagramPacket inDatagram = new DatagramPacket(recvBuffer, recvBuffer.length);

        PacketParser parser = new PacketParser();
        Packet packet;

        // Sending write request
        WriteRequestPacket writeRequest = new WriteRequestPacket(remoteFilemname, TransferMode.OCTET, login, passwd);
        buffer = writeRequest.serialize();
        outDatagram = new DatagramPacket(buffer, buffer.length, addr, port);
        sock.send(outDatagram);

        int lastBlock = 0;
        boolean done = false;
        while(!done) {
            // Waiting for answer
            sock.receive(inDatagram);
            packet = parser.parse(inDatagram);

            // Check packet type
            if(packet instanceof ErrorPacket){
                throw new RuntimeException(((ErrorPacket) packet).getErrorMessage());
            } else if(!(packet instanceof AckPacket)) {
                throw new RuntimeException("Unexpected packet " + packet.toString());
            }

            AckPacket ackPacket = (AckPacket) packet;
            int ackBlockNum = ackPacket.getBlockNumber();

            if(lastBlock != ackBlockNum) {
                throw new RuntimeException("Unexpected ack " + ackPacket.toString());
            }

            file.seek(0);
            file.seek((lastBlock++) * 512);
            int blockLen = file.read(block);

            DataPacket dataPacket = new DataPacket(lastBlock, block, blockLen);
            buffer = dataPacket.serialize();
            outDatagram = new DatagramPacket(buffer, buffer.length, addr, port);
            sock.send(outDatagram);

            if(blockLen < 512) {
                done = true;
            }
        }
    }

    public void readFile(String remoteFilemname, String localFilename) throws IOException {
        byte[] recvBuffer = new byte[516];
        byte[] buffer;
        RandomAccessFile file = new RandomAccessFile(localFilename, "rw");

        DatagramPacket outDatagram;
        DatagramPacket inDatagram = new DatagramPacket(recvBuffer, recvBuffer.length);

        PacketParser parser = new PacketParser();
        Packet packet;

        // Sending write request
        ReadRequestPacket readRequest = new ReadRequestPacket(remoteFilemname, TransferMode.OCTET, login, passwd);
        buffer = readRequest.serialize();
        outDatagram = new DatagramPacket(buffer, buffer.length, addr, port);
        sock.send(outDatagram);

        while(true) {
            // Waiting for answer
            sock.receive(inDatagram);
            packet = parser.parse(inDatagram);

            // Check packet type
            if(packet instanceof ErrorPacket){
                throw new RuntimeException(((ErrorPacket) packet).getErrorMessage());
            } else if(!(packet instanceof DataPacket)) {
                throw new RuntimeException("Unexpected packet " + packet.toString());
            }

            DataPacket dataPacket = (DataPacket) packet;
            int blockNum = dataPacket.getBlockNumber();
            byte[] data = dataPacket.getData();

            file.seek(0);
            file.seek((blockNum-1) * 512);
            file.write(data);

            AckPacket ackPacket = new AckPacket(blockNum);
            buffer = ackPacket.serialize();
            outDatagram = new DatagramPacket(buffer, buffer.length, addr, port);
            sock.send(outDatagram);

            if(data.length < 512) {
                break;
            }
        }
    }
}
