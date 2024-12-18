package setup

import com.intellij.ide.starter.ide.IDETestContext
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.Starter
import com.intellij.tools.ide.util.common.logOutput
import java.io.File
import java.nio.file.Paths

private val currentWorkDir = File(System.getProperty("user.dir"))
val userHome = System.getProperty("user.home")
val relativePath = "out/ide-tests/cache/projects/unpacked/SampleLibrary"
val mainPath = File(currentWorkDir, relativePath).absolutePath
val ideaPath = File(currentWorkDir, "$relativePath/SampleLibrary/.idea/.idea.SampleLibrary").absolutePath

val mainPathWithUserHome = mainPath.replace(userHome, "\$USER_HOME\$")
val ideaPathWithUserHome = ideaPath.replace(userHome, "\$USER_HOME\$")

private val testRiderConfigPath = Paths.get("testData", "riderConfigs");
private val testRepo = TestCase(
    IdeProductProvider.RD, GitHubProject.fromGithub(
    branchName = "main",
    repoRelativeUrl = "https://github.com/kirillburton/SampleLibrary.git"
)).useRelease("2024.2")

val riderLicenseKey = System.getenv("RIDER_KEY") ?: throw Exception("RIDER LICENSE KEY MISSING IN THE ENVIRONMENT VARIABLE KEY");


fun setupTestContext(testName: String, isWidgetTurnedOn: Boolean): IDETestContext {
    val testCase = testRepo
    generateTrustedSolutions()
    generateTrustedPaths()
    writeMemoryIndicatorWidgetToConfig(isWidgetTurnedOn)

    return Starter
        .newContext(testName = testName, testCase = testCase)
        .copyExistingConfig(testRiderConfigPath)
        .updateGeneralSettings()
        .setLicense(riderLicenseKey)
        .prepareProjectCleanImport()
}

fun writeMemoryIndicatorWidgetToConfig(isTurnedOn: Boolean) {
    val xmlContent = """
        <application>
            <component name="GeneralSettings">
                <option name="showTipsOnStartup" value="false"/>
                <option name="reopenLastProject" value="false"/>
                <option name="confirmExit" value="false"/>
                <option name="confirmOpenNewProject2" value="-1"/>
            </component>
            <component name="Registry">
                <entry key="freeze.reporter.profiling" value="false"/>
                <entry key="actionSystem.playback.typecommand.delay" value="0"/>
                <entry key="ide.experimental.ui" value="true"/>
            </component>
            <component name="StatusBar">
                <option name="widgets">
                    <map>
                        <entry key="Memory" value="${isTurnedOn}"/>
                    </map>
                </option>
            </component>
        </application>
    """.trimIndent()
    writeXMLtoFile(xmlContent, "ide.general.xml")
}

fun generateTrustedSolutions() {
    val xmlContent = """
        <application>
            <component name="TrustedSolutionStore">
                <option name="trustedLocations">
                    <set>
                        <option value="$mainPathWithUserHome"/>
                        <option value="$ideaPathWithUserHome"/>
                    </set>
                </option>
            </component>
        </application>
    """.trimIndent()
    writeXMLtoFile(xmlContent, "trustedSolutions.xml")
}

fun generateTrustedPaths() {
    val xmlContent = """
        <application>
            <component name="Trusted.Paths">
                <option name="TRUSTED_PROJECT_PATHS">
                    <map>
                        <entry key="$mainPathWithUserHome" value="true"/>
                    </map>
                </option>
            </component>
        </application>
    """.trimIndent()
    writeXMLtoFile(xmlContent, "trusted-paths.xml")
}

fun writeXMLtoFile(xmlContent: String, xmlName: String){
    val outputDir = File(currentWorkDir, "testData/riderConfigs/options")
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, xmlName)
    outputFile.writeText(xmlContent)

    logOutput("$xmlName CONFIG CONTENT WRITTEN TO ${outputFile.absolutePath}")
}