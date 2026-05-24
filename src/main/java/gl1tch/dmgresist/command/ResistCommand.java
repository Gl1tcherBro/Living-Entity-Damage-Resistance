package gl1tch.dmgresist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import gl1tch.dmgresist.LivingEntityDamageResistance;
import gl1tch.dmgresist.util.IEntityData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.List;

public class ResistCommand {
    public static void register() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("resistances")
                        .then(Commands.argument("target", EntityArgument.entities())
                                .then(Commands.argument("addRemove", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            builder.suggest("add");
                                            builder.suggest("remove");

                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("damageType", ResourceArgument.resource(commandBuildContext, Registries.DAMAGE_TYPE))
                                                .executes(context -> {
                                                    IEntityData player = (IEntityData) EntityArgument.getEntity(context, "target");
                                                    String addRemove = StringArgumentType.getString(context, "addRemove");

                                                    if (!(addRemove.equals("add") | addRemove.equals("remove"))) {
                                                        context.getSource().getPlayer().sendSystemMessage(Component.literal("§cOnly add or remove are allowed as the method."));
                                                    }
                                                    List<Holder<DamageType>> damageResistances = player.getDamageResistances();
                                                    Holder<DamageType> damageType = context.getSource().getLevel().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).get(ResourceArgument.getResource(context, "damageType", Registries.DAMAGE_TYPE).key()).orElseThrow();
                                                    DamageSource damageSource = new DamageSource(damageType);

                                                    LivingEntityDamageResistance.LOGGER.info(DamageTypes.ARROW.toString());
                                                    LivingEntityDamageResistance.LOGGER.info(damageSource.typeHolder().toString());

                                                    if (addRemove.equals("add")) {
                                                        damageResistances.add(damageType);
                                                    } else {
                                                        try {
                                                            damageResistances.remove(damageType);
                                                        } catch (Exception n) {
                                                            context.getSource().getPlayer().sendSystemMessage(Component.literal("§cThe target entity already does not have this damage resistance or an unknow error occurred."));
                                                        }
                                                    }
                                                    player.setDamageResistances(damageResistances);

                                                    return 0;
                                                })))));
    }
}
