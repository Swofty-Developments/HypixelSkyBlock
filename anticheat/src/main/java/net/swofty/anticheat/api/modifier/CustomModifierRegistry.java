package net.swofty.anticheat.api.modifier;

import net.swofty.anticheat.prediction.modifier.FrictionModifier;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomModifierRegistry {
    private final List<VelocityModifier> customVelocityModifiers = new CopyOnWriteArrayList<>();
    private final List<FrictionModifier> customFrictionModifiers = new CopyOnWriteArrayList<>();

    public void registerVelocityModifier(VelocityModifier modifier) {
        customVelocityModifiers.add(modifier);
    }

    public void registerFrictionModifier(FrictionModifier modifier) {
        customFrictionModifiers.add(modifier);
    }

    public void unregisterVelocityModifier(Class<? extends VelocityModifier> modifierClass) {
        customVelocityModifiers.removeIf(modifier -> modifier.getClass().equals(modifierClass));
    }

    public void unregisterFrictionModifier(Class<? extends FrictionModifier> modifierClass) {
        customFrictionModifiers.removeIf(modifier -> modifier.getClass().equals(modifierClass));
    }

    public List<VelocityModifier> getCustomVelocityModifiers() {
        return new ArrayList<>(customVelocityModifiers);
    }

    public List<FrictionModifier> getCustomFrictionModifiers() {
        return new ArrayList<>(customFrictionModifiers);
    }

    public void clear() {
        customVelocityModifiers.clear();
        customFrictionModifiers.clear();
    }
}
