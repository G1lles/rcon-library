package be.synnovation.rcon.client;

import be.synnovation.rcon.exception.RconException;

public interface RconClient extends AutoCloseable {

    /**
     * Connects to the remote server via RCON and authenticates using the configured password.
     *
     * @throws RconException if connection or authentication fails
     */
    void connect();

    /**
     * Sends a command to the server via RCON.
     *
     * @param command The command string (e.g. "reload confirm", "kick player", etc.)
     * @return Server response (if any)
     * @throws RconException if sending or receiving fails
     */
    String sendCommand(String command);

    /**
     * Closes the RCON connection (if open).
     */
    @Override
    void close();
}
