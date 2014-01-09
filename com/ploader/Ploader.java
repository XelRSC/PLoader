package com.ploader;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.rsbuddy.orion.Orion;

/***
 *@author Xel
 *@version 1.0
 *@project PLoader
 *@file Ploader.java
 *@date 8.1.2014
 *@time 15.08.40
 */
public class Ploader {

	private final JFrame orionFrame;
	private JTabbedPane orionTabbedPane;
	private JMenuBar orionMenuBar;
	private JMenuItem btnPluginloader;
	private static Ploader ploader;
	private final Tools tools;
	
	public static void main(final String[] args){		
		ploader = new Ploader(args);	
	}
	
	public Ploader(final String[] args){
		tools = new Tools();
		//launchDebugger(); <- used to contain a System.out&System.err redirect, don't think people would care too much
		
		try {
			orionBooter(args);		 				//Boot orion
		} catch (final Exception e) {e.printStackTrace();}
	
		orionFrame = tools.frameFinder("orion");  		//Get Orion JFrame
		
		for(final Component c : tools.getAllComponents(orionFrame))
		{
			if(c instanceof JTabbedPane){
				orionTabbedPane = (JTabbedPane) c;  //Get Orion tabbedpane(pane on the right that contains plugins)
			}
			
			if(c instanceof JMenuBar){
				orionMenuBar = (JMenuBar)c;			//Get Orion menubar
			}
		}
		
		//Construct teh button
		//Do it here in order to prevent any laf glitches without res
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				btnPluginloader = new JMenuItem("Plugin Loader");
				btnPluginloader.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(final ActionEvent e) {
						new PloaderGui(orionFrame, ploader); //load gui and pass orion frame for position and this for launching
					}});
				orionMenuBar.add(btnPluginloader); //Add pluginloader button to menubar
				
			}});
	}
	
	private void orionBooter(final String[] args) throws Exception{
		Orion.main(args);
	}

	//lol why is this here...
	public void launchPlugin(final String[] args){
		final String pluginPath = args[0];
		final String jarName = args[1];
		final String mainClass = args[2];
		
		URL[] jUrl = null;
		final File jar = new File(pluginPath + "\\" + jarName + ".jar");
		try {
			jUrl = new URL[]{jar.toURI().toURL()};
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
		
		final URLClassLoader loader = new URLClassLoader(jUrl, System.class.getClassLoader());
		
		try{
			final Class<?> cls = loader.loadClass(mainClass);
			final Method getMethod = cls.getDeclaredMethod("gui");
			
			final Object clsInstance = cls.newInstance();
			final Object o = getMethod.invoke(clsInstance);
			final JPanel jp = (JPanel)o;
			orionTabbedPane.addTab("", new ImageIcon(pluginPath + "\\icon.png"), jp);
			
		}catch(final Exception e){e.printStackTrace();}
	}

	public static Ploader getPloader() {
		return ploader;
	}

	public Tools getTools() {
		return tools;
	}
}
