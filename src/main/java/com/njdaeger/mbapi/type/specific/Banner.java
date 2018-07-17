package com.njdaeger.mbapi.type.specific;

import com.njdaeger.mbapi.Material;
import com.njdaeger.mbapi.MaterialBridge;
import com.njdaeger.mbapi.Util;
import com.njdaeger.mbapi.data.StackedBlockType;
import com.njdaeger.mbapi.properties.Directional;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Collections;
import java.util.List;
import java.util.Set;

//TODO this entire class. its not entirely possible to rotate a banner with the updated blockdata api
public class Banner extends StackedBlockType<Banner> implements Directional<Banner> {
    
    private Set<BlockFace> allowedDirections;
    private List<Pattern> patterns;
    private BlockFace direction;
    private DyeColor color;
    
    public Banner(Material<Banner> material) {
        super(material);
        this.allowedDirections = Util.allDirectionsExcept(BlockFace.UP, BlockFace.DOWN, BlockFace.SELF);
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
        org.bukkit.block.Banner bannerPatterns = (org.bukkit.block.Banner)block.getBlockData();
        bannerPatterns.setPatterns(this.patterns);
        org.bukkit.material.Banner banner = (org.bukkit.material.Banner)block;
        banner.setFacingDirection(direction);
        ((org.bukkit.material.Banner)block).setFacingDirection(direction);
        block.setBlockData((BlockData)banner);
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
    
    public void addPattern(Pattern pattern) {
        patterns.add(pattern);
    }
    
    public void removePattern(int patternIndex) {
        patterns.remove(patternIndex);
    }
    
    public List<Pattern> getPatterns() {
        return patterns;
    }
    
    public int getPatternAmount() {
        return patterns.size();
    }
    
    public DyeColor getColor() {
        throw new UnsupportedOperationException("Waiting for full 1.13 release.");
    }
    
}
