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
import net.minecraft.util.Facing
import minecraftscalalib.Helpful._
import net.minecraft.block.Block
import net.minecraft.block.BlockPistonMoving

class BlockVeryStickyPiston(val block_ID:Int) extends BlockPistonBase(block_ID, true) {

  private val BLOCK_MOVING_LIMIT = 13
  
   def IsIndirectlyPowered(c:Coordinates, eventParam:Int): Boolean = {
      c match {
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y - 1,z,  0)    && eventParam != 0) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y + 1,z,  1)    && eventParam != 1) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y    ,z - 1, 2) && eventParam != 2) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y    ,z + 1, 3) && eventParam != 3) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x + 1,y    ,z    , 5) && eventParam != 5) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x - 1,y    ,z    , 4) && eventParam != 4) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y    ,z    , 0) && eventParam != 0) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y + 2,z    , 1) && eventParam != 4) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y + 1,z - 1, 2)) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x    ,y + 1,z + 1, 3)) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x -1 ,y + 1,z    , 4)) => true
        case Coordinates(w, x, y, z) if (w.getIndirectPowerOutput(x + 1,y + 1,z    , 5)) => true
        case _ => false
      }
    }

  /**
    * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
    * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
    */
	override def onBlockEventReceived(w:World, x:Int, y:Int, z:Int, blockID:Int, eventID:Int, eventParam:Int): Boolean = {
     val isIndPowered = this.IsIndirectlyPowered(Coordinates(w, x,y,z), eventID)
    
    eventID match {
       //Client acts
      case 1 if(!w.isRemote && isIndPowered ) => {
        w.setBlockMetadataWithNotify(z, y, z, eventParam | 8, 2)
        false
      }
      case 0 if(!w.isRemote && !isIndPowered ) => false
        //Server
        
      case 0 if(!tryExtend(w, x, y, z, eventParam))=> false
       //Extends
      case 0 if(tryExtend(w, x, y, z, eventParam))=> {
        w.setBlockMetadataWithNotify(z, y, z, eventParam | 8, 2)
        true
      }
       // Lots happens here, need to finish
       // Retracts
      case 1 if(!tryRetract() ) => false 
      case 1 if(tryRetract() )  => true
      
      case _ => false
    }
	  
	}
	
	//Need to finish
	private def tryRetract():Boolean = {
	  true
	}
	
	/**
     * attempts to extend the piston. returns false if impossible.
     */
    private def tryExtend(w:World, x:Int, y:Int, z:Int, f:Int): Boolean = {
      
      val x1 = x + Facing.offsetsXForSide(f)
      val y1 = y + Facing.offsetsYForSide(f)
      val z1 = z + Facing.offsetsZForSide(f)
      
    
      val blocks = getBlocksToPush(new Coordinates(w,x1,y1,z1),f)
      
      if(blocks.isEmpty) { 
        return false
      }
      
      val revb = blocks.reverse
      
      revb foreach{ b =>
    	
    	val m = b.getoffsetCoordinatesForFacing(f); 
    	val blockId = b.getBlockId()
    	val metadata = b.world.getBlockMetadata(b);
    	b.world.setBlock(m, Block.pistonMoving.blockID, metadata, 4);
    	val te = BlockPistonMoving.getTileEntity(m.x, m.y, m.z, true, false)
    	b.world.setBlockTileEntity(m, te)
    	b.world.notifyBlocksOfNeighborChange(m.x, m.y, m.z, blockId)
      }
        
      
      true
    }
    
    /**
     * Get's blocks to Move
     */
    private def getBlocksToPush(fromPos:Coordinates, f:Int):List[Coordinates] = {
      
      val dist = 0 to BLOCK_MOVING_LIMIT 
      val fistCoord = List[Coordinates](fromPos.getoffsetCoordinatesForFacing(f))
      
      val coordList = dist.foldLeft(fistCoord){
        case (list,b:Int) => list.head.getoffsetCoordinatesForFacing(f) :: list
      }
      
      val blocks = coordList.takeWhile{ c =>
        canMoveBlockAtCoordinates(c) && canBlockAtCoordinatesBePushedInto(c)
      }
      
      if(canPushIntoLastCoord(blocks.lastOption,f).isEmpty) {
        List()
      }
      else {
        blocks
      }
      
    }
    
    def canPushIntoLastCoord(lastBlock:Option[Coordinates],facing:Int):Option[Coordinates] = {
      for(
          x <- lastBlock;
          y = x.getoffsetCoordinatesForFacing(facing)
          if canBlockAtCoordinatesBePushedInto(y)
          ) yield y
      
      } 
    
    /**
     * False means that it can't be pushed, and am blocked to move it
     * 
     */
    def canMoveBlockAtCoordinates(c:Coordinates):Boolean = {
      c.getBlockId match {
        case Block.obsidian.blockID => false
        case b if (Block.blocksList(b).getMobilityFlag() == 2) => false
        case _ if(c.world.blockHasTileEntity(c)) => false
        case _ => true
      }
    }
    
    /**
     * False means that it can't be pushed, but pistons can move over it.
     * Thus ends our stack of blocks to move.
     */
    def canBlockAtCoordinatesBePushedInto(c:Coordinates):Boolean = {
      c.getBlockId match {
        case b if (Block.blocksList(b).getMobilityFlag() == 1) => false
        case _ => true
      }
    }

}