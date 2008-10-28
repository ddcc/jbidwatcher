package com.jbidwatcher.ui.config;
/*
 * Copyright (c) 2000-2007, CyberFOX Software, Inc. All Rights Reserved.
 *
 * Developed by mrs (Morgan Schweers)
 */

import com.jbidwatcher.ui.util.JPasteListener;
import com.jbidwatcher.ui.util.SpringUtilities;
import com.jbidwatcher.util.config.JConfig;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class JConfigMyJBidwatcherTab extends JConfigTab {
  private JCheckBox mEnable;
  private JTextField mEmail;
  private JPasswordField mPassword;
  private JTextField mUserId;
  private JButton mCreateOrUpdate;

  public String getTabName() { return "My JBidwatcher"; }
  public void cancel() { }

  public boolean apply() {
    boolean enabled = mEnable.isSelected();
    JConfig.setConfiguration("my.jbidwatcher.enabled", Boolean.toString(enabled));

    String email = mEmail.getText();
    if(email != null) JConfig.setConfiguration("my.jbidwatcher.email", email);
    char[] password = mPassword.getPassword();
    if(password != null) JConfig.setConfiguration("my.jbidwatcher.password", new String(password));

    return true;
  }

  public void updateValues() {
    String email = JConfig.queryConfiguration("my.jbidwatcher.email", "");
    String pass = JConfig.queryConfiguration("my.jbidwatcher.password", "");
    boolean enabled = JConfig.queryConfiguration("my.jbidwatcher.enabled", "false").equals("true");
    String id = JConfig.queryConfiguration("my.jbidwatcher.id");

    mEmail.setText(email);
    mPassword.setText(pass);
    mEnable.setSelected(enabled);

    if(id != null) {
      mUserId.setText(id);
      mCreateOrUpdate.setText("Update");
    } else {
      mUserId.setText("");
      mCreateOrUpdate.setText("Create");
    }

    for(ActionListener al : mEnable.getActionListeners()) {
      al.actionPerformed(new ActionEvent(mEnable, ActionEvent.ACTION_PERFORMED, "Redraw"));
    }
  }

  private void setComponentTooltip(JComponent comp, String text) {
    comp.setToolTipText(text);
    comp.getAccessibleContext().setAccessibleDescription(text);
  }

  private JPanel buildUserSettings() {
    JPanel jp = new JPanel(new BorderLayout());
    jp.setBorder(BorderFactory.createTitledBorder("My JBidwatcher User Settings"));

    JPanel innerPanel = new JPanel();
    innerPanel.setLayout(new SpringLayout());

    mEmail = new JTextField();
    mEmail.addMouseListener(JPasteListener.getInstance());
    setComponentTooltip(mEmail, "Email address to use for your My JBidwatcher account.");
    final JLabel emailLabel = new JLabel("Email Address:");
    emailLabel.setLabelFor(mEmail);
    innerPanel.add(emailLabel);
    innerPanel.add(mEmail);

    mPassword = new JPasswordField();
    mPassword.addMouseListener(JPasteListener.getInstance());
    setComponentTooltip(mPassword, "Password to use on My JBidwatcher (NOT the same as your eBay password!)");
    final JLabel passwordLabel = new JLabel("My JBid Password:");
    passwordLabel.setLabelFor(mPassword);
    innerPanel.add(passwordLabel);
    innerPanel.add(mPassword);

    innerPanel.add(new JLabel(""));
    final JLabel mWarningMessage = new JLabel("This must not be the same as your eBay password!", JLabel.RIGHT);
    mWarningMessage.setFont(mWarningMessage.getFont().deriveFont(Font.ITALIC | Font.BOLD));
    Box button = Box.createHorizontalBox();
    mCreateOrUpdate = new JButton("");
    mCreateOrUpdate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if(action == null) return;

        boolean create = action.equals("Create");
        boolean update = action.equals("Update");

        if(create) {
          createNewUser();
        } else if(update) {
          updateUser();
        } else {
          System.err.println("Unknown action command: " + action);
        }
      }
    });
    button.add(mCreateOrUpdate);
    button.add(Box.createHorizontalGlue());
    button.add(mWarningMessage);
    innerPanel.add(button);

    mUserId = new JTextField();
    mUserId.setEditable(false);
    JLabel userIdLabel = new JLabel("My JBidwatcher Id:");
    userIdLabel.setEnabled(false);
    userIdLabel.setLabelFor(mUserId);
    innerPanel.add(userIdLabel);
    innerPanel.add(mUserId);

    SpringUtilities.makeCompactGrid(innerPanel, 4, 2, 6, 6, 6, 1);

    mEnable = new JCheckBox();
    mEnable.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        boolean selected = mEnable.isSelected();

        emailLabel.setEnabled(selected);
        mEmail.setEnabled(selected);
        mEmail.setEditable(selected);

        passwordLabel.setEnabled(selected);
        mPassword.setEnabled(selected);
        mPassword.setEditable(selected);

        mCreateOrUpdate.setEnabled(selected);
        mWarningMessage.setEnabled(selected);
        mUserId.setEnabled(selected);
      }
    });
    JLabel enableLabel = new JLabel("Enable My JBidwatcher");
    enableLabel.setLabelFor(mEnable);

    jp.add(makeLine(mEnable, enableLabel), BorderLayout.NORTH);
    jp.add(innerPanel, BorderLayout.CENTER);

    return(jp);
  }

  private void updateUser() {
    //To change body of created methods use File | Settings | File Templates.
  }

  private void createNewUser() {
    //To change body of created methods use File | Settings | File Templates.
  }

  public JConfigMyJBidwatcherTab() {
    super();
    this.setLayout(new BorderLayout());
    JPanel jp = new JPanel();
    jp.setLayout(new BorderLayout());
    jp.add(buildUserSettings(), BorderLayout.NORTH);
//    jp.add(buildExtraSettings(), BorderLayout.SOUTH);
    this.add(panelPack(jp), BorderLayout.NORTH);
    updateValues();
  }
}