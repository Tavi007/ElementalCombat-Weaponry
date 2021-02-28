package Tavi007.ElementalCombat_Weaponry.events;

import java.util.Collection;
import java.util.HashMap;

import Tavi007.ElementalCombat.ElementalCombatAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackData;
import Tavi007.ElementalCombat.capabilities.defense.DefenseData;
import Tavi007.ElementalCombat.config.ServerConfig;
import Tavi007.ElementalCombat.loading.DamageSourceCombatProperties;
import Tavi007.ElementalCombat_Weaponry.ElementalCombatWeaponry;
import Tavi007.ElementalCombat_Weaponry.init.ItemList;
import Tavi007.ElementalCombat_Weaponry.items.MirrorArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ElementalCombatWeaponry.MOD_ID, bus = Bus.FORGE)
public class ServerEvents {

	@SubscribeEvent
	public static void onLivingDropsEvent(LivingDropsEvent event) {
		if(!(event.getEntityLiving() instanceof PlayerEntity)) {
			AttackData atckData = ElementalCombatAPI.getAttackDataWithActiveItem(event.getEntityLiving());
			addEssenceDropToList(atckData.getElement(), event.getEntityLiving(), event.getDrops(), event.getLootingLevel());

			DefenseData defData = ElementalCombatAPI.getDefenseData(event.getEntityLiving());
			defData.getElementFactor().forEach((element,factor) -> {
				if(factor > 0) {
					addEssenceDropToList(element, event.getEntityLiving(), event.getDrops(), event.getLootingLevel());
				}
			}); 
		}
	}

	private static void addEssenceDropToList(String element, LivingEntity entity, Collection<ItemEntity> drops, int lootingLevel) {	
		int numberOfDrops = 1 + lootingLevel;

		if (numberOfDrops > 0) {
			double x = entity.getPosX();
			double y = entity.getPosY();
			double z = entity.getPosZ();

			switch(element) {
			case "fire":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_FIRE.get(), numberOfDrops)));
				break;
			case "ice":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_ICE.get(), numberOfDrops)));
				break;
			case "water":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_WATER.get(), numberOfDrops)));
				break;
			case "thunder":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_THUNDER.get(), numberOfDrops)));
				break;
			case "darkness":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_DARKNESS.get(), numberOfDrops)));
				break;
			case "light":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_LIGHT.get(), numberOfDrops)));
				break;
			case "earth":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_EARTH.get(), numberOfDrops)));
				break;
			case "wind":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_WIND.get(), numberOfDrops)));
				break;
			case "flora":
				drops.add(new ItemEntity(entity.getEntityWorld(), x, y, z, new ItemStack(ItemList.ESSENCE_FLORA.get(), numberOfDrops)));
				break;
			}
		}
	}

	@SubscribeEvent
	public static void elementifyLivingHurtEvent(LivingHurtEvent event) {
		DamageSource damageSource = event.getSource();
		Entity immediateSource = damageSource.getImmediateSource();
		
		// Get combat data from source
		String sourceElement;
		String sourceStyle;
		if(immediateSource instanceof LivingEntity) {
			AttackData atckCap = ElementalCombatAPI.getAttackDataWithActiveItem((LivingEntity) immediateSource);
			sourceStyle = atckCap.getStyle();
			sourceElement = atckCap.getElement();
		}
		else if(immediateSource instanceof ProjectileEntity) {
			AttackData atckCap = ElementalCombatAPI.getAttackData((ProjectileEntity) immediateSource);
			sourceStyle = atckCap.getStyle();
			sourceElement = atckCap.getElement();
		}
		else {
			DamageSourceCombatProperties damageSourceProperties = ElementalCombatAPI.getDefaultProperties(damageSource);
			sourceStyle = damageSourceProperties.getAttackStyle();
			sourceElement = damageSourceProperties.getAttackElement();
		}

		//default values in case style or element is empty (which should not happen)
		if (sourceStyle.isEmpty()) {sourceStyle = ServerConfig.getDefaultStyle();}
		if (sourceElement.isEmpty()) {sourceElement = ServerConfig.getDefaultElement();}
		// for mirror armor
		final String element = sourceElement;
		final String style = sourceStyle;
		
		event.getEntityLiving().getArmorInventoryList().forEach( armorStack -> {
			if(armorStack.getItem() instanceof MirrorArmor) {
				DefenseData armorDef = ElementalCombatAPI.getDefenseData(armorStack);
				HashMap<String, Integer> elemMap = new HashMap<String, Integer>();
				HashMap<String, Integer> styleMap = new HashMap<String, Integer>();
				elemMap.put(element, ServerConfig.getMaxFactor()/10);
				styleMap.put(style, ServerConfig.getMaxFactor()/10);
				armorDef.setElementFactor(elemMap);
				armorDef.setStyleFactor(styleMap);
			}
		});
	}
}