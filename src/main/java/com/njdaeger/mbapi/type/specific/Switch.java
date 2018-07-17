package com.njdaeger.mbapi.type.specific;

import com.njdaeger.mbapi.Material;
import com.njdaeger.mbapi.MaterialBridge;
import com.njdaeger.mbapi.Util;
import com.njdaeger.mbapi.data.StackedBlockType;
import com.njdaeger.mbapi.properties.Directional;
import com.njdaeger.mbapi.properties.Powerable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Switch.Face;

import java.util.Collections;
import java.util.Set;

/*

Properties:

- face      SELF
- facing    Directional
- powered   Powerable

 */

public class Switch extends StackedBlockType<Switch> implements Powerable<Switch>, Directional<Switch> {
    
    private Set<BlockFace> allowedDirections;
    private BlockFace direction;
    private boolean powered;
    private Face face;
    
    public Switch(Material<Switch> material) {
        super(material);
        this.allowedDirections = Util.mainDirections();
    }
    
    @Override
    public void setBlock(Location location, boolean setIfDifferent, boolean applyPhysics) {
        Block block = location.getBlock();
        if (!block.getType().equals(getBukkitMaterial())) {
            if (setIfDifferent) block.setType(getBukkitMaterial());
            else return;
        }
        if (MaterialBridge.isPretechnical()) {
            block.setData((byte)getLegacyData().getDurability(), applyPhysics);
            return;
        }
        org.bukkit.block.data.type.Switch swch = (org.bukkit.block.data.type.Switch)block.getBlockData();
        swch.setFace(face);
        swch.setFacing(direction);
        swch.setPowered(powered);
        block.setBlockData(swch);
    }
    
    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }
    
    @Override
    public boolean isPowered() {
        return powered;
    }
    
    @Override
    public void setDirection(BlockFace direction) {
        if (isAllowedDirection(direction)) this.direction = direction;
    }
    
    @Override
    public BlockFace getDirection() {
        return direction;
    }
    
    @Override
    public Set<BlockFace> getAllowedDirections() {
        return Collections.unmodifiableSet(allowedDirections);
    }
    
    public void setFace(Face face) {
        this.face = face;
    }
    
    public Face getFace() {
        return face;
    }
    
}
