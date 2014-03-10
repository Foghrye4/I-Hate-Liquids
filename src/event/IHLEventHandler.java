package assets.i_hate_liquids.src.event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import ibxm.Player;


public class IHLEventHandler 
{
	//Debug purpose only.
	/*@ForgeSubscribe
	public void onPlayerclick(PlayerInteractEvent event) 
	{
		World world = event.entityPlayer.worldObj;
		int msgId=event.entityPlayer.getCurrentEquippedItem().itemID;
		event.entityPlayer.addChatMessage("id="+msgId);
	}*/
	
	
	@ForgeSubscribe
	public void onBlockBreak(BreakEvent event) 
	{
			Material currentMaterial=Material.water;
			int startx = event.x;
			int starty = event.y;
			int startz = event.z;
			World world = event.world;
			boolean startSearch = false;
			if(IHLInvisibleMagican.getFlowDecay(world, startx+1, starty, startz)>=0)
			{
				startx++;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx, starty, startz+1)>=0)
			{
				startz++;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx+1, starty+1, startz)>=0)
			{
				startx++;
				starty++;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx, starty+1, startz+1)>=0)
			{
				startz++;
				starty++;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx-1, starty, startz)>=0)
			{
				startx--;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx, starty, startz-1)>=0)
			{
				startz--;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx-1, starty+1, startz)>=0)
			{
				startx--;
				starty++;
				startSearch = true;
			}
			if(IHLInvisibleMagican.getFlowDecay(world, startx, starty+1, startz-1)>=0)
			{
				startz--;
				starty++;
				startSearch = true;
			}
			if(startSearch)
			{
			world.spawnEntityInWorld(new IHLInvisibleMagican(world, (double)startx + 0.5D, (double)starty + 0.5D, (double)startz + 0.5D,event.x,event.y,event.z));
			startSearch = false;
			}
	}

		@ForgeSubscribe
	    public void onBucketUse(FillBucketEvent event) 
		{
			ItemStack result = null;
			if(event.current.itemID==325)
			{
				result = fillBucket(event.world, event.target);
			} else {return;}
            
            if (result == null)
             return;
            
            event.result = result;
            event.setResult(Result.ALLOW);
           }

           public ItemStack fillBucket(World world, MovingObjectPosition pos)
           {
            int blockID = world.getBlockId(pos.blockX, pos.blockY+1, pos.blockZ);
            
            if ((blockID == Block.waterStill.blockID) && world.getBlockMetadata(pos.blockX, pos.blockY+1, pos.blockZ) > 0)
            {
            	if(searchSourceAndDestroy(world, pos.blockX, pos.blockY+1, pos.blockZ)){
            		return new ItemStack(Item.bucketWater);
            	}
            	else
            	return null;
            }
            else if ((blockID == Block.lavaStill.blockID) && world.getBlockMetadata(pos.blockX, pos.blockY+1, pos.blockZ) > 0)
            	{
            	if(searchSourceAndDestroy(world, pos.blockX, pos.blockY+1, pos.blockZ)){
            		return new ItemStack(Item.bucketLava);
            	}
            	else
            	return null;
            	}
            	else
            return null;
            }


		private boolean searchSourceAndDestroy(World world, int startx, int starty, int startz) 
		{
			int currentFlowDecay=getFlowDecay(world, startx, starty, startz);
			int currentBlockID=world.getBlockId(startx, starty, startz);
			for (int i=0; i<64;i++)
				{
				//Go search up, if possible.
					if(getFlowDecay(world, startx, starty+1, startz)>=0)
					{
						starty++;
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					
					else if(getFlowDecay(world, startx+1, starty+1, startz)>=0)
					{
						starty++;
						startx++;						
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					
					else if(getFlowDecay(world, startx-1, starty+1, startz)>=0)
					{
						starty++;
						startx--;						
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					
					else if(getFlowDecay(world, startx, starty+1, startz+1)>=0)
					{
						starty++;
						startz++;						
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					
					else if(getFlowDecay(world, startx, starty+1, startz-1)>=0)
					{
						starty++;
						startz--;						
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					//Start checking neighbor blocks to lower flow decay.
					else if(getFlowDecay(world, startx-1, starty, startz)<currentFlowDecay&&getFlowDecay(world, startx-1, starty, startz)!=-1)
						{
							startx--;
							currentFlowDecay=getFlowDecay(world, startx, starty, startz);
						}
					else if(getFlowDecay(world, startx, starty, startz+1)<currentFlowDecay&&getFlowDecay(world, startx, starty, startz+1)!=-1)
						{
							startz++;
							currentFlowDecay=getFlowDecay(world, startx, starty, startz);
						}
					else if(getFlowDecay(world, startx, starty, startz-1)<currentFlowDecay&&getFlowDecay(world, startx, starty, startz-1)!=-1)
						{
							startz--;
							currentFlowDecay=getFlowDecay(world, startx, starty, startz);
						}
					else if(getFlowDecay(world, startx+1, starty, startz)<currentFlowDecay&&getFlowDecay(world, startx+1, starty, startz)!=-1)
					{
						startx++;
						currentFlowDecay=getFlowDecay(world, startx, starty, startz);
					}
					else {i=64;}
					
				}
			if(getFlowDecay(world, startx, starty, startz)==0)
			{
				world.setBlockToAir(startx, starty, startz);
				return true;	
			} else {return false;} 
		}


	    protected int getFlowDecay(World par1World, int par2, int par3, int par4)
	    {
	    	return  par1World.getBlockMaterial(par2, par3, par4).isLiquid() ? par1World.getBlockMetadata(par2, par3, par4) : -1;
	    }

}
