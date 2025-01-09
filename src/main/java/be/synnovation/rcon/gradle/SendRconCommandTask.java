package be.synnovation.rcon.gradle;

import be.synnovation.rcon.client.RconClientImpl;
import be.synnovation.rcon.exception.RconException;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * Custom Gradle task to send RCON commands.
 */
@Getter
@Setter
public class SendRconCommandTask extends DefaultTask {

    @Input
    private String command;

    @TaskAction
    public void sendCommand() {
        RconSettingsExtension settings = getProject().getExtensions().findByType(RconSettingsExtension.class);
        if (settings == null) {
            throw new IllegalStateException("RconSettingsExtension is not configured in the project.");
        }

        String host = settings.getHost();
        int port = settings.getPort();
        String password = settings.getPassword();

        if (host == null || password == null) {
            throw new IllegalArgumentException("RCON host and password must be provided.");
        }

        getLogger().lifecycle("Sending RCON command to {}:{}", host, port);

        try (RconClientImpl client = new RconClientImpl(host, port, password)) {
            client.connect();
            String response = client.sendCommand(command);
            getLogger().lifecycle("RCON Response: {}", response);
        } catch (RconException e) {
            getLogger().error("Failed to send RCON command: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            getLogger().error("Unexpected error occurred while sending RCON command", e);
            throw new RuntimeException(e);
        }
    }
}
