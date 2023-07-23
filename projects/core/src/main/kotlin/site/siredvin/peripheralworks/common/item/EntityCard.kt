package site.siredvin.peripheralworks.common.item

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.peripheralium.common.items.DescriptiveItem
import site.siredvin.peripheralworks.data.ModText
import site.siredvin.peripheralworks.subsystem.entityperipheral.EntityPeripheralLookup
import java.util.UUID

class EntityCard : DescriptiveItem(Properties().stacksTo(1)) {
    companion object {
        private val CUSTOM_MODEL_DATA_TAG = "CustomModelData"
        private val ENTITY_UUID_TAG = "entityUUID"
        fun isEmpty(itemStack: ItemStack): Boolean {
            val itemTag = itemStack.tag ?: return true
            return !itemTag.contains(CUSTOM_MODEL_DATA_TAG)
        }

        fun isEntityMatching(entity: Entity): Boolean {
            return EntityPeripheralLookup.collectPlugins(entity).isNotEmpty()
        }

        fun storeEntity(itemStack: ItemStack, entity: Entity) {
            val itemTag = itemStack.orCreateTag
            itemTag.putByte(CUSTOM_MODEL_DATA_TAG, 1)
            itemTag.putUUID(ENTITY_UUID_TAG, entity.uuid)
        }

        fun getEntityUUID(itemStack: ItemStack): UUID? {
            val itemTag = itemStack.tag ?: return null
            if (!itemTag.contains(ENTITY_UUID_TAG)) return null
            return itemTag.getUUID(ENTITY_UUID_TAG)
        }
    }

    override fun isFoil(stack: ItemStack): Boolean {
        return !isEmpty(stack)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        if (!isEmpty(itemStack)) {
            list.add(ModText.SOMETHING_STORED_INSIDE_CARD.text)
        }
        super.appendHoverText(itemStack, level, list, tooltipFlag)
    }

    override fun use(level: Level, player: Player, interactionHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemInHand = player.getItemInHand(interactionHand)
        if (level is ServerLevel && interactionHand == InteractionHand.MAIN_HAND && !isEmpty(itemInHand)) {
            val entityUUID = getEntityUUID(itemInHand) ?: return InteractionResultHolder.pass(itemInHand)
            val entity = level.getEntity(entityUUID) ?: return InteractionResultHolder.pass(itemInHand)
            player.displayClientMessage(
                ModText.TARGET_ENTITY.format(
                    entity.name.string,
                    entity.blockPosition().toString(),
                ),
                false,
            )
        }
        return InteractionResultHolder.pass(itemInHand)
    }
}
