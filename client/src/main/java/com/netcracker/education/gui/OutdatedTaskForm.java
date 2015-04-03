/*
 * Created by JFormDesigner on Sat Dec 06 04:41:54 BRT 2014
 */

package com.netcracker.education.gui;

import com.netcracker.education.cache.entities.TaskImpl;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Task;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ilya Lyubaikin
 */
public class OutdatedTaskForm extends JDialog {
    TaskList tasksList;
    List<Task> outdatedTasks;
    Task selectedTask;

    public OutdatedTaskForm(TaskList taskList) {
        this.tasksList = taskList;
        this.outdatedTasks = selectOverdate(taskList);
        if (outdatedTasks.isEmpty()) return;
        initComponents();
        updateListView();
        setVisible(true);
    }

    private void updateListView() {
        if (asNames().length != 0)
            list1.setListData(asNames());
        else
            setVisible(false);
    }

    private List<Task> selectOverdate(TaskList taskList) {

        List<Task> outdatedTasks = new LinkedList<>();
        for (int i = 0; i < taskList.getActiveTasks().size(); i++) {
            Task task = taskList.getActiveTasks().get(i);
            if (task.getAlertTime().before(new Date())) {
                outdatedTasks.add(task);
            }
        }

        return outdatedTasks;
    }

    private Object[] asNames() {
        Object[] names = new Object[outdatedTasks.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = outdatedTasks.get(i).getName();
        }
        return names;
    }

    private void list1ValueChanged(ListSelectionEvent e) {
        int index = list1.getSelectedIndex();
        if (index != -1) {
            selectedTask = outdatedTasks.get(index);
            taskNameLabel.setText(selectedTask.getName());
            spinner1.setValue(selectedTask.getAlertTime());
            textArea1.setText(selectedTask.getDescription());
        }
    }

    private void dismissButtonActionPerformed(ActionEvent e) {
        outdatedTasks.remove(selectedTask);
        updateListView();
    }

    private void dismissAllButtonActionPerformed(ActionEvent e) {
        setVisible(false);
    }

    private void ProlongActionPerformed(ActionEvent e) {
        int prolongTime = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the minutes time of prolong:",
                "Prolong time", JOptionPane.PLAIN_MESSAGE));
        Task prolongedTask = new TaskImpl(selectedTask.getName(), selectedTask.getDescription(),
                new Date(selectedTask.getAlertTime().getTime() + 60 * 1000 * prolongTime));
        tasksList.setTask(selectedTask, prolongedTask);
        outdatedTasks.remove(selectedTask);
        updateListView();
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
        scrollPane1 = new JScrollPane();
        textPane1 = new JTextPane();
        scrollPane2 = new JScrollPane();
        list1 = new JList();
        label1 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        taskNameLabel = new JLabel();
        spinner1 = new JSpinner();
        scrollPane3 = new JScrollPane();
        textArea1 = new JTextArea();
        Prolong = new JButton();
        dismissButton = new JButton();
        dismissAllButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {

            //---- textPane1 ----
            textPane1.setText("Since the previous run of the program, these tasks were overdated. Select the action for them.");
            scrollPane1.setViewportView(textPane1);
        }

        //======== scrollPane2 ========
        {

            //---- list1 ----
            list1.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    list1ValueChanged(e);
                }
            });
            scrollPane2.setViewportView(list1);
        }

        //---- label1 ----
        label1.setText("Task name: ");

        //---- label3 ----
        label3.setText("Alert date:");

        //---- label4 ----
        label4.setText("Task description:");

        //---- taskNameLabel ----
        taskNameLabel.setText("default task name");

        //---- spinner1 ----
        spinner1.setModel(new SpinnerDateModel());

        //======== scrollPane3 ========
        {
            scrollPane3.setViewportView(textArea1);
        }

        //---- Prolong ----
        Prolong.setText("Prolong");
        Prolong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
                ProlongActionPerformed(e);
            }
        });

        //---- dismissButton ----
        dismissButton.setText("Dismiss");
        dismissButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
                dismissButtonActionPerformed(e);
            }
        });

        //---- dismissAllButton ----
        dismissAllButton.setText("Dismiss All");
        dismissAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
                dismissAllButtonActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(58, 58, 58)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addComponent(label1)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(taskNameLabel))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addComponent(label3)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(label4))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(Prolong, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dismissButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dismissAllButton, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 365, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(13, Short.MAX_VALUE))))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1)
                                .addComponent(taskNameLabel))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(5, 5, 5)
                            .addComponent(label4)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(Prolong)
                        .addComponent(dismissButton)
                        .addComponent(dismissAllButton))
                    .addContainerGap(41, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
    private JScrollPane scrollPane1;
    private JTextPane textPane1;
    private JScrollPane scrollPane2;
    private JList list1;
    private JLabel label1;
    private JLabel label3;
    private JLabel label4;
    private JLabel taskNameLabel;
    private JSpinner spinner1;
    private JScrollPane scrollPane3;
    private JTextArea textArea1;
    private JButton Prolong;
    private JButton dismissButton;
    private JButton dismissAllButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
