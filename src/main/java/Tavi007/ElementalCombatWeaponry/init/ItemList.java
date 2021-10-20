package Tavi007.ElementalCombatWeaponry.init;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import Tavi007.ElementalCombatWeaponry.items.StyleSwitchingSword;
import Tavi007.ElementalCombatWeaponry.items.ArmorMaterial;
import Tavi007.ElementalCombatWeaponry.items.BiomeDependentArmor;
import Tavi007.ElementalCombatWeaponry.items.CopyAttackElementItem;
import Tavi007.ElementalCombatWeaponry.items.DayNightArmor;
import Tavi007.ElementalCombatWeaponry.items.ElementSwitchingSword;
import Tavi007.ElementalCombatWeaponry.items.MirrorArmor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemList {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ElementalCombatWeaponry.MOD_ID);

	private static Properties singleStack = new Item.Properties().group(ElementalCombatWeaponry.ELEMENTAL_COMBAT_GROUP).maxStackSize(1);
	private static Properties singleStackWMaxDmg = new Item.Properties().group(ElementalCombatWeaponry.ELEMENTAL_COMBAT_GROUP).maxStackSize(1);
	private static Properties fullStack = new Item.Properties().group(ElementalCombatWeaponry.ELEMENTAL_COMBAT_GROUP).maxStackSize(64);
	
	//essence
	public static final RegistryObject<Item> ESSENCE_FIRE 		= ITEMS.register("essence_fire", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_ICE 		= ITEMS.register("essence_ice", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_WATER 		= ITEMS.register("essence_water", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_THUNDER 	= ITEMS.register("essence_thunder", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_DARKNESS 	= ITEMS.register("essence_darkness", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_LIGHT 		= ITEMS.register("essence_light", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_EARTH 		= ITEMS.register("essence_earth", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_WIND 		= ITEMS.register("essence_wind", () -> new Item(fullStack));
	public static final RegistryObject<Item> ESSENCE_FLORA 		= ITEMS.register("essence_flora", () -> new Item(fullStack));

	//weapons
	public static final RegistryObject<Item> WOODEN_SPEAR 	= ITEMS.register("wooden_spear", () -> new SwordItem(ItemTier.WOOD, 3, -2.4F, singleStack));
	public static final RegistryObject<Item> STONE_SPEAR 	= ITEMS.register("stone_spear", () -> new SwordItem(ItemTier.STONE, 3, -2.4F, singleStack));
	public static final RegistryObject<Item> IRON_SPEAR 	= ITEMS.register("iron_spear", () -> new SwordItem(ItemTier.IRON, 3, -2.4F, singleStack));
	public static final RegistryObject<Item> GOLDEN_SPEAR 	= ITEMS.register("golden_spear", () -> new SwordItem(ItemTier.GOLD, 3, -2.4F, singleStack));
	public static final RegistryObject<Item> DIAMOND_SPEAR 	= ITEMS.register("diamond_spear", () -> new SwordItem(ItemTier.DIAMOND, 3, -2.4F, singleStack));
	
	public static final String[] halberdStylesArray = new String[] {"slash", "stab"};
	public static final Set<String> halberdStyles = new HashSet<>(Arrays.asList(halberdStylesArray));
	public static final RegistryObject<Item> WOODEN_HALBERD 	= ITEMS.register("wooden_halberd", () -> new StyleSwitchingSword(ItemTier.WOOD, 5, -2.8F, halberdStyles, singleStack));
	public static final RegistryObject<Item> STONE_HALBERD 		= ITEMS.register("stone_halberd", () -> new StyleSwitchingSword(ItemTier.STONE, 5, -2.8F, halberdStyles, singleStack));
	public static final RegistryObject<Item> IRON_HALBERD 		= ITEMS.register("iron_halberd", () -> new StyleSwitchingSword(ItemTier.IRON, 5, -2.8F, halberdStyles, singleStack));
	public static final RegistryObject<Item> GOLDEN_HALBERD 	= ITEMS.register("golden_halberd", () -> new StyleSwitchingSword(ItemTier.GOLD, 5, -2.8F, halberdStyles, singleStack));
	public static final RegistryObject<Item> DIAMOND_HALBERD 	= ITEMS.register("diamond_halberd", () -> new StyleSwitchingSword(ItemTier.DIAMOND, 5, -2.8F, halberdStyles, singleStack));
	
	// need balance changes
	public static final RegistryObject<Item> WATER_STAFF 		= ITEMS.register("water_staff", () -> new CopyAttackElementItem(ItemTier.WOOD, singleStackWMaxDmg.maxDamage(1000)));
	public static final RegistryObject<Item> QUAKE_HAMMER 		= ITEMS.register("quake_hammer", () -> new SwordItem(ItemTier.WOOD, 10, -3.0F, singleStackWMaxDmg.maxDamage(1000)));
	public static final RegistryObject<Item> THORN_CLUB 		= ITEMS.register("thorn_club", () -> new SwordItem(ItemTier.WOOD, 8, -2.7F, singleStackWMaxDmg.maxDamage(1000)));
	public static final RegistryObject<Item> GUST_SWORD 		= ITEMS.register("gust_sword", () -> new SwordItem(ItemTier.WOOD, 6, -2.0F, singleStackWMaxDmg.maxDamage(1000)));
	public static final RegistryObject<Item> LIGHTNING_DAGGER 	= ITEMS.register("lightning_dagger", () -> new SwordItem(ItemTier.WOOD, 2, -0.6F, singleStackWMaxDmg.maxDamage(1000)));
	public static final RegistryObject<Item> VOID_AXE 			= ITEMS.register("void_axe", () -> new AxeItem(ItemTier.DIAMOND, 5.0F, -3.0F, singleStack));
	public static final RegistryObject<Item> LIGHT_BOW 			= ITEMS.register("light_bow", () -> new BowItem(singleStack));
	
	public static final String[] fireAndIceArray = new String[] {"fire", "ice"};
	public static final Set<String> fireAndIce = new HashSet<>(Arrays.asList(fireAndIceArray));
	public static final RegistryObject<Item> FIREANDICE_SWORD = ITEMS.register("fire_and_ice_sword", () -> new ElementSwitchingSword(ItemTier.IRON, 3, -2.4F, fireAndIce, singleStack));
	
	// armor
	// single pieces
	public static final RegistryObject<Item> SUNGLASSES 			= ITEMS.register("sunglasses", () -> new ArmorItem(ArmorMaterial.SUNGLASS, EquipmentSlotType.HEAD, singleStack));
	public static final RegistryObject<Item> OFUDA					= ITEMS.register("ofuda", () -> new ArmorItem(ArmorMaterial.PAPER, EquipmentSlotType.HEAD, singleStack));
	
	public static final RegistryObject<Item> WETSUIT_CHESTPLATE		= ITEMS.register("wetsuit_chestplate", () -> new ArmorItem(ArmorMaterial.RUBBER, EquipmentSlotType.CHEST, singleStack));
	public static final RegistryObject<Item> DAYNIGHT_CHESTPLATE 	= ITEMS.register("daynight_chestplate", () -> new DayNightArmor(ArmorMaterial.CLOCK, EquipmentSlotType.CHEST, singleStack));

	public static final RegistryObject<Item> WETSUIT_LEGGINGS 		= ITEMS.register("wetsuit_leggings", () -> new ArmorItem(ArmorMaterial.RUBBER, EquipmentSlotType.LEGS, singleStack));
	public static final RegistryObject<Item> COOLING_LEGGINGS 		= ITEMS.register("cooling_leggings", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.LEGS, singleStack));
	
	public static final RegistryObject<Item> RUBBER_BOOTS 			= ITEMS.register("rubber_boots", () -> new ArmorItem(ArmorMaterial.RUBBER, EquipmentSlotType.FEET, singleStack));
	public static final RegistryObject<Item> HEAVY_BOOTS			= ITEMS.register("heavy_boots", () -> new ArmorItem(ArmorMaterial.IRON, EquipmentSlotType.FEET, singleStack));
	
	// full sets
	public static final RegistryObject<Item> CREEPER_CHESTPLATE = ITEMS.register("creeper_chestplate", () -> new ArmorItem(ArmorMaterial.CREEPER, EquipmentSlotType.CHEST, singleStack));
	public static final RegistryObject<Item> CREEPER_LEGGINS 	= ITEMS.register("creeper_leggings", () -> new ArmorItem(ArmorMaterial.CREEPER, EquipmentSlotType.LEGS, singleStack));
	public static final RegistryObject<Item> CREEPER_BOOTS 		= ITEMS.register("creeper_boots", () -> new ArmorItem(ArmorMaterial.CREEPER, EquipmentSlotType.FEET, singleStack));
	
	public static final RegistryObject<Item> WOOL_HELMET 		= ITEMS.register("wool_helmet", () -> new ArmorItem(ArmorMaterial.WOOL, EquipmentSlotType.HEAD, singleStack));
	public static final RegistryObject<Item> WOOL_CHESTPLATE 	= ITEMS.register("wool_chestplate", () -> new ArmorItem(ArmorMaterial.WOOL, EquipmentSlotType.CHEST, singleStack));
	public static final RegistryObject<Item> WOOL_LEGGINS 		= ITEMS.register("wool_leggings", () -> new ArmorItem(ArmorMaterial.WOOL, EquipmentSlotType.LEGS, singleStack));
	public static final RegistryObject<Item> WOOL_BOOTS 		= ITEMS.register("wool_boots", () -> new ArmorItem(ArmorMaterial.WOOL, EquipmentSlotType.FEET, singleStack));
	
	public static final RegistryObject<Item> HIKING_HAT			= ITEMS.register("hiking_hat", () -> new BiomeDependentArmor(ArmorMaterial.HIKING, EquipmentSlotType.HEAD, singleStack));
	public static final RegistryObject<Item> HIKING_VEST		= ITEMS.register("hiking_vest", () -> new BiomeDependentArmor(ArmorMaterial.HIKING, EquipmentSlotType.CHEST, singleStack));
	public static final RegistryObject<Item> HIKING_SHORTS	 	= ITEMS.register("hiking_shorts", () -> new BiomeDependentArmor(ArmorMaterial.HIKING, EquipmentSlotType.LEGS, singleStack));
	public static final RegistryObject<Item> HIKING_BOOTS		= ITEMS.register("hiking_boots", () -> new BiomeDependentArmor(ArmorMaterial.HIKING, EquipmentSlotType.FEET, singleStack));

	public static final RegistryObject<Item> MIRROR_HELMET		= ITEMS.register("mirror_helmet", () -> new MirrorArmor(ArmorMaterial.MIRROR, EquipmentSlotType.HEAD, singleStack));
	public static final RegistryObject<Item> MIRROR_CHESTPLATE	= ITEMS.register("mirror_chestplate", () -> new MirrorArmor(ArmorMaterial.MIRROR, EquipmentSlotType.CHEST, singleStack));
	public static final RegistryObject<Item> MIRROR_LEGGINS	 	= ITEMS.register("mirror_leggings", () -> new MirrorArmor(ArmorMaterial.MIRROR, EquipmentSlotType.LEGS, singleStack));
	public static final RegistryObject<Item> MIRROR_BOOTS		= ITEMS.register("mirror_boots", () -> new MirrorArmor(ArmorMaterial.MIRROR, EquipmentSlotType.FEET, singleStack));
}
