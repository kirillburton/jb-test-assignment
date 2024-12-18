package customComponents

import com.intellij.driver.client.Driver
import com.intellij.driver.sdk.ui.Finder
import com.intellij.driver.sdk.ui.components.*
import com.intellij.driver.sdk.ui.ui
import com.intellij.tools.ide.util.common.logOutput
import org.intellij.lang.annotations.Language

fun Finder.memoryIndicatorWidget(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@class='MemoryUsagePanelImpl']", MemoryIndicatorWidgetUI::class.java)

fun Finder.memoryIndicatorWidget(action: MemoryIndicatorWidgetUI.() -> Unit) {
    memoryIndicatorWidget().action()
}

fun Driver.memoryIndicatorWidget(action: MemoryIndicatorWidgetUI.() -> Unit) {
    this.ui.memoryIndicatorWidget(action)
}

class MemoryIndicatorWidgetUI(data: ComponentData) : UiComponent(data) {

    private val ideaFrameComponent by lazy { driver.cast(component, IdeFrameImpl::class) }

    val memoryUsageText: String
        get() = this.allTextAsString()

    val parseUsedMemoryRegex = """(\d+)\s+of\s+(\d+)M""".toRegex()
    val usedMemory : Int
        get() {
            return parseUsedMemoryRegex.find(memoryUsageText)?.groupValues?.get(1)?.toInt() ?: 0
        }
    val totalMemory : Int
        get () {
            return parseUsedMemoryRegex.find(memoryUsageText)?.groupValues?.get(2)?.toInt() ?: 0
        }

    fun cleanGarbage() = {
        logOutput("CLEANING MEMORY. MEMORY USAGE BEFORE CLEANING: $usedMemory of $totalMemory M")
        this.click()
        logOutput("GARBAGE CLEAN COMPLETED. MEMORY USAGE AFTER CLEANING: $usedMemory of $totalMemory M")
    } ()
}

fun Finder.detailsTooltip(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@class='Header']", MemoryDetailsTooltipUI::class.java)


class MemoryDetailsTooltipUI(data: ComponentData) : UiComponent(data) {
    val detailedMemoryText = this.allTextAsString()
}


