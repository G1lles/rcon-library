package be.synnovation.rcon.model;

import be.synnovation.rcon.service.RconPacketDecoder;
import be.synnovation.rcon.exception.RconException;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Manages raw socket connection to the RCON server.
 */
public class RconConnection {

    @Getter
    private final String host;

    @Getter
    private final int port;

    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private volatile int currentRequestId = 1;

    public RconConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Opens the socket connection.
     */
    public void open() {
        try {
            this.socket = new Socket(host, port);
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            throw new RconException("Failed to open socket connection to RCON server.", e);
        }
    }

    /**
     * Sends raw data to the RCON server.
     *
     * @param packetBytes The encoded RCON packet bytes
     * @return The requestId used in the packet
     */
    public int sendPacket(byte[] packetBytes) {
        try {
            int requestId = getNextRequestId();
            output.write(packetBytes);
            output.flush();
            return requestId;
        } catch (IOException e) {
            throw new RconException("Failed to send RCON packet.", e);
        }
    }

    /**
     * Receives an RCON packet from the server and decodes it.
     *
     * @param decoder The decoder to parse the incoming data
     * @return RconResponse
     */
    public RconResponse receivePacket(RconPacketDecoder decoder) {
        return decoder.decodePacket(input);
    }

    /**
     * Closes the socket connection.
     */
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private synchronized int getNextRequestId() {
        return currentRequestId++;
    }
}
