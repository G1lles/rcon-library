package be.synnovation.rcon.gradle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gradle.api.Project;

import java.util.Objects;

/**
 * Custom Gradle extension to configure RCON settings.
 */
@Getter
@Setter
@NoArgsConstructor
public class RconSettingsExtension {

    private String host = "127.0.0.1";
    private int port = 25575;
    private String password;

    public RconSettingsExtension(Project project) {
        this.host = project.findProperty("rcon.host") != null
                ? Objects.requireNonNull(project.findProperty("rcon.host")).toString()
                : System.getenv("RCON_HOST");

        this.port = project.hasProperty("rcon.port")
                ? Integer.parseInt(Objects.requireNonNull(project.findProperty("rcon.port")).toString())
                : (System.getenv("RCON_PORT") != null ? Integer.parseInt(System.getenv("RCON_PORT")) : 25575);

        this.password = project.findProperty("rcon.password") != null
                ? Objects.requireNonNull(project.findProperty("rcon.password")).toString()
                : System.getenv("RCON_PASSWORD");
    }
}
