package be.synnovation.rcon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RconRequestType {
    LOGIN(3),
    COMMAND(2);

    private final int value;
}