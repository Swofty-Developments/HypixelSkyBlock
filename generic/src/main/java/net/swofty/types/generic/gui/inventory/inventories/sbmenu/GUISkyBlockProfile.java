package net.swofty.types.generic.gui.inventory.inventories.sbmenu;
 
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.ChatColor;

public class GUISkyBlockProfile extends SkyBlockInventoryGUI {

    public GUISkyBlockProfile() {
        super("Your SkyBlock Profile", InventoryType.CHEST_6_ROW);
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(48));
        set(GUIClickableItem.getGoBackItem(49, new GUISkyBlockMenu()));

    }

    @Override
    public void onOpen(final InventoryGUIOpenEvent e) {

        final SkyBlockPlayer player = e.player();
        final PlayerStatistics statistics = player.getStatistics();
        double visualcap = statistics.allStatistics().get(ItemStatistic.CRIT_CHANCE).intValue() * 100.0;
        if (visualcap > 100.0) {
            visualcap = 100.0;
        }

        set(19, ItemStackCreator.getStack(ChatColor.RED + "❤ Health " + ChatColor.WHITE + statistics.allStatistics().get(ItemStatistic.HEALTH).intValue(), Material.GOLDEN_APPLE,1,ChatColor.GRAY + "Health is your total maximum", ChatColor.GRAY + "health. Your natural", ChatColor.GRAY + "regeneration gives "));
        set(20, ItemStackCreator.getStack(ChatColor.GREEN + "❈ Defense " + ChatColor.WHITE + statistics.allStatistics().get(ItemStatistic.DEFENSE).intValue(), Material.IRON_CHESTPLATE, 1, ChatColor.GRAY + "Defense reduces the damage that", ChatColor.GRAY + "you take from enemies.", " ", ChatColor.GRAY + "Damage Reduction: " + ChatColor.GREEN + Math.round(statistics.allStatistics().get(ItemStatistic.DEFENSE).intValue() / (statistics.allStatistics().get(ItemStatistic.DEFENSE).intValue() + 100.0) * 100.0 * 10.0 / 10.0) + "%"));
        set(21, ItemStackCreator.getStack(ChatColor.RED + "❁ Strength " + ChatColor.WHITE + statistics.allStatistics().get(ItemStatistic.STRENGTH).intValue(), Material.BLAZE_POWDER,  1, ChatColor.GRAY + "Strength increases your base", ChatColor.GRAY + "melee damage, including punching", ChatColor.GRAY + "and weapons.", " ", ChatColor.GRAY + "Base Damage: " + ChatColor.GREEN + 5.0 + statistics.allStatistics().get(ItemStatistic.STRENGTH).intValue() / 5.0));
        set(22, ItemStackCreator.getStack(ChatColor.WHITE + "✦ Speed " + statistics.allStatistics().get(ItemStatistic.SPEED).intValue() * 100.0, Material.SUGAR,  1, ChatColor.GRAY + "Speed increases your walk speed.", " ", ChatColor.GRAY + "You are " + ChatColor.GREEN + ((statistics.allStatistics().get(ItemStatistic.SPEED).intValue() * 100.0) - 100) + "% " + ChatColor.GRAY + "faster!"));
        set(23, ItemStackCreator.getStackHead(ChatColor.BLUE + "☣ Crit Chance " + ChatColor.WHITE + (int) visualcap + "%", "3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1, ChatColor.GRAY + "Crit Chance is your chance to", ChatColor.GRAY + "deal extra damage on enemies."));
        set(24, ItemStackCreator.getStackHead(ChatColor.BLUE + "☠ Crit Damage " + ChatColor.WHITE + (statistics.allStatistics().get(ItemStatistic.CRIT_DAMAGE).intValue() * 100.0) + "%", "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1, ChatColor.GRAY + "Crit Damage is the amount of", ChatColor.GRAY + "extra damage you deal when", ChatColor.GRAY + "landing a critical hit."));
        set(25, ItemStackCreator.getStack(ChatColor.AQUA + "✎ Intelligence " + ChatColor.WHITE + statistics.allStatistics().get(ItemStatistic.FEROCITY).intValue(), Material.ENCHANTED_BOOK,  1, ChatColor.GRAY + "Intelligence increases both your", ChatColor.GRAY + "Mana Pool and the damage of your", ChatColor.GRAY + "magical items.", " ", ChatColor.GRAY + "Magic Damage: +" + ChatColor.AQUA + statistics.allStatistics().get(ItemStatistic.INTELLIGENCE).intValue() + "%", ChatColor.GRAY + "Mana Pool: " + ChatColor.AQUA + statistics.allStatistics().get(ItemStatistic.INTELLIGENCE).intValue() + 100.0));
        set(30, ItemStackCreator.getStack(ChatColor.DARK_AQUA + "α Sea Creature Chance " + ChatColor.RED + "✗", Material.PRISMARINE_CRYSTALS,  1, ChatColor.GRAY + "Sea Creature Chance is your", ChatColor.GRAY + "chance to catch Sea Creatures", ChatColor.GRAY + "while fishing."));
        set(32, ItemStackCreator.getStackHead(ChatColor.LIGHT_PURPLE + "♣ Pet Luck " + ChatColor.RED + "✗", "93c8aa3fde295fa9f9c27f734bdbab11d33a2e43e855accd7465352377413b", 1, ChatColor.GRAY + "Pet Luck increases how many pets", ChatColor.GRAY + "you find and gives you better", ChatColor.GRAY + "luck when crafting pets."));
        set(31, ItemStackCreator.getStack(ChatColor.AQUA + "✯ Magic Find " + ChatColor.WHITE + statistics.allStatistics().get(ItemStatistic.MAGIC_FIND).intValue() * 100.0, Material.STICK,  1, ChatColor.GRAY + "Magic Find increases how many", ChatColor.GRAY + "rare items you find.", " "));
    }


    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
