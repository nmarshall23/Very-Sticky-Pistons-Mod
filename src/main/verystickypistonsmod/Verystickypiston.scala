package verystickypistonsmod

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.Init
import cpw.mods.fml.common.Mod.PostInit
import cpw.mods.fml.common.Mod.PreInit
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkMod
import net.minecraftforge.common.Configuration
import cpw.mods.fml.common.registry.LanguageRegistry

@Mod(modid="verystickypistonsmod", name="VeryStickyPistonsMod", version="0.0.1", modLanguage = "scala")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
object VeryStickyPiston {
        
   val veryStickyPiston = new BlockVeryStickyPiston(5100).setUnlocalizedName("specificItem")
 
   @PreInit
   def preInit(event: FMLPreInitializationEvent) {

   }
        
   @Init
   def init(event: FMLInitializationEvent) {
       LanguageRegistry.addName(veryStickyPiston, "Very Sticky Piston");
   }
        
   @PostInit
   def postInit(event:FMLPostInitializationEvent) {
                
   }
}
