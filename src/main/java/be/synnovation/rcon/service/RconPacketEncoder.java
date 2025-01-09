package be.synnovation.rcon.service;

import be.synnovation.rcon.enums.RconRequestType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Encodes commands or login requests into RCON packets (byte arrays).
 */
public class RconPacketEncoder {

    /**
     * Creates an RCON packet and returns it as raw bytes.
     *
     * @param data        The command string or login password
     * @param requestType The type of request (LOGIN or COMMAND)
     * @return Encoded packet as a byte array
     */
    public byte[] encodePacket(String data, RconRequestType requestType) {
        int requestId = 0; // The real requestId is determined by the RconConnection
        byte[] payload = data.getBytes(StandardCharsets.UTF_8);

        // Length = 4 (requestId) + 4 (type) + payload.length + 2 (null terminators)
        int length = 4 + 4 + payload.length + 2;

        ByteBuffer buffer = ByteBuffer.allocate(length + 4);
        buffer.putInt(length);
        buffer.putInt(requestId);
        buffer.putInt(requestType.getValue());
        buffer.put(payload);
        // Two null terminators
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x00);

        return buffer.array();
    }
}