package us.derfers.tribex.rapids.GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Composite;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.derfers.tribex.rapids.Main;
import us.derfers.tribex.rapids.Utilities;

public class GUI_Swing {
	public static void loadGUI(String filePath, ScriptEngine engine, JPanel parentComposite, Boolean clearWidgets) {
		//TODO:  Want this one Nathaniel?
	}
	
	
	private static void addMethodListener(String type, Component widget, final String value, final ScriptEngine engine) {		
		//Add event listener
		((JComponent) widget).addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					engine.eval(value);
				} catch (ScriptException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
