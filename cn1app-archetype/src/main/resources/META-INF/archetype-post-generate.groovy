import static groovy.io.FileType.*
import java.nio.file.Path

def rootDir = new java.io.File(request.getOutputDirectory() + "/" + request.getArtifactId())
def rootPom = new java.io.File(rootDir, "pom.xml")
setupModules(rootPom);

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

