package com.ploader;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/***
 *@author Xel
 *@version 1.0
 *@project PLoader
 *@file Tools.java
 *@date 9.1.2014
 *@time 8.22.14
 */
public class Tools {

	public JFrame frameFinder(final String searchHint)			//Used for finding a frame which title contains searchHint
	{
		for(final Frame f : Frame.getFrames()){
			if(f.getTitle().toLowerCase().contains(searchHint)){
				return (JFrame)f;
			}
		}		
		return null;
	}
	
	public List<Component> getAllComponents(final Container c) //Used for getting all components inside a container and children
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
	
}
