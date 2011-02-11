package it.masch.stats;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * stats block listener
 * @author masch
 */
public class statsBlockListener extends BlockListener {
    private final stats plugin;
    private statsLogger logger = new statsLogger();

    public statsBlockListener(final stats plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockDamage(BlockDamageEvent evt) {
        if(evt.getDamageLevel() == evt.getDamageLevel().BROKEN) {
            logger.logBlock(evt.getPlayer().getDisplayName(), evt.getBlock().getTypeId(), true);
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent evt) {
        logger.logBlock(evt.getPlayer().getDisplayName(), evt.getBlock().getTypeId(), false);
    }

    //put all Block related code here
}
