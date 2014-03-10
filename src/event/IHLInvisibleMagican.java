package assets.i_hate_liquids.src.event;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
 
public class IHLInvisibleMagican extends Entity
{
	private int entityAge;
	private int deletedBlock_x;
	private int deletedBlock_y;
	private int deletedBlock_z;

	public IHLInvisibleMagican(World par1World, double par2, double par4, double par6,int deletedBlock_x,int deletedBlock_y,int deletedBlock_z)
    {
        super(par1World);
        this.setSize(0.5F, 0.5F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.deletedBlock_x=deletedBlock_x;
        this.deletedBlock_y=deletedBlock_y;
        this.deletedBlock_z=deletedBlock_z;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    public IHLInvisibleMagican(World par1World)
    {
        super(par1World);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
    }

    protected void entityInit() 
    {
    	this.entityAge = 0;
    }


    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
    	searchAndReplaceLiquidBlock(this.worldObj);
    	++this.entityAge;
        if (this.entityAge >= 6000)
        {
            this.setDead();
        }
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
            return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    }

    public boolean canAttackWithItem()
    {
        return false;
    }
    
    
    public static int getFlowDecay(World par1World, int par2, int par3, int par4)
    {
    	//boolean output;
    	//output = par1World.getBlockMaterial(par2, par3, par4).isLiquid();
    	//output = par1World.getBlockId(par2, par3, par4) == Block.waterStill.blockID || par1World.getBlockId(par2, par3, par4) == Block.lavaStill.blockID;
    	//output = output || par1World.getBlockId(par2, par3, par4) == Block.waterMoving.blockID || par1World.getBlockId(par2, par3, par4) == Block.lavaMoving.blockID;
    	return  par1World.getBlockMaterial(par2, par3, par4).isLiquid() ? par1World.getBlockMetadata(par2, par3, par4) : -1;
    }

    private void searchAndReplaceLiquidBlock(World world) 
{
	boolean startSearch = false;
	int ixt[] = new int[46];
	ixt[0]=this.deletedBlock_x;
	int iyt[] = new int[46];
	iyt[0]=this.deletedBlock_y;
	int izt[] = new int[46];
	izt[0]=this.deletedBlock_z;
	int indx;
	indx = 1;
	int startx=this.deletedBlock_x; 
	int starty=this.deletedBlock_y;
	int startz=this.deletedBlock_z;

	for (int iy=4; iy>=0;iy--)
	{
		for (int ix=0; ix<=2;ix++)
		{
			for (int iz=0; iz<=2;iz++)
			{
				if(getFlowDecay(world, startx+ix-1, starty+iy-2, startz+iz-1)>0)
				{
					startSearch = true;
					ixt[indx]=startx+ix-1;
					iyt[indx]=starty+iy-2;
					izt[indx]=startz+iz-1;
					indx++;
				}
			}
		}
	}
	int currentFlowDecay=getFlowDecay(world, startx, starty, startz);
	int currentBlockID=world.getBlockId(startx, starty, startz);
	if(startSearch)
	{
		startx=ixt[indx-1];
		starty=iyt[indx-1];
		startz=izt[indx-1];
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
	}
	indx=0;
		for (int ix=0; ix<=16;ix++)
		{
			for (int iz=0; iz<=16;iz++)
			{
				if(getFlowDecay(world, startx+ix-8, starty, startz+iz-8)==0&&iyt[indx]<starty)
				{
					if(getFlowDecay(world, ixt[indx], iyt[indx], izt[indx])>0&&indx!=0)
					{
						world.setBlockMetadataWithNotify(ixt[indx], iyt[indx], izt[indx], 0, 3);
						indx++;
					} 
					else if(indx==0)
					{
						world.setBlock(ixt[indx], iyt[indx], izt[indx], world.getBlockId(startx+ix-8, starty, startz+iz-8),0,3);
						indx++;
					}
					world.setBlockToAir(startx+ix-8, starty, startz+iz-8);
				} else {this.setDead();} 
			}
		}


}      

}