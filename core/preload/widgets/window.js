/**
 * @file Provides the ability to use XML window tags to create JFrames with nested child widgets.
 * @author Tribex
 */

require(Packages.javax.swing.JFrame);
require(Packages.javax.swing.JMenuBar);
require(Packages.java.awt.GridBagLayout);
require(Packages.java.awt.event.WindowAdapter);

__widgetTypes.registerWidget("window", function (widgetElement) {
    //Create a new Window
    var widget = new JFrame();

    //XXX: Initialization :XXX\\
    var windowPanel = widget.getContentPane();

    widget.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    if (widgetElement.hasAttribute("id") && widgetElement.getAttributeNode("id").getTextContent().equals("__INIT__")) {
        setVisible = true;
        widget.addWindowListener(new JavaAdapter(WindowAdapter, {
            windowClosing : function(evt) {
                System.exit(0);
            },
        }));
    } else if (widgetElement.getAttributeNode("id") == null) {
        program.showError("Error: Window does not have an id. Unable to recover.");
        System.exit(0);
    } else {
        windowID = widgetElement.getAttributeNode("id").getTextContent();
        widget.addWindowListener(new JavaAdapter(WindowAdapter, {
            windowClosing : function(evt) {
                window.setVisible(false);
            },
        }));
    }

    //Create a GBL
    windowPanel.setLayout(new GridBagLayout());

    var menuBar = new JMenuBar();
    widget.setJMenuBar(menuBar);

    widget.setLayout(new GridBagLayout());
    widget.setTitle("Untitled Window");

    //Initialize the widget.
    var id = __widgetOps.initializeWidget(widget, widgetElement, "__TOPLEVEL__");

    //Load all elements inside of the composite/widget.  INFINITE NESTING!
    __widgetOps.loadInComposite(widget, widgetElement.getElementsByTagName("body").item(0), id);

    widget.pack();
    return id;
},
[], [], ["*"]);
