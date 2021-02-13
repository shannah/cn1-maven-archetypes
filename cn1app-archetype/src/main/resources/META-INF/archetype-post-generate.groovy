@Grab(group="org.codehaus.groovy", module="groovy-all", version="2.4.8")
import static groovy.io.FileType.*
import java.nio.file.Path

def rootDir = new java.io.File(request.getOutputDirectory() + "/" + request.getArtifactId())
def rootPom = new java.io.File(rootDir, "pom.xml")
setupModules(rootPom);

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

