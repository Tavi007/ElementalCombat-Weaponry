package Tavi007.ElementalCombatWeaponry.init;

import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class StartupClientOnly {

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event)
	{
		ItemModelsProperties.registerProperty(ItemList.LIGHT_BOW.get(), 
				new ResourceLocation("pull"), (stack, world, living) -> {
					if (living == null) {
						return 0.0F;
					} else {
						return living.getActiveItemStack() != stack ? 0.0F : (float) (stack.getUseDuration() - living.getItemInUseCount()) / 20.0F;
					}
				});

		ItemModelsProperties.registerProperty(ItemList.LIGHT_BOW.get(), 
				new ResourceLocation("pulling"), (stack, world, living) -> 
		living != null && living.isHandActive() && living.getActiveItemStack() == stack ? 1.0F : 0.0F);


		ItemModelsProperties.registerProperty(ItemList.DAYNIGHT_CHESTPLATE.get(), 
				new ResourceLocation(ElementalCombatWeaponry.MOD_ID, "worldtick"), (stack, world, living) -> 
		living != null ? living.getEntityWorld().getDayTime() : 0F);


		ElementalCombatWeaponry.LOGGER.info("Item Models Properties registered");
	}
}
