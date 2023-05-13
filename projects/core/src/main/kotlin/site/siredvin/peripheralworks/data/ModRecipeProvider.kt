package site.siredvin.peripheralworks.data

import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.peripheralium.data.blocks.TweakedShapedRecipeBuilder
import site.siredvin.peripheralium.data.blocks.TweakedShapelessRecipeBuilder
import site.siredvin.peripheralium.data.blocks.TweakedUpgradeRecipeBuilder
import site.siredvin.peripheralworks.common.setup.Blocks
import site.siredvin.peripheralworks.common.setup.Items
import site.siredvin.peripheralworks.xplat.ModRecipeIngredients
import java.util.function.Consumer

class ModRecipeProvider(output: PackOutput) : RecipeProvider(output) {
    override fun buildRecipes(consumer: Consumer<FinishedRecipe>) {
        val ingredients = ModRecipeIngredients.get()

        TweakedShapedRecipeBuilder.shaped(Items.PERIPHERALIUM_HUB.get())
            .define('D', ingredients.diamond)
            .define('E', ingredients.emerald)
            .define('P', ingredients.peripheralium)
            .define('M', ingredients.enderModem)
            .pattern("PDP")
            .pattern("EME")
            .pattern("PDP")
            .save(consumer)

        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.PERIPHERALIUM_HUB.get()),
            ingredients.netheriteIngot,
            Items.NETHERITE_PERIPHERALIUM_MODEM.get()
        ).save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.PERIPHERAL_CASING.get().asItem())
            .define('B', ingredients.peripheraliumBlock)
            .define('C', ingredients.anyCoal)
            .define('I', ingredients.ironIngot)
            .pattern("ICI")
            .pattern("CBC")
            .pattern("ICI")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Blocks.UNIVERSAL_SCANNER.get().asItem())
            .define('O', ingredients.observer)
            .define('C', Ingredient.of(Blocks.PERIPHERAL_CASING.get().asItem()))
            .define('P', ingredients.peripheralium)
            .pattern("POP")
            .pattern(" C ")
            .pattern(" O ")
            .save(consumer)
    }
}