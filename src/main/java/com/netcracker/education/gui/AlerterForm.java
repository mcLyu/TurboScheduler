/*
 * Created by JFormDesigner on Tue Dec 02 03:51:52 BRT 2014
 */

package com.netcracker.education.client.gui;

import java.awt.event.*;

import com.netcracker.education.cache.beans.TaskService;
import com.netcracker.education.cache.interfaces.Task;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Ilya Lyubaikin
 */

public class AlerterForm extends JFrame {
    TaskService taskService;

    enum USER_CHANGED {DONE, WAIT, NULL;}

    USER_CHANGED result = USER_CHANGED.NULL;

    public AlerterForm() {
        initComponents();
    }

    public boolean waitResult(Task alertTask) {
        setVisible(true);
        taskNameLabel.setText(alertTask.getName());
        descriptionArea.setText(alertTask.getDescription());
        dateSpinner.setValue(alertTask.getAlertTime());
        while (result == USER_CHANGED.NULL) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (result == USER_CHANGED.DONE) return true;
        else return false;
    }

    private void button2ActionPerformed(ActionEvent e) {
        setVisible(false);
        result = USER_CHANGED.DONE;
    }

    private void waitButtonActionPerformed(ActionEvent e) {
        setVisible(false);
        result = USER_CHANGED.WAIT;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
        label1 = new JLabel();
        taskNameLabel = new JLabel();
        label2 = new JLabel();
        dateSpinner = new JSpinner();
        label3 = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        label4 = new JLabel();
        scrollPane2 = new JScrollPane();
        descriptionArea = new JTextArea();
        waitButton = new JButton();
        doneButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Alert!");
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("Task name:");

        //---- taskNameLabel ----
        taskNameLabel.setText("text");

        //---- label2 ----
        label2.setText("Alert date:");

        //---- dateSpinner ----
        dateSpinner.setModel(new SpinnerDateModel(new java.util.Date(1417503600000L), null, null, java.util.Calendar.DAY_OF_MONTH));

        //---- label3 ----
        label3.setText("Contacts:");

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(list1);
        }

        //---- label4 ----
        label4.setText("Description");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(descriptionArea);
        }

        //---- waitButton ----
        waitButton.setText("Wait(30 min)");
        waitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waitButtonActionPerformed(e);
            }
        });

        //---- doneButton ----
        doneButton.setText("Done");
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button2ActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(label1)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(taskNameLabel)
                                        .addComponent(dateSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label3))
                                .addGap(17, 17, 17))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPaneLayout.createParallelGroup()
                                                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                        .addComponent(label4)
                                                        .addGap(304, 304, 304))
                                                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap()))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(waitButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(doneButton, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15))))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(taskNameLabel)
                                        .addComponent(label3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label2)
                                                .addComponent(dateSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addComponent(label4)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(doneButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(waitButton, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(13, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
    private JLabel label1;
    private JLabel taskNameLabel;
    private JLabel label2;
    private JSpinner dateSpinner;
    private JLabel label3;
    private JScrollPane scrollPane1;
    private JList list1;
    private JLabel label4;
    private JScrollPane scrollPane2;
    private JTextArea descriptionArea;
    private JButton waitButton;
    private JButton doneButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
