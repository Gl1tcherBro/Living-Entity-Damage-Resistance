package gl1tch.dmgresist.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.ArrayList;
import java.util.List;

public interface IEntityData {
    List<Holder<DamageType>> getDamageResistances();
    void setDamageResistances(List<Holder<DamageType>> damageTypes);
}
