#set( $symbol_pound = '#' )
        #set( $symbol_dollar = '$' )
        #set( $symbol_escape = '\' )
        package ${package};
/*
[archetype]
----
extends=../cn1app-archetype
groupId=com.codenameone
artifactId=guibuilder-app-archetype
version=7.0-SNAPSHOT
parentGroupId=com.codenameone
parentArtifactId=generated-archetypes
version=7.0-SNAPSHOT
----

[files]
----
src/main/guibuilder/__mainName__MainForm.gui
src/main/java/__mainName__MainForm.java
----

[file:src/main/guibuilder/__mainName__MainForm.gui]
----
<?xml version="1.0" encoding="UTF-8"?>

<component type="Form" layout="LayeredLayout" layeredLayoutPreferredWidthMM="0.0" layeredLayoutPreferredHeightMM="0.0"  autolayout="true" title="${mainName}MainForm" scrollableY="true" name="${mainName}MainForm">
  <component type="Button" text="Click Me" name="Button">
    <layoutConstraint insets="auto auto auto auto" referenceComponents="-1 -1 -1 -1" referencePositions="0.0 0.0 0.0 0.0" />
  </component>
</component>
----

[file:src/main/java/__mainName__MainForm.java]
----
package ${package};
public class ${mainName}MainForm extends com.codename1.ui.Form {
    public ${mainName}MainForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public ${mainName}MainForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
    }

//-- DON'T EDIT BELOW THIS LINE!!!
    protected com.codename1.ui.Button gui_Button = new com.codename1.ui.Button();


// <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.LayeredLayout());
        setInlineStylesTheme(resourceObjectInstance);
        setScrollableY(true);
                setInlineStylesTheme(resourceObjectInstance);
        setTitle("MyForm");
        setName("MyForm");
        gui_Button.setText("Click Me");
                gui_Button.setInlineStylesTheme(resourceObjectInstance);
        gui_Button.setName("Button");
        addComponent(gui_Button);
        ((com.codename1.ui.layouts.LayeredLayout)gui_Button.getParent().getLayout()).setInsets(gui_Button, "auto auto auto auto").setReferenceComponents(gui_Button, "-1 -1 -1 -1").setReferencePositions(gui_Button, "0.0 0.0 0.0 0.0");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
----
 */
import static com.codename1.ui.CN.*;
import com.codename1.ui.*;
import com.codename1.ui.layouts.*;
import com.codename1.io.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.util.Resources;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */
public class ${mainName} {

private Form current;
private Resources theme;

public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
        // prevent the event from propagating
        err.consume();
        if(err.getError() != null) {
        Log.e(err.getError());
        }
        Log.sendLogAsync();
        Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
        }


public void start(){
        if(current!=null){
        current.show();
        return;
        }

        ${mainName}MainForm hi=new ${mainName}MainForm(theme);

        hi.show();
        }

public void stop() {
        current = getCurrentForm();
        if(current instanceof Dialog) {
        ((Dialog)current).dispose();
        current = getCurrentForm();
        }
        }

public void destroy() {
        }

        }
