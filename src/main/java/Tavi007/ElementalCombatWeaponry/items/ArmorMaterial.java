package Tavi007.ElementalCombatWeaponry.items;

import java.util.function.Supplier;

import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import Tavi007.ElementalCombatWeaponry.init.ItemList;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

public enum ArmorMaterial implements IArmorMaterial {

    // needs balance changes
    CLOCK("clock", 30, new int[] { 2, 6, 7, 3 }, 9, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.CLOCK)),
    CREEPER("creeper", 30, new int[] { 2, 6, 7, 3 }, 12, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.CREEPER_HEAD)),
    HIKING("hiking", 30, new int[] { 2, 6, 7, 3 }, 9, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F,
            () -> Ingredient.of(ItemList.ESSENCE_EARTH.get())),
    IRON("iron", 30, new int[] { 2, 6, 7, 3 }, 12, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.IRON_INGOT)),
    MIRROR("mirror", 30, new int[] { 2, 6, 7, 3 }, 9, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.GLASS)),
    PAPER("paper", 30, new int[] { 2, 6, 7, 3 }, 25, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.PAPER)),
    RUBBER("rubber", 30, new int[] { 2, 6, 7, 3 }, 12, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.LEATHER)),
    SUNGLASS("sunglass", 30, new int[] { 2, 6, 7, 3 }, 12, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(Items.GLASS)),
    WOOL("wool", 30, new int[] { 2, 6, 7, 3 }, 12, SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 0.0F, () -> Ingredient.of(ItemTags.WOOL));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairMaterialLazy;

    private ArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountArrayIn, int enchantabilityIn, SoundEvent soundEventIn,
            float toughnessIn, float knockbackResistanceIn, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = ElementalCombatWeaponry.MOD_ID + ":" + nameIn;
        this.maxDamageFactor = maxDamageFactorIn;
        this.damageReductionAmountArray = damageReductionAmountArrayIn;
        this.enchantability = enchantabilityIn;
        this.soundEvent = soundEventIn;
        this.toughness = toughnessIn;
        this.knockbackResistance = knockbackResistanceIn;
        this.repairMaterialLazy = Lazy.concurrentOf(repairMaterialSupplier);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterialLazy.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
