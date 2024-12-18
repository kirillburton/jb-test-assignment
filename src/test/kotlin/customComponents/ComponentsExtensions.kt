package customComponents

import com.intellij.driver.sdk.ui.components.*
import java.awt.event.KeyEvent


fun SearchEverywherePopupUI.callByShortcut() {
    // better way of doing it is extracting from current IDE keymap
    keyboard { doublePressing(KeyEvent.VK_SHIFT) {} }
}

fun SearchEverywherePopupUI.searchMemoryIndicatorAndToggle() {
    keyboard {
        backspace()
        keyboard {
            // again, better fetch that option name from IDE property if that's possible
            enterText("Show Memory Indicator")
        }
        enter()
    }
}

fun SearchEverywherePopupUI.toggleSelectedProperty() {
    keyboard {
        enter()
    }
}

fun IdeaFrameUI.toggleMemoryIndicatorInPopupMenu() {
    this.popupMenu().x{
        byText("Memory Indicator")
    }.click();
}

