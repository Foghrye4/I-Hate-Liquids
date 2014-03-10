package assets.i_hate_liquids.src;
import assets.i_hate_liquids.src.event.IHLEventHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

@Mod(modid=IHLModInfo.MODID, name=IHLModInfo.MODNAME, version=IHLModInfo.MODVERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class IHLMod {
	public static IHLMod instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		MinecraftForge.EVENT_BUS.register(new IHLEventHandler());
				
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
	}
}