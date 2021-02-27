package Tavi007.ElementalCombat_Weaponry.init;

import Tavi007.ElementalCombat_Weaponry.ElementalCombatWeaponry;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleList {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ElementalCombatWeaponry.MOD_ID);

	public static final RegistryObject<BasicParticleType> CRIT_ELEMENT	 	= PARTICLES.register("crit_element", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> CRIT_STYLE 		= PARTICLES.register("crit_style", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> RESIST_ELEMENT	= PARTICLES.register("resist_element", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> RESIST_STYLE		= PARTICLES.register("resist_style", () -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> ABSORB			= PARTICLES.register("absorb", () -> new BasicParticleType(true));
}
