package customComponents

import com.intellij.driver.client.Driver
import com.intellij.driver.sdk.ui.Finder
import com.intellij.driver.sdk.ui.components.ComponentData
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.ui
import org.intellij.lang.annotations.Language

fun Finder.readOnlyAttributeWidget(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@icon='unlocked.svg' or @icon='locked.svg']", ReadOnlyAttributeWidgetUI::class.java)

fun Finder.readOnlyAttributeWidget(action: ReadOnlyAttributeWidgetUI.() -> Unit) {
    readOnlyAttributeWidget().action()
}

fun Driver.readOnlyAttributeWidget(action: ReadOnlyAttributeWidgetUI.() -> Unit) {
    this.ui.readOnlyAttributeWidget(action)
}

class ReadOnlyAttributeWidgetUI(data: ComponentData) : UiComponent(data) {
    // Not implemented
}