package com.ploader;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/***
 *@author Xel
 *@version 1.0
 *@project PLoader
 *@file PluginPanel.java
 *@date 8.1.2014
 *@time 15.32.27
 */
public class PluginPanel extends JPanel {
	private final JButton btnStart;
	private final JLabel lblPlugin;
	private final JLabel lblAuthor;
	private final JLabel lblVersion;
	public PluginPanel(final String[] pluginData, final Ploader ploader) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 80, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		this.lblPlugin = new JLabel("Plugin:");
		this.lblPlugin.setHorizontalTextPosition(SwingConstants.LEFT);
		this.lblPlugin.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblPlugin = new GridBagConstraints();
		gbc_lblPlugin.anchor = GridBagConstraints.WEST;
		gbc_lblPlugin.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlugin.gridx = 0;
		gbc_lblPlugin.gridy = 0;
		add(this.lblPlugin, gbc_lblPlugin);
		
		this.lblAuthor = new JLabel("Author:");
		this.lblAuthor.setHorizontalTextPosition(SwingConstants.LEFT);
		this.lblAuthor.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
		gbc_lblAuthor.anchor = GridBagConstraints.WEST;
		gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
		gbc_lblAuthor.gridx = 0;
		gbc_lblAuthor.gridy = 1;
		add(this.lblAuthor, gbc_lblAuthor);
		
		this.lblVersion = new JLabel("Version:");
		this.lblVersion.setHorizontalTextPosition(SwingConstants.LEFT);
		this.lblVersion.setHorizontalAlignment(SwingConstants.LEFT);
		final GridBagConstraints gbc_lblVersion = new GridBagConstraints();
		gbc_lblVersion.anchor = GridBagConstraints.WEST;
		gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
		gbc_lblVersion.gridx = 0;
		gbc_lblVersion.gridy = 2;
		add(this.lblVersion, gbc_lblVersion);
		
		this.btnStart = new JButton("Start");
		final GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.anchor = GridBagConstraints.SOUTH;
		gbc_btnStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 3;
		add(this.btnStart, gbc_btnStart);
		
		lblPlugin.setText("Plugin: " + pluginData[0]);
		lblAuthor.setText("Author: " + pluginData[1]);
		lblVersion.setText("Version: " + pluginData[2]);
		
		this.setMaximumSize(new Dimension(9001, 120));
		
		btnStart.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				ploader.launchPlugin(new String[]{pluginData[3], pluginData[4], pluginData[5]});
			}});
	}

}
