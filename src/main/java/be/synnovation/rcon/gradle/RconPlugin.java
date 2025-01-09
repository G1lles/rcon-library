package be.synnovation.rcon.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class RconPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("rcon", RconSettingsExtension.class, project);

        project.getTasks().register("sendRconCommand", SendRconCommandTask.class, task -> {
            task.setDescription("Sends an RCON command to a remote server.");
            task.setGroup("RCON");
        });
    }
}
