package minecraftscalalib

import net.minecraft.util.Facing
import net.minecraft.world.World
import net.minecraft.tileentity.TileEntity



object Helpful {
  
  implicit def makeABetterWorld(w:World) = new BetterWorld(w)
  
  class BetterWorld(w:World) {
    def isAirBlock(c:Coordinates) = w.isAirBlock(c.x, c.y, c.z)
    def getBlockId(c:Coordinates) = w.getBlockId(c.x, c.y, c.z)
    def getBlockMetadata(c:Coordinates) = w.getBlockMetadata(c.x, c.y, c.z)
    def blockHasTileEntity(c:Coordinates) = w.blockHasTileEntity(c.x, c.y, c.z)
    
    def setBlock(c:Coordinates, blockId:Int, metadata:Int, flags:Int):Boolean = w.setBlock(c.x, c.y, c.z, blockId, metadata, flags)
    def setBlockTileEntity(c:Coordinates, enity:TileEntity) = w.setBlockTileEntity(c.x, c.y, c.z, enity)
  }

  case class Coordinates(world:World, x:Int, y:Int, z:Int) {
    
    
    def getoffsetCoordinatesForFacing(facing:Int):Coordinates = {
      new Coordinates(world,Facing.offsetsXForSide(facing),
            Facing.offsetsYForSide(facing),
            Facing.offsetsZForSide(facing))
    }
    
    def isAirBlock() = world.isAirBlock(this)
    
    def getBlockId() = world.getBlockId(this)
    
    
    

    
    def getCorrdinatesFromFacing(facing:Int, dist:Int) = {
 //    getoffsetCoordinatesForFacing()
    }
  }
  
  
}