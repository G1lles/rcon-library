package be.synnovation.rcon.service;

import be.synnovation.rcon.exception.RconException;
import be.synnovation.rcon.model.RconResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Decodes incoming RCON packets from an InputStream.
 */
@Slf4j
public class RconPacketDecoder {

    /**
     * Reads bytes from the stream and constructs an RconResponse.
     */
    public RconResponse decodePacket(InputStream input) {
        try {
            byte[] lengthBytes = new byte[4];
            if (readFully(input, lengthBytes) < 4) {
                throw new RconException("Failed to read RCON packet length.");
            }

            int length = ByteBuffer.wrap(lengthBytes).getInt();
            if (length < 10) {
                throw new RconException("Invalid RCON packet length: " + length);
            }

            // Read the rest of the packet
            byte[] packetBytes = new byte[length];
            if (readFully(input, packetBytes) < length) {
                throw new RconException("Incomplete RCON packet from server.");
            }

            ByteBuffer packetBuffer = ByteBuffer.wrap(packetBytes);
            int requestId = packetBuffer.getInt();       // next 4
            int type = packetBuffer.getInt();            // next 4

            // The body is (length - 8) bytes, minus 2 null terminators at the end
            int payloadSize = length - 8;
            byte[] bodyBytes = new byte[payloadSize];
            packetBuffer.get(bodyBytes);

            // The last two bytes are zeros, so the “real” string length is payloadSize - 2
            String body = new String(bodyBytes, 0, payloadSize - 2, StandardCharsets.UTF_8);

            return new RconResponse(requestId, type, body);
        } catch (IOException e) {
            throw new RconException("IOException occurred while decoding RCON packet.", e);
        }
    }

    private int readFully(InputStream in, byte[] buffer) throws IOException {
        return readFully(in, buffer, 0, buffer.length);
    }

    private int readFully(InputStream in, byte[] buffer, int offset, int length) throws IOException {
        int totalRead = 0;
        while (totalRead < length) {
            int count = in.read(buffer, offset + totalRead, length - totalRead);
            if (count < 0) break;
            totalRead += count;
        }
        return totalRead;
    }
}
