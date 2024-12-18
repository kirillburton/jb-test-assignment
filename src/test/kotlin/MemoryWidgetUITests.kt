import com.intellij.driver.client.Driver
import com.intellij.driver.sdk.ui.components.*
import com.intellij.driver.sdk.ui.shouldBe
import com.intellij.driver.sdk.ui.ui
import com.intellij.driver.sdk.waitForIndicators
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.tools.ide.performanceTesting.commands.CommandChain
import com.intellij.tools.ide.performanceTesting.commands.startPowerSave
import customComponents.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import setup.*
import kotlin.time.Duration.Companion.minutes

class MemoryWidgetUITests {
    val ON_widgetAssertionText = "Memory Widget Indicator should be present";
    val OFF_widgetAssertionText = "Memory Widget Indicator should be turned off";

    @Test
    fun clickOnWidget_ShouldCleanGarbage() {

        val context = setupTestContext(CurrentTestMethod.hyphenateWithClass(), isWidgetTurnedOn = true)
        context.runIdeWithDriver().useDriverAndCloseIde {
            openProjectAsDirectory()
            waitForIndicators(5.minutes)
            ideStatusBar {
                memoryIndicatorWidget {
                    waitAnyTexts()
                    val usedBeforeClean = usedMemory;
                    cleanGarbage()
                    val usedAfterClean = usedMemory;

                    assertThat(usedBeforeClean).isGreaterThan(usedAfterClean)
                }
            }
        }
    }

    @Test
    fun hoverOverWidget_ShouldShowDetails() {

        val context = setupTestContext(CurrentTestMethod.hyphenateWithClass(), isWidgetTurnedOn = true)
            .skipIndicesInitialization(true)
        context.runIdeWithDriver().useDriverAndCloseIde {
            openProjectAsDirectory()
            waitForIndicators(5.minutes)
            ideStatusBar {
                memoryIndicatorWidget {
                    waitAnyTexts()
                    moveMouse()
                }
            }
            ideFrame {
                popup()
                    .detailsTooltip().shouldBe(
                    "Hovering over Memory Indicator widget should prompt details tooltip to appear"
                ) { this.isVisible() }
                // easy to check contents of the tooltip
                // but for now I'll just mention it }
            }
        }
    }

    @Test
    fun disableInStatusBarPopupMenu_ShouldDisableWidget() {

        val context = setupTestContext(CurrentTestMethod.hyphenateWithClass(), isWidgetTurnedOn = true)
            .skipIndicesInitialization(true)
        context.runIdeWithDriver().useDriverAndCloseIde {
            openProjectAsDirectory()
            waitForIndicators(5.minutes)
            ideStatusBar {
                waitAnyTexts()
                memoryIndicatorWidget {
                    shouldBe(ON_widgetAssertionText) { this.present() }
                }
                openStatusBarMenu()
            }
            ideFrame {
                toggleMemoryIndicatorInPopupMenu()
            }
            ideStatusBar {
                memoryIndicatorWidget {
                    shouldBe(OFF_widgetAssertionText) { this.notPresent() }
                }
            }
        }
    }

    @Test
    fun enableInStatusBarPopupMenu_ShouldEnableWidget() {

        val context = setupTestContext(CurrentTestMethod.hyphenateWithClass(), isWidgetTurnedOn = false)
            .skipIndicesInitialization(true)
        context.runIdeWithDriver().useDriverAndCloseIde {
            openProjectAsDirectory()
            waitForIndicators(5.minutes)
            ideStatusBar {
                waitAnyTexts()
                memoryIndicatorWidget {
                    shouldBe(OFF_widgetAssertionText) { this.notPresent() }
                }
                openStatusBarMenu()
            }
            ideFrame {
                toggleMemoryIndicatorInPopupMenu()
            }
            ideStatusBar {
                memoryIndicatorWidget {
                    shouldBe(ON_widgetAssertionText) { this.present() }
                }
            }
        }
    }

    @Test
    fun toggleInSearchEverywhere_ShouldEnableAndDisableWidget() {

        val context = setupTestContext(CurrentTestMethod.hyphenateWithClass(), isWidgetTurnedOn = false)
        context.runIdeWithDriver().useDriverAndCloseIde {
            openProjectAsDirectory()
            waitForIndicators(5.minutes)
            ideFrame {
                searchEverywherePopup().callByShortcut()
                searchEverywherePopup().searchMemoryIndicatorAndToggle()
                memoryIndicatorWidget {
                    shouldBe(ON_widgetAssertionText) { this.present() }
                }
                searchEverywherePopup().toggleSelectedProperty()
                memoryIndicatorWidget {
                    shouldBe(OFF_widgetAssertionText) { this.notPresent() }
                }
            }
        }
    }
    
}
