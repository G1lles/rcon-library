package be.synnovation.rcon.client;

import be.synnovation.rcon.enums.RconRequestType;
import be.synnovation.rcon.exception.RconException;
import be.synnovation.rcon.model.RconConnection;
import be.synnovation.rcon.model.RconResponse;
import be.synnovation.rcon.service.RconPacketDecoder;
import be.synnovation.rcon.service.RconPacketEncoder;

import java.io.IOException;

public class RconClientImpl implements RconClient {

    private final RconConnection connection;
    private final RconPacketEncoder encoder;
    private final RconPacketDecoder decoder;
    private final String password;

    public RconClientImpl(String host, int port, String password) {
        this.connection = new RconConnection(host, port);
        this.encoder = new RconPacketEncoder();
        this.decoder = new RconPacketDecoder();
        this.password = password;
    }

    @Override
    public void connect() {
        connection.open();

        // Attempt authentication
        int requestId = connection.sendPacket(encoder.encodePacket(password, RconRequestType.LOGIN));
        RconResponse response = connection.receivePacket(decoder);

        // RCON “login” is successful only if response’s requestId matches the original requestId
        if (response.requestId() != requestId) {
            throw new RconException("Authentication to RCON server failed. Check your password.");
        }
    }

    @Override
    public String sendCommand(String command) {
        int requestId = connection.sendPacket(encoder.encodePacket(command, RconRequestType.COMMAND));
        RconResponse response = connection.receivePacket(decoder);

        if (response.requestId() != requestId) {
            throw new RconException("RCON command request ID mismatch. Potential protocol error.");
        }

        return response.body();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RconException("Failed to close RCON connection.", e);
        }
    }
}

