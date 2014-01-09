package com.ploader;

import java.awt.Container;

/***
 *@author Xel
 *@version 1.0
 *@project PLoader
 *@file Plugin.java
 *@date 9.1.2014
 *@time 8.21.09
 */
public abstract class Plugin {

	public abstract Container gui();
	
	public abstract void update();
	
	public abstract boolean exit();
	
	public Tools getTools(){
		return Ploader.getPloader().getTools();
	}
	
}
