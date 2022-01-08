package Tavi007.ElementalCombatWeaponry.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import Tavi007.ElementalCombat.api.AttackDataAPI;
import Tavi007.ElementalCombat.api.DefenseDataAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackLayer;
import Tavi007.ElementalCombat.capabilities.defense.DefenseLayer;
import Tavi007.ElementalCombat.config.ServerConfig;
import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import Tavi007.ElementalCombatWeaponry.init.ItemList;
import Tavi007.ElementalCombatWeaponry.items.MirrorArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {

            AttackLayer atckData = AttackDataAPI.getFullDataAsLayer(event.getEntityLiving());
            addEssenceDropToList(atckData.getElement(), event.getEntityLiving(), event.getDrops(), event.getLootingLevel());

            DefenseLayer defData = DefenseDataAPI.getFullDataAsLayer(event.getEntityLiving());
            defData.getElementFactor().forEach((element, factor) -> {
                if (factor > 0) {
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

            switch (element) {
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

    // for the Mirror Armor
    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent event) {
        List<ItemStack> mirrorStacks = new ArrayList<ItemStack>();
        event.getEntityLiving().getArmorInventoryList().forEach(armorStack -> {
            if (armorStack.getItem() instanceof MirrorArmor) {
                mirrorStacks.add(armorStack);
            }
        });

        if (!mirrorStacks.isEmpty()) {
            DamageSource damageSource = event.getSource();
            AttackLayer attackLayer = AttackDataAPI.getFullDataAsLayer(damageSource);
            HashMap<String, Integer> elemMap = new HashMap<String, Integer>();
            HashMap<String, Integer> styleMap = new HashMap<String, Integer>();
            elemMap.put(attackLayer.getElement(), ServerConfig.getMaxFactor() / 10);
            styleMap.put(attackLayer.getStyle(), ServerConfig.getMaxFactor() / 10);
            DefenseLayer defenseLayer = new DefenseLayer(styleMap, elemMap);
            mirrorStacks.forEach(stack -> {
                DefenseDataAPI.putLayer(stack, defenseLayer, ElementalCombatWeaponry.ARMOR_RESOURCE_LOCATION);
            });
        }

    };
}
