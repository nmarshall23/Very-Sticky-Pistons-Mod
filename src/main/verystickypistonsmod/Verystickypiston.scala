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

@Mod(modid="verystickypistonsmod", name="VeryStickyPistonsMod", version="0.0.1", modLanguage = "scala")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
object VeryStickyPiston {
        
        
 
   @PreInit
   def preInit(event: FMLPreInitializationEvent) {

   }
        
   @Init
   def init(event: FMLInitializationEvent) {

   }
        
   @PostInit
   def postInit(event:FMLPostInitializationEvent) {
                
   }
}