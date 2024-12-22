package customComponents

import com.intellij.driver.sdk.ui.Finder
import com.intellij.driver.sdk.ui.components.ComponentData
import com.intellij.driver.sdk.ui.components.UiComponent
import org.intellij.lang.annotations.Language

fun Finder.memoryDetailsTooltip(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@class='Header']", MemoryDetailsTooltipUI::class.java)

class MemoryDetailsTooltipUI(data: ComponentData) : UiComponent(data) {
    val detailedMemoryTooltipText = this.allTextAsString()
}

