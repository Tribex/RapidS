package us.derfers.tribex.rapids.GUI;

import static us.derfers.tribex.rapids.Utilities.debugMsg;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Loader;
import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;
import us.derfers.tribex.rapids.parsers.CSSParser;

public class GUI_Swing {

	//Constants that need to be defined to keep the GUI in one frame system.
	private JFrame window = new JFrame();
	private JPanel windowPanel = new JPanel();

	//Stylesheet information
	private Map<String, Map<String, String>> stylesMap = new HashMap<String, Map<String, String>>();

	public void loadGUI(String filePath, ScriptEngine engine, Object parent, Boolean clearWidgets) {
		//XXX: Initialization :XXX\\
		/*
		 * Basically: Port the following code to Swing, nothing hard. :P   
		 */
		//If this is the initial run, and therefore there is no shell or display, initialize them

		//Create ParentComposite variable
		JPanel parentComposite = null;

		//Check to see if the parent exists
		if (parent == null || parent.getClass().equals(JFrame.class)) {
			//If it does not exist, add the the default panel to the window
			window.getContentPane().add(windowPanel);

			//Set the parentComposite to the windowPanel
			parentComposite = windowPanel;
		} else {
			//Set the parentComposite to the Parent
			parentComposite = (JPanel) parent;
		}

		//TODO: Flexible layout types
		//Create a gridbaglayout
		windowPanel.setLayout(new GridBagLayout());

		try {

			//XML File Loading
			File file = new File(filePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			//XXX: HEAD :XXX\\
			//Get Window information from the Head Element
			NodeList headElementList = doc.getElementsByTagName("head").item(0).getChildNodes();

			//Loop through the children of the Head element
			for (int counter=0; counter < headElementList.getLength(); counter++) {

				Node headNode = headElementList.item(counter);

				//Make sure we have real element nodes
				if (headNode.getNodeType() == Node.ELEMENT_NODE) {
					final Element headElement = (Element) headNode;
					//Set the shell title with the title or window_title node
					if (headElement.getNodeName().equals("title") || headElement.getNodeName().equals("window_title")) {
						window.setTitle(headElement.getTextContent());

						//Parse style information in the header
					} else if (headElement.getNodeName().equals("style")) {
						//Load all styles from the style tags
						if (headElement.getAttributeNode("href") != null) {
							loadStyles(null, headElement.getTextContent());
						} else {
							loadStyles(headElement.getTextContent(), null);

						}

					} else if (headElement.getNodeName().equals("link")) {
						parseLinks(headElement, engine);
					}
				}
			}
			//XXX: BODY : XXX\\
			//Loop through all children of the body element and add them
			loadInComposite(parentComposite, doc.getElementsByTagName("body").item(0), engine); 

		} catch (Exception e) {
			e.printStackTrace();
			Utilities.showError("Unable to properly initialize a SWT GUI. \n"+filePath+" may be corrupt or incorrectly formatted.");
		}

		//Fit the window to the elements in it.
		window.pack();

		//Create a subMap for holding the Window and any properties of it
		Map<String, Object> shellMap = new HashMap<String, Object>();

		shellMap.put("__WINDOW__", window);

		Main.loader.XMLWidgets.put("__WINDOW__", shellMap);

		//Loading the JS must be done ABSOLUTELY LAST before the open call, or some properties will be missed.
		Main.loader.loadJS(filePath, engine);

		//Open the window
		window.setVisible(true);

		//XXX: END WIDGET CREATION :XXX\\	
	}

	//TODO: Comment
	private void loadInComposite(JPanel parentComposite, Node node, ScriptEngine engine) {

		//XXX: BODY : XXX\\
		debugMsg(parentComposite.toString());

		//Get Widgets from the Body Element
		NodeList bodyElementList = node.getChildNodes();
		//Loop through all children of the root element.
		for (int counter=0; counter < bodyElementList.getLength(); counter++) {

			Node widgetNode = bodyElementList.item(counter);

			if (widgetNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element widgetElement = (Element) widgetNode;

				//Load all link tags
				if (widgetElement.getNodeName().equals("link")) {
					if (widgetElement.getAttributeNode("href") != null) {
						Main.loader.loadAll((widgetElement.getAttributeNode("href").getNodeValue()), parentComposite, false, engine);

					} else {
						Utilities.showError("Warning: <link> tags must contain a href attribute.");
					}

					//JButtons
				} else if (widgetElement.getNodeName().equals("button")) {
					JButton widget = new JButton();

					parentComposite.add(widget, getWidgetConstraint(widgetElement));
					//Set button text with the content of the <button></button> tags
					widget.setText(widgetElement.getTextContent());


					//Iterate through listener types and set listeners if they exist
					for (String listenerType : Main.loader.listenerTypesArray) {
						//Add a listener for listenerType if specified
						if (widgetElement.getAttributeNode(listenerType) != null) {
							addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
						}

					}
					Loader.addWidgetToMaps(widgetElement, widget, engine);

				} else if (widgetElement.getNodeName().equals("spinner")) {
					//SPINNER
					final Element spinnerElement = (Element) widgetNode;
					//Create a new composite for sub-elements
					SpinnerNumberModel model = new SpinnerNumberModel();

					if (spinnerElement.getAttributeNode("value") != null) {
						model.setValue(Integer.valueOf(spinnerElement.getAttributeNode("value").getNodeValue()));
					}

					if (spinnerElement.getAttributeNode("max") != null) {
						model.setMaximum(Integer.valueOf(spinnerElement.getAttributeNode("max").getNodeValue()));
					}

					if (spinnerElement.getAttributeNode("min") != null) {
						model.setMinimum(Integer.valueOf(spinnerElement.getAttributeNode("min").getNodeValue()));
					}

					//Add widget to maps
					JSpinner widget = new JSpinner(model);
					parentComposite.add(widget, getWidgetConstraint(widgetElement));

					Loader.addWidgetToMaps(spinnerElement, widget, engine);

					//LABEL CODE
				} else if (widgetElement.getNodeName().equals("label")) {
					final Element labelElement = (Element) widgetNode;

					JLabel widget = new JLabel();

					widget.setText(labelElement.getTextContent());

					parentComposite.add(widget, getWidgetConstraint(widgetElement));

					Loader.addWidgetToMaps(labelElement, widget, engine);
					for (String listenerType : Main.loader.listenerTypesArray) {
						//Add a listener for listenerType if specified
						if (widgetElement.getAttributeNode(listenerType) != null) {
							addMethodListener(listenerType, (Component) widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
						}

					}

				}

			}

		}
		//position and draw the widgets
		//parentComposite.doLayout();
		//window.pack();

	}

	//Parse <link> tags
	private boolean parseLinks(Element linkElement, ScriptEngine engine) {

		//Check and see if the <link> tag contains a rel and href attribute
		if (linkElement.getAttributeNode("rel") != null && linkElement.getAttribute("href") != null) {
			//If it links to a stylesheet
			if (linkElement.getAttributeNode("rel").getTextContent().equals("stylesheet")) {

				//Check and see if the file exists
				if (this.loadStyles(null, linkElement.getAttributeNode("href").getTextContent()) == false) {
					Utilities.debugMsg("Error: invalid file in link tag pointing to "+linkElement.getAttributeNode("href").getTextContent());
					return false;
				};

				//If it links to a script
			} else if (linkElement.getAttributeNode("rel").getTextContent().equals("script")) {

				//Check and see if the file exists
				try {
					//Run script in file
					engine.eval(new java.io.FileReader(linkElement.getAttributeNode("href").getTextContent()));
					return true;

				} catch (FileNotFoundException e) {
					Utilities.debugMsg("Error: invalid file in link tag pointing to "+linkElement.getAttributeNode("href").getTextContent());
					e.printStackTrace();
					return false;
				} catch (DOMException e) {
					Utilities.debugMsg("Error: Improperly formatted XML");
					e.printStackTrace();
					return false;
				} catch (ScriptException e) {
					Utilities.debugMsg("Error: invalid script in file "+linkElement.getAttributeNode("href").getTextContent());
					e.printStackTrace();
					return false;
				}
			} else {
				//Attempt to load as a .rsm file
				Main.loader.loadAll((linkElement.getAttributeNode("href").getNodeValue()), 
						Main.loader.XMLWidgets.get("__WINDOW__").get("__WINDOW__"), false, engine);
			}

		} else {
			Utilities.showError("Warning: <link> tags must contain a href attribute and a rel attribute. Skipping tag.");
		}
		return false;
	}

	//Style loading method
	private boolean loadStyles(String content, String file) {
		//If the user has specified loading from a string, not file
		if (file == null) {
			//Create a new CSSParser
			CSSParser parser = new CSSParser(content);

			//Put all the content of the parsed CSS into the stylesMap
			stylesMap.putAll(parser.parseAll());
			return true;
			//If the user has specified loading from a file, not string
		} else if (content == null) {
			//Attempt to get the style information from the file
			try {
				//Load the file into the string toParse
				String toParse = FileUtils.readFileToString(new File(file));

				//Create a new CSSParser
				CSSParser parser = new CSSParser(toParse);

				//Put all the content of the parsed CSS into the stylesMap
				stylesMap.putAll(parser.parseAll());
				System.out.println(stylesMap.toString());
				return true;
			} catch (IOException e) {
				Utilities.showError("Error: Invalid CSS formatting in file: "+file);
				return false;
			}


		}
		//In case something went wrong that was not caught
		return false;
	}

	private GridBagConstraints getWidgetConstraint(Element widgetElement) {
		//Default Styles for widgetConstraint
		GridBagConstraints widgetConstraint = new GridBagConstraints(){

			private static final long serialVersionUID = 1L;

			{
				anchor = GridBagConstraints.LINE_START;
				fill = BOTH;
				ipadx = 5;
				ipadx = 5;
				weightx = 0.1;
				weighty = 0.1;
				gridx = 0;
				gridy = GridBagConstraints.RELATIVE;
			}
		};


		//Load styles from ID
		try {
			if (widgetElement.getNodeName() != null) {
				Map<String, String> styles = stylesMap.get(widgetElement.getNodeName());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			}

			if (widgetElement.getAttributeNode("class") != null) {
				Map<String, String> styles = stylesMap.get("."+widgetElement.getAttributeNode("class").getTextContent());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			}
			
			if (widgetElement.getAttributeNode("id") != null) {
				Map<String, String> styles = stylesMap.get("#"+widgetElement.getAttributeNode("id").getTextContent());
				loadConstraintStyles(widgetConstraint, widgetElement, styles);
			} 


		} catch (Exception e) {
			Utilities.showError("Bad CSS");
		}
		return widgetConstraint;
	}
	
	private void loadConstraintStyles(GridBagConstraints widgetConstraint, Element widgetElement, Map<String, String> styles) {

		//Margins, so that we can use multiple styles to accomplish the same thing
		Map<String, Integer> margins = new HashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
			{
				put("left", 0);
				put("right", 0);
				put("top", 0);
				put("bottom", 0);
			}
		};
		//Get the styles for the widget's ID

		//If there are some styles in it
		if (styles != null) {
			//Set fill direction(s)
			if (styles.get("fill") != null) {
				//If the widget is to fill horizontally
				if (styles.get("fill").equalsIgnoreCase("horizontal")) {
					widgetConstraint.fill = GridBagConstraints.HORIZONTAL;

					//If the widget is to fill vertically
				} else if (styles.get("fill").equalsIgnoreCase("vertical")){
					widgetConstraint.fill = GridBagConstraints.VERTICAL;

					//If the widget is to fill both horizontally and vertically
				} else if (styles.get("fill").equalsIgnoreCase("both")){
					widgetConstraint.fill = GridBagConstraints.BOTH;

					//If the widget is not to fill
				} else if (styles.get("fill").equalsIgnoreCase("none")){
					widgetConstraint.fill = GridBagConstraints.NONE;
				}
			}
			//Set the x grid position
			if (styles.get("position-x") != null) {
				if (styles.get("position-x").contains("rel")) {
					widgetConstraint.gridx = GridBagConstraints.RELATIVE;
				} else {
					widgetConstraint.gridx = Integer.valueOf(styles.get("position-x"));
				}
			}

			//Set the y grid position
			if (styles.get("position-y") != null) {
				if (styles.get("position-y").contains("rel")) {
					widgetConstraint.gridy = GridBagConstraints.RELATIVE;
				} else {
					widgetConstraint.gridy = Integer.valueOf(styles.get("position-y"));
				}
			}

			//Set the x weight
			if (styles.get("weight-x") != null) {
				widgetConstraint.weightx = Float.valueOf(styles.get("weight-x"));
			}

			//Set the y weight
			if (styles.get("weight-y") != null) {
				widgetConstraint.weighty = Float.valueOf(styles.get("weight-y"));
			}

			//Set the margin-left
			if (styles.get("margin-left") != null) {
				margins.put("left", Integer.valueOf(styles.get("margin-left")));
			}

			//Set the margin-right
			if (styles.get("margin-right") != null) {
				margins.put("right", Integer.valueOf(styles.get("margin-right")));
			}

			//Set the margin-top
			if (styles.get("margin-top") != null) {
				margins.put("top", Integer.valueOf(styles.get("margin-top")));
			}

			//Set the margin-bottom
			if (styles.get("margin-bottom") != null) {
				margins.put("bottom", Integer.valueOf(styles.get("margin-bottom")));
			}
			//Set the y external margin
			if (styles.get("margin") != null) {
				
				//Split string (int int) or (int int int int) into an array 
				String [] tempMargins = styles.get("margin").split(" ");
				
				//If the user has specified different values for every side, set them
				if (tempMargins.length == 4) {
					margins.put("top", Integer.valueOf(tempMargins[0]));
					margins.put("left", Integer.valueOf(tempMargins[1]));
					margins.put("bottom", Integer.valueOf(tempMargins[2]));
					margins.put("right", Integer.valueOf(tempMargins[3]));
					
				//If the user has specified horizontal and vertical values, set them
				} else if (tempMargins.length == 2) {
					margins.put("top", Integer.valueOf(tempMargins[0]));
					margins.put("left", Integer.valueOf(tempMargins[1]));
					margins.put("bottom", Integer.valueOf(tempMargins[0]));
					margins.put("right", Integer.valueOf(tempMargins[1]));
				}

			}

			//Set the x internal padding
			if (styles.get("padding-x") != null) {
				widgetConstraint.ipadx = Integer.valueOf(styles.get("padding-x"));
			}

			//Set the y internal padding
			if (styles.get("padding-y") != null) {
				widgetConstraint.ipady = Integer.valueOf(styles.get("padding-y"));
			}

			//Set the occupied cells x-dir
			if (styles.get("occupied-cells-x") != null) {
				widgetConstraint.gridwidth = Integer.valueOf(styles.get("occupied-cells-x"));
			}

			//Set the occupied cells y-dir
			if (styles.get("occupied-cells-y") != null) {
				widgetConstraint.gridheight = Integer.valueOf(styles.get("occupied-cells-y"));
			}

			//Set the occupied cells y-dir
			if (styles.get("anchor") != null) {
				Field f = null;
				try {
					f = widgetConstraint.getClass().getField(styles.get("anchor").toUpperCase().replace('-', '_'));
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					widgetConstraint.anchor = f.getInt(widgetConstraint);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			widgetConstraint.insets = new Insets(margins.get("top"), margins.get("left"), margins.get("bottom"), margins.get("right"));

		}
	}
	//Event listeners
	private boolean addMethodListener(String type, Component widget, final String value, final ScriptEngine engine) {		
		//Add event listener
		try {
			if (type.equals("onclick")) {
				((JComponent) widget).addMouseListener(new MouseAdapter(){
					@Override
					public void mouseClicked(MouseEvent arg0) {
						try {
							engine.eval(value);
						} catch (ScriptException e1) {
							Utilities.showError("Bad JavaScript: "+value);
							e1.printStackTrace();
						}

					}

				});
			} else if (type.equals("onmouseover")) {
				((JComponent) widget).addMouseListener(new MouseAdapter(){
					@Override
					public void mouseEntered(MouseEvent arg0) {
						try {
							engine.eval(value);
						} catch (ScriptException e1) {
							Utilities.showError("Bad JavaScript: "+value);
							e1.printStackTrace();
						}

					}

				});
			} else if (type.equals("onmouseout")) {
				((JComponent) widget).addMouseListener(new MouseAdapter(){
					@Override
					public void mouseExited(MouseEvent arg0) {
						try {
							engine.eval(value);
						} catch (ScriptException e1) {
							Utilities.showError("Bad JavaScript: "+value);
							e1.printStackTrace();
						}

					}

				});
			} else if (type.equals("onmousedown")) {
				((JComponent) widget).addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent arg0) {
						try {
							engine.eval(value);
						} catch (ScriptException e1) {
							Utilities.showError("Bad JavaScript: "+value);
							e1.printStackTrace();
						}

					}

				});
			} else if (type.equals("onmouseup")) {
				((JComponent) widget).addMouseListener(new MouseAdapter(){
					@Override
					public void mouseReleased(MouseEvent arg0) {
						try {
							engine.eval(value);
						} catch (ScriptException e1) {
							Utilities.showError("Bad JavaScript: "+value);
							e1.printStackTrace();
						}

					}

				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
