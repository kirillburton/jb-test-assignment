package customComponents

import com.intellij.driver.client.Driver
import com.intellij.driver.sdk.ui.Finder
import com.intellij.driver.sdk.ui.components.ComponentData
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.ui
import com.intellij.driver.sdk.waitAny
import com.intellij.tools.ide.util.common.logOutput
import org.intellij.lang.annotations.Language
import kotlin.time.Duration.Companion.seconds


fun Finder.ideStatusBar(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@class='IdeStatusBarImpl']", IdeStatusBarImplUI::class.java)

fun Finder.ideStatusBar(action: IdeStatusBarImplUI.() -> Unit) {
    ideStatusBar().action()
}

fun Driver.ideStatusBar(action: IdeStatusBarImplUI.() -> Unit) {
    this.ui.ideStatusBar(action)
}


class IdeStatusBarImplUI(data: ComponentData) : UiComponent(data) {

    fun openStatusBarMenu() {
        waitAnyTexts()

        val widgets = listOf(
            memoryIndicatorWidget(),
            readOnlyAttributeWidget()
            // this can be expanded with all widgets,
            // so that it's more flexible when we don't know which widgets are present
        )
        val presentWidgets = waitAny(getter = { widgets }, checker = { it.present() });
        presentWidgets[0].rightClick();
    }
}

