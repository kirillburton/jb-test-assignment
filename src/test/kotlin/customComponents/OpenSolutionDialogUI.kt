package customComponents

import com.intellij.driver.client.Driver
import com.intellij.driver.sdk.ui.Finder
import com.intellij.driver.sdk.ui.components.ComponentData
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.components.button
import com.intellij.driver.sdk.ui.components.dialog
import com.intellij.driver.sdk.ui.ui
import org.intellij.lang.annotations.Language

fun Finder.openSolutionDialog(@Language("xpath") xpath: String? = null) =
    x(xpath ?: "//div[@class='JBLayeredPane']", OpenSolutionDialogUI::class.java)

fun Finder.openSolutionDialog(action: OpenSolutionDialogUI.() -> Unit) {
    openSolutionDialog().action()
}

fun Driver.openSolutionDialog(action: OpenSolutionDialogUI.() -> Unit) {
    this.ui.openSolutionDialog(action)
}

// Temporary stuff, needs a proper component

class OpenSolutionDialogUI(data: ComponentData) : UiComponent(data) {

}

fun Driver.openProjectAsSolution() {
    ui.dialog({
        byTitle("Select a Solution to Open")
    }).button("Open").click()
}
fun Driver.openProjectAsDirectory() {
    ui.dialog({
        byTitle("Select a Solution to Open")
    }).x{ byText("Open \'SampleLibrary\' as Directory")}.click()
}


