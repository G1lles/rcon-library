package be.synnovation.rcon.model;

import lombok.Getter;

/**
 * Represents a decoded RCON response from the server.
 * <p>
 * Distinct from RconPacket to avoid confusion with the raw protocol structure.
 */
@Getter
public record RconResponse(int requestId, int type, String body) {
}
