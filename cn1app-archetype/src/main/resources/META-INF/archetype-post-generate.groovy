import static groovy.io.FileType.*
import java.nio.file.Path

def rootDir = new java.io.File(request.getOutputDirectory() + "/" + request.getArtifactId())
def rootPom = new java.io.File(rootDir, "pom.xml")
setupModules(rootPom);
resolveJavaVersion(rootDir);

/**
 * There are a few scripts that need to be executable (or should be)
 */

// The maven wrapper scripts should be executable
def mvnw = new java.io.File(rootDir, "mvnw")
mvnw.setExecutable(true, false)

// run.sh should be executable
def runSh = new java.io.File(rootDir, "run.sh")
runSh.setExecutable(true, false)

// The build.sh should be executable
def buildSh = new java.io.File(rootDir, "build.sh")
buildSh.setExecutable(true, false)

if (request.getProperties().getProperty("ide", null) == "netbeans") {
    def netbeansDir = new java.io.File(rootDir, "tools/netbeans");
    if (netbeansDir.exists()) {
        netbeansDir.listFiles().each {
            def destFile = new java.io.File(rootDir, it.getName())
            java.nio.file.Files.copy(it.toPath(), destFile.toPath())
        }
    }
}

/**
 * For some reason archetype automatically enables ALL modules definied in the archetype-metadata
 * even if we only want some of them conditionally enabled.  In our case, we only want the common
 * module enabled by default.  The rest are enabled according to the codename1.platform property.
 * @param pomFile
 * @return
 */
/**
 * The archetype ships codenameone_settings.properties and common/pom.xml with
 * `${javaVersion}` Velocity placeholders. With the default `javaVersion=auto`
 * those placeholders resolve to the literal string "auto" - we replace it here
 * with 17 when the archetype is invoked on a JDK >= 17 and with 8 otherwise.
 * If the user passed `-DjavaVersion=8` or `-DjavaVersion=17` explicitly, the
 * Velocity pass already substituted that value and there is nothing to do.
 */
def resolveJavaVersion(rootDir) {
    def settingsFile = new java.io.File(rootDir, "common/codenameone_settings.properties")
    def commonPom = new java.io.File(rootDir, "common/pom.xml")
    def hasAutoSettings = settingsFile.exists() && settingsFile.text.contains("codename1.arg.java.version=auto")
    def hasAutoPom = commonPom.exists() && commonPom.text.contains("<source>auto</source>")
    if (!hasAutoSettings && !hasAutoPom) {
        return
    }

    def resolved = pickJavaVersionFromCurrentJvm()

    if (hasAutoSettings) {
        def content = settingsFile.text.replace("codename1.arg.java.version=auto",
                "codename1.arg.java.version=" + resolved)
        settingsFile.newWriter("UTF-8").withWriter { w -> w << content }
    }
    if (hasAutoPom) {
        def content = commonPom.text
                .replace("<source>auto</source>", "<source>" + resolved + "</source>")
                .replace("<target>auto</target>", "<target>" + resolved + "</target>")
        commonPom.newWriter("UTF-8").withWriter { w -> w << content }
    }
}

/**
 * @return "17" when running on JDK 17 or newer, "8" otherwise.
 */
def pickJavaVersionFromCurrentJvm() {
    def specVersion = System.getProperty("java.specification.version", "1.8")
    def major
    try {
        if (specVersion.startsWith("1.")) {
            major = Integer.parseInt(specVersion.substring(2))
        } else {
            major = Integer.parseInt(specVersion.split("\\.")[0])
        }
    } catch (NumberFormatException ignored) {
        major = 8
    }
    return major >= 17 ? "17" : "8"
}

def setupModules(pomFile) {
    def content = pomFile.text;
    def modulesPos = content.indexOf("<modules>");
    def endTag = "</modules>";

    def modulesEndPos = content.indexOf(endTag) + endTag.length();
    def modulesSection = "<modules>\n<module>common</module>\n</modules>";

    content = content.substring(0, modulesPos) + modulesSection + content.substring(modulesEndPos);
    pomFile.newWriter("UTF-8").withWriter { w ->
        w << content
    }


}

