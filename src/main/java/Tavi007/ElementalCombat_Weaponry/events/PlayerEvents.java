package Tavi007.ElementalCombat.events;

import java.util.HashMap;

import Tavi007.ElementalCombat.ElementalCombat;
import Tavi007.ElementalCombat.ElementalCombatAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackData;
import Tavi007.ElementalCombat.capabilities.defense.DefenseData;
import Tavi007.ElementalCombat.config.ClientConfig;
import Tavi007.ElementalCombat.init.StartupClientOnly;
import Tavi007.ElementalCombat.loading.ItemCombatProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ElementalCombat.MOD_ID, bus = Bus.FORGE)
public class PlayerEvents
{
	@SubscribeEvent
	public static void livingEquipmentChange(LivingEquipmentChangeEvent event) {
		//change defense properties
		LivingEntity entity = event.getEntityLiving();
		if(event.getSlot().getSlotType() == EquipmentSlotType.Group.ARMOR)
		{
			// get data
			DefenseData defDataItemFrom = ElementalCombatAPI.getDefenseData(event.getFrom());
			DefenseData defDataItemTo = ElementalCombatAPI.getDefenseData(event.getTo());

			// compute change
			DefenseData newData = new DefenseData();
			newData.substract(defDataItemFrom);
			newData.add(defDataItemTo);

			// apply change
			ElementalCombatAPI.addDefenseData(entity, newData);
		}
	}

	// every armor piece (curios and vanilla) will be re-applied on log in
	// same goes for any auras (WIP)
	// this is why, all this stuff need to be removed on log out.
	@SubscribeEvent
	public static void playerLoggedOut(PlayerLoggedOutEvent event) {
		PlayerEntity entity = event.getPlayer();
		if (entity != null) {
			DefenseData defCapEntity = ElementalCombatAPI.getDefenseData(entity);
			defCapEntity.clear();
		}
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		if(StartupClientOnly.TOGGLE_HUD.isKeyDown()){
			ClientConfig.toogleHUD();
		}
	}
	
	@SubscribeEvent
	public static void onItemCrafted(ItemCraftedEvent event) {
		ItemStack stack = event.getCrafting();
		ItemCombatProperties itemProperties = ElementalCombatAPI.getDefaultProperties(stack);

		DefenseData defData = ElementalCombatAPI.getDefenseData(stack);
		defData.setElementFactor(new HashMap<String, Integer>(itemProperties.getDefenseElement()));
		defData.setElementFactor(new HashMap<String, Integer>(itemProperties.getDefenseStyle()));
		
		AttackData atckData = ElementalCombatAPI.getAttackData(stack);
		final AttackData atck = new AttackData(itemProperties.getAttackStyle(), itemProperties.getAttackElement());
		atckData.set(atck);
	}
}
