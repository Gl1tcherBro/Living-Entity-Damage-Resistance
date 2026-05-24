package gl1tch.dmgresist.util;

import gl1tch.dmgresist.command.ResistCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModStuffs {
    public static void register() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(ResistCommand::register);
    }
}
