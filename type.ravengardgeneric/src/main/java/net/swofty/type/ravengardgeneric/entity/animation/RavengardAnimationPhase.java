package net.swofty.type.ravengardgeneric.entity.animation;

public enum RavengardAnimationPhase {
    IDLE(true),
    WALK(true),
    TALK(false);

    private final boolean looping;

    RavengardAnimationPhase(boolean looping) {
        this.looping = looping;
    }

    public boolean looping() {
        return looping;
    }
}
