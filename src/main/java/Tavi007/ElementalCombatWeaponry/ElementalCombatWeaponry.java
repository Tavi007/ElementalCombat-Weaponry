package Tavi007.ElementalCombatWeaponry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Tavi007.ElementalCombatWeaponry.init.BlockList;
import Tavi007.ElementalCombatWeaponry.init.ItemList;
import Tavi007.ElementalCombatWeaponry.init.StartupClientOnly;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("elementalcombat_weaponry")
public class ElementalCombatWeaponry {

    public static ElementalCombatWeaponry instance;
    public static final String MOD_ID = "elementalcombat_weaponry";
    public static final ResourceLocation ARMOR_RESOURCE_LOCATION = new ResourceLocation(MOD_ID, "armor");
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static IEventBus MOD_EVENT_BUS;
    public static final ItemGroup ELEMENTAL_COMBAT_GROUP = new ElementalCombatWeaponry.ElementalCombatItemGroup(MOD_ID);

    @SuppressWarnings("deprecation")
    public ElementalCombatWeaponry() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        ItemList.ITEMS.register(ElementalCombatWeaponry.MOD_EVENT_BUS);
        BlockList.ITEMS.register(ElementalCombatWeaponry.MOD_EVENT_BUS);
        BlockList.BLOCKS.register(ElementalCombatWeaponry.MOD_EVENT_BUS);

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

        // register client only stuff
        DistExecutor.runWhenOn(Dist.CLIENT, () -> ElementalCombatWeaponry::registerClientOnly);
    }

    public static class ElementalCombatItemGroup extends ItemGroup {

        public ElementalCombatItemGroup(String name) {
            super(name);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemList.FIREANDICE_SWORD.get());
        }
    }

    private static void registerClientOnly() {
        MOD_EVENT_BUS.register(StartupClientOnly.class);
    }
}
