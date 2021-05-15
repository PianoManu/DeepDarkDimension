package mod.pianomanu.ddd.tileentity;

import mod.pianomanu.ddd.init.Registration;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import static mod.pianomanu.ddd.init.Registration.DDD_TELEPORTER_TILE_ENTITY;

public class TeleporterBlockTileEntity extends TileEntity {
    public TeleporterBlockTileEntity() {
        super(DDD_TELEPORTER_TILE_ENTITY.get());
    }
}
