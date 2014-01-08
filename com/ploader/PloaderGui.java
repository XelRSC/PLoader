package com.ploader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/***
 *@author Xel
 *@version 1.0
 *@project PLoader
 *@file PloaderGui.java
 *@date 8.1.2014
 *@time 15.23.08
 */
//this is pretty messy
public class PloaderGui extends JFrame {
	private final JMenuBar menuBar;
	private final JMenu mnFile;
	private final JMenuItem mntmLoadPlugins;
	private final JMenuItem mntmReloadPlugins;
	private final JScrollPane scrollPane;
	private final JPanel pluginContainer;
	private File pluginFolder;
	private final Ploader ploader;

	public PloaderGui(final JFrame orionFrame, final Ploader pl){
		ploader = pl;
		setTitle("Orion plugin loader");
		setAlwaysOnTop(true);
		setBounds(orionFrame.getLocation().x + orionFrame.getWidth() / 2 - 300,
				  orionFrame.getLocation().y + orionFrame.getHeight() / 2 - 200,
				  600,
				  400);
		
		this.menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		
		this.mnFile = new JMenu("File");
		this.menuBar.add(this.mnFile);
		
		this.mntmLoadPlugins = new JMenuItem("Load Plugins");
		this.mntmLoadPlugins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				loadPlugins();
			}
		});
		this.mnFile.add(this.mntmLoadPlugins);
		
		this.mntmReloadPlugins = new JMenuItem("Reload Plugins");
		this.mntmReloadPlugins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				reloadPlugins();
			}
		});
		this.mnFile.add(this.mntmReloadPlugins);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		this.scrollPane = new JScrollPane();
		getContentPane().add(this.scrollPane, BorderLayout.CENTER);
		
		this.pluginContainer = new JPanel();
		this.scrollPane.setViewportView(this.pluginContainer);
		this.pluginContainer.setLayout(new BoxLayout(this.pluginContainer, BoxLayout.Y_AXIS));
		setVisible(true);
	}
	
	
	private void loadPlugins(){
		final JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new java.io.File("."));
		jfc.setDialogTitle("Choose plugins folder");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.showOpenDialog(this);
		pluginFolder = jfc.getSelectedFile();
		
		parsePlugins();
	}
	
	private void reloadPlugins(){
		if(pluginFolder != null){
			if(pluginFolder.exists()){
				parsePlugins();
			}
		}
	}
	
	private void parsePlugins(){
		final ArrayList<String[]> plugins = new ArrayList<String[]>();
		
		final File[] files = pluginFolder.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(final File f, final String s){
				return s.endsWith(".xml");
			}
		});
		
		for(final File f : files){
			try{
			final DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = dbFac.newDocumentBuilder();
			final Document doc = builder.parse(f);
			
			final NodeList nList = doc.getElementsByTagName("plugin");
			
			for(int i = 0 ; i < nList.getLength() ; i++){
				final Node node = nList.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE){
					final Element e = (Element)node;
					
					plugins.add(new String[]{getElement(e, "name"),
											 getElement(e, "author"),
											 getElement(e, "version"),
											 pluginFolder + "\\" + getElement(e, "path"),
											 getElement(e, "jar"),
											 getElement(e, "main")});
				}
			}
			
			
			}catch(final Exception e){e.printStackTrace();}
		}
		
		constructPlugins(plugins);
	}
	
	private String getElement(final Element e, final String tag){
		return e.getElementsByTagName(tag).item(0).getTextContent();
	}
	
	private void constructPlugins(final ArrayList<String[]> plugins){
		pluginContainer.removeAll();
		for(final String[] sa : plugins){
			pluginContainer.add(new PluginPanel(sa, ploader));
		}
		
		pluginContainer.revalidate();
	}
}
