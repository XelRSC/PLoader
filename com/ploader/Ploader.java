package com.ploader;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
	private final Ploader ploader;
	
	public static void main(final String[] args){
		new Ploader(args);
	}
	
	public Ploader(final String[] args){
		ploader = this;
		//launchDebugger(); <- used to contain a System.out&System.err redirect, don't think people would care too much
		
		try {
			orionBooter(args);		 				//Boot orion
		} catch (final Exception e) {e.printStackTrace();}
	
		orionFrame = frameFinder("orion");  		//Get Orion JFrame
		
		for(final Component c : getAllComponents(orionFrame))
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
	
	private JFrame frameFinder(final String searchHint)			//Used for finding a frame which title contains searchHint
	{
		for(final Frame f : Frame.getFrames()){
			if(f.getTitle().toLowerCase().contains(searchHint)){
				return (JFrame)f;
			}
		}		
		return null;
	}

	private List<Component> getAllComponents(final Container c) //Used for getting all components inside a container and children
	{
		final Component[] comps = c.getComponents();
		final List<Component> compList = new ArrayList<Component>();
		for(final Component comp : comps)
		{
			compList.add(comp);
			if(comp instanceof Container)
				compList.addAll(getAllComponents((Container)comp));
		}
		return compList;
	}


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
}
