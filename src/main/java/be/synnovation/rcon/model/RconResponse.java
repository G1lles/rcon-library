package be.synnovation.rcon.model;

/**
 * Represents a decoded RCON response from the server.
 * <p>
 * Distinct from RconPacket to avoid confusion with the raw protocol structure.
 */
public record RconResponse(int requestId, int type, String body) {
}
