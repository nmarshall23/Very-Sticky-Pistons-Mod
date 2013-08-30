package verystickypistonsmod

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import java.util.Random
import net.minecraft.block.BlockPistonBase
import net.minecraft.item.ItemStack
import net.minecraft.entity.EntityLivingBase

class BlockVeryStickyPiston(val block_ID:Int) extends BlockPistonBase(block_ID, true) {

/**
 * Called whenever the block is added into the world. Args: world, x, y, z
 */
 override def onBlockAdded(w:World, x:Int, y:Int, z:Int)
 {
     if (!w.isRemote && w.getBlockTileEntity(x, y, z) == null)
     {
            this.updatePistonState(w, x, y, z)
     }
  }
 
 /**
  * Called when the block is placed in the world.
  * Args: World, x, y, z, EntitythatPlaced, ItemStack
  */
 override def onBlockPlacedBy(w:World, x:Int, y:Int, z:Int, entity:EntityLivingBase, stack: ItemStack)
    {
        val l = BlockPistonBase.determineOrientation(w, x, y, z, entity)
        w.setBlockMetadataWithNotify(x, y, z, l, 2)

        if (!w.isRemote)
        {
           this.updatePistonState(w, x, y, z)
        }
    }
    
  
  /**
   * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
   * their own) Args: World, x, y, z, Facing 
   */
  override def onNeighborBlockChange(w:World, x:Int, y:Int, z:Int, Facing:Int) = {
    if (!w.isRemote)
        {
    		this.updatePistonState(w, x, y, z)
        }
  }
  
  /**
     * handles attempts to extend or retract the piston.
     */
  private def updatePistonState(w:World, x:Int, y:Int, z:Int) {
     val facing = BlockPistonBase.getOrientation(w.getBlockMetadata(x,y,z))
     
  }
  
}