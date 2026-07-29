package net.swofty.dungeons.catacombs.boss.state;

import net.swofty.dungeons.catacombs.boss.BossPhaseTrigger;
import net.swofty.dungeons.catacombs.boss.CatacombsBossEncounter;
import net.swofty.dungeons.catacombs.boss.CatacombsBossPhase;

import java.util.ArrayList;
import java.util.List;

public final class BossFightController {
    private final CatacombsBossEncounter encounter;
    private final List<BossFightTrace> traces = new ArrayList<>();
    private BossFightState state = BossFightState.NOT_STARTED;
    private int phaseIndex;
    private int objectiveProgress;

    public BossFightController(CatacombsBossEncounter encounter) {
        this.encounter = encounter;
    }

    public CatacombsBossEncounter encounter() {
        return encounter;
    }

    public BossFightState state() {
        return state;
    }

    public CatacombsBossPhase currentPhase() {
        return encounter.phases().get(Math.min(phaseIndex, encounter.phases().size() - 1));
    }

    public List<BossFightTrace> traces() {
        return List.copyOf(traces);
    }

    public void start() {
        state = BossFightState.ACTIVE;
        traces.add(BossFightTrace.of(currentPhase().id(), BossFightEvent.START, currentPhase().displayName()));
    }

    public void accept(BossFightEvent event, int value) {
        if (state == BossFightState.COMPLETED || state == BossFightState.FAILED) {
            return;
        }
        if (event == BossFightEvent.PLAYER_WIPED) {
            state = BossFightState.FAILED;
            traces.add(BossFightTrace.of(currentPhase().id(), event, "Party wiped"));
            return;
        }
        if (event == BossFightEvent.FORCE_COMPLETE) {
            completePhase();
            return;
        }

        CatacombsBossPhase phase = currentPhase();
        traces.add(BossFightTrace.of(phase.id(), event, String.valueOf(value)));
        if (matches(phase, event, value)) {
            completePhase();
        }
    }

    private boolean matches(CatacombsBossPhase phase, BossFightEvent event, int value) {
        if (phase.trigger() == BossPhaseTrigger.ENTER_BOSS_ROOM && event == BossFightEvent.START) {
            return true;
        }
        if (phase.trigger() == BossPhaseTrigger.BOSS_HEALTH_THRESHOLD && event == BossFightEvent.BOSS_DAMAGED) {
            return value <= phase.triggerValue();
        }
        if (phase.trigger() == BossPhaseTrigger.ADDS_DEFEATED && event == BossFightEvent.ADD_DEFEATED) {
            objectiveProgress += value;
            return phase.triggerValue() <= 0 || objectiveProgress >= phase.triggerValue();
        }
        if (phase.trigger() == BossPhaseTrigger.PLAYER_OBJECTIVE && event == BossFightEvent.OBJECTIVE_COMPLETED) {
            objectiveProgress += value;
            return objectiveProgress >= phase.triggerValue();
        }
        if (phase.trigger() == BossPhaseTrigger.TIMER && event == BossFightEvent.TIMER_EXPIRED) {
            return true;
        }
        return phase.trigger() == BossPhaseTrigger.PHASE_COMPLETE && event == BossFightEvent.OBJECTIVE_COMPLETED;
    }

    private void completePhase() {
        traces.add(BossFightTrace.of(currentPhase().id(), BossFightEvent.FORCE_COMPLETE, "Phase complete"));
        objectiveProgress = 0;
        phaseIndex++;
        if (phaseIndex >= encounter.phases().size()) {
            state = BossFightState.COMPLETED;
        } else {
            state = BossFightState.PHASE_TRANSITION;
            state = BossFightState.ACTIVE;
        }
    }
}
