package net.swofty.type.dwarvenmines.commission;

public class Commission {
    public final String name;
    public final CommissionCategory category;
    public final Objective objective;
    public final boolean oneTimeOnly;

    public Commission(
            String name,
            CommissionCategory category,
            Objective objective,
            boolean oneTimeOnly
    ) {
        this.name = name;
        this.category = category;
        this.objective = objective;
        this.oneTimeOnly = oneTimeOnly;
    }

    public String generateDescription() {
        StringBuilder sb = new StringBuilder();

        switch (objective.type) {
            case MINE -> {
                sb.append("Mine §a").append(objective.amount).append(" §7");
                sb.append(getTargetName(objective.target)).append(" Ore");
                appendLocation(sb);
                sb.append(".");
            }
            case SLAY -> {
                sb.append("Slay §a").append(objective.amount).append(" §7");
                sb.append(getTargetNamePlural(objective.target));
                sb.append("§7");
                appendLocation(sb);
                appendEvent(sb);
                sb.append(".");
            }
            case DAMAGE -> {
                sb.append("Damage ").append(getTargetNamePlural(objective.target));
                sb.append(" §7").append(objective.amount).append(" times");
                appendLocation(sb);
                sb.append(".");
            }
            case PARTICIPATE -> sb.append("Participate in the ").append(getEventName(objective.event)).append(".");
            case COLLECT -> {
                sb.append("Collect ").append(objective.amount).append(" ");
                sb.append(getCollectibleName(objective.target));
                appendEvent(sb);
                sb.append(".");
            }
            case DEPOSIT -> {
                sb.append("Deposit ").append(objective.amount).append(" Tickets");
                appendEvent(sb);
                sb.append(".");
            }
        }

        return sb.toString();
    }

    private void appendLocation(StringBuilder sb) {
        if (!objective.location.isAny()) {
            objective.location.getRegion().ifPresent(region ->
                    sb.append(" in §b").append(region.getName()));
        }
    }

    private void appendEvent(StringBuilder sb) {
        if (objective.event != EventType.NONE) {
            sb.append(" during the ").append(getEventName(objective.event));
        }
    }

    private String getTargetName(Objective.BlockTarget target) {
        return switch (target) {
            case MITHRIL -> "Mithril";
            case TITANIUM -> "Titanium";
            case GOBLIN -> "Goblin";
            case GLACITE_WALKER -> "Glacite Walker";
            case TREASURE_HOARDER -> "Treasure Hoarder";
            case GOLDEN_GOBLIN -> "Golden Goblin";
            case STAR_SENTRY -> "Star Sentry";
            case NONE -> "";
        };
    }

    private String getTargetNamePlural(Objective.BlockTarget target) {
        return switch (target) {
            case MITHRIL -> "Mithril Ore";
            case TITANIUM -> "Titanium Ore";
            case GOBLIN -> "§cGoblins";
            case GLACITE_WALKER -> "§bGlacite Walkers";
            case TREASURE_HOARDER -> "§cTreasure Hoarders";
            case GOLDEN_GOBLIN -> "§6Golden Goblin";
            case STAR_SENTRY -> "Star Sentrys";
            case NONE -> "";
        };
    }

    private String getCollectibleName(Objective.BlockTarget target) {
        return switch (target) {
            case MITHRIL -> "Mithril Powder";
            default -> target.name();
        };
    }

    private String getEventName(EventType event) {
        return switch (event) {
            case GOBLIN_RAID -> "§cGoblin Raid §7Event";
            case RAFFLE -> "§eRaffle §7Event";
            case DOUBLE_POWDER -> "2x Powder Event";
            case NONE -> "";
        };
    }
}
