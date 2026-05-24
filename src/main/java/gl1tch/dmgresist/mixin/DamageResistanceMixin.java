package gl1tch.dmgresist.mixin;


import com.mojang.serialization.Codec;
import gl1tch.dmgresist.LivingEntityDamageResistance;
import gl1tch.dmgresist.util.IEntityData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class DamageResistanceMixin implements IEntityData {
    public List<Holder<DamageType>> damageResistances = new ArrayList<>();

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addSaveData(ValueOutput output, CallbackInfo ci) {
        output.storeNullable("DamageResistance", Codec.list(DamageType.CODEC), this.damageResistances);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readSaveData(ValueInput input, CallbackInfo ci) {
        this.setDamageResistances(input.read("DamageResistance", Codec.list(DamageType.CODEC)).orElse(new ArrayList<>()));
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void takeDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        for (Holder<DamageType> damageType : this.getDamageResistances()) {
            if (source.type().toString().equals(damageType.value().toString())) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Override
    public List<Holder<DamageType>> getDamageResistances() {
        return this.damageResistances;
    }

    @Override
    public void setDamageResistances(List<Holder<DamageType>> damageTypes) {
        this.damageResistances = damageTypes;
    }
}
