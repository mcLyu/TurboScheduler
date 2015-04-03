/*
 * Created by JFormDesigner on Sat Nov 22 17:09:39 GST 2014
 */

package com.netcracker.education.gui;

import com.netcracker.education.Client;
import com.netcracker.education.cache.entities.TaskImpl;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * @author unknown
 */
public class MainForm extends JFrame implements Observer {
    Client client;
    TaskList taskList;

    public MainForm(Client client) {
        this.client = client;
        client.registerObserver(this);
        initComponents();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dateSpinner.setValue(new Date());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void update(Object obj) {
        taskList = (TaskList) obj;
        taskListView.setListData(taskList.getActiveTaskNames());
        performedListView.setListData(taskList.getPerformedTaskNames());
    }
    public void updateTaskList(TaskList taskList) {

    }

    private void addButtonActionPerformed(ActionEvent e) {
        if (!nameTextField.getText().equals("")) {
            Task task = new TaskImpl(nameTextField.getText(), descriptionArea.getText(), (Date) dateSpinner.getValue());
            client.addTask(task);
        } else JOptionPane.showMessageDialog(this, "Task without name!");
    }

    private void taskListViewValueChanged(ListSelectionEvent e) {
        int index = taskListView.getSelectedIndex();
        if (index != -1) {
            Task task = taskList.getTask(index);
            nameTextField.setText(task.getName());
            dateSpinner.setValue(task.getAlertTime());
            descriptionArea.setText(task.getDescription());
        }
    }

    private void deleteButtonActionPerformed(ActionEvent e) {
        int indexActive = taskListView.getSelectedIndex();
        int indexPerformed = performedListView.getSelectedIndex();
        if (indexActive != -1) {
            client.deleteTask(indexActive);
            taskListView.setSelectedIndex(indexActive);
        } else if (indexPerformed != -1) {
            int activeTasksSize = taskList.getActiveTasks().size();
            client.deleteTask(activeTasksSize + indexPerformed);
            performedListView.setSelectedIndex(indexPerformed);
        } else JOptionPane.showMessageDialog(this, "No task selected!");
    }

    private void saveButtonActionPerformed(ActionEvent e) {
        int indexActive = taskListView.getSelectedIndex();
        int indexPerformed = performedListView.getSelectedIndex();
        if (indexActive != -1) {
            client.deleteTask(indexActive);
            client.addTask(new TaskImpl(nameTextField.getText(), descriptionArea.getText(), (Date) dateSpinner.getValue()));
        } else if (indexPerformed != -1) {
            int activeTasksSize = taskList.getActiveTasks().size();
            client.deleteTask(activeTasksSize+ indexPerformed);
            client.addTask(new TaskImpl(nameTextField.getText(), descriptionArea.getText(), (Date) dateSpinner.getValue()));
        } else JOptionPane.showMessageDialog(this, "No task selected!");
    }

    private void thisWindowClosing(WindowEvent e) {
        /*try {
            //storage.saveData(taskService.getTaskList());
        } catch (StorageException e1) {
            e1.printStackTrace();
        }*/
        System.exit(0);
    }

    private void performedListViewValueChanged(ListSelectionEvent e) {
        int index = performedListView.getSelectedIndex();
        if (index != -1) {
            int activeTasksSize = taskList.getActiveTasks().size();
            Task task = taskList.getTask(activeTasksSize + index);
            nameTextField.setText(task.getName());
            dateSpinner.setValue(task.getAlertTime());
            descriptionArea.setText(task.getDescription());
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
        scrollPane1 = new JScrollPane();
        taskListView = new JList();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        scrollPane2 = new JScrollPane();
        contactList = new JList();
        label6 = new JLabel();
        deleteButton = new JButton();
        addButton = new JButton();
        saveButton = new JButton();
        nameTextField = new JTextField();
        scrollPane3 = new JScrollPane();
        descriptionArea = new JTextArea();
        dateSpinner = new JSpinner();
        scrollPane4 = new JScrollPane();
        performedListView = new JList();
        label7 = new JLabel();

        //======== this ========
        setTitle("Scheduler");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();

        //======== scrollPane1 ========
        {

            //---- taskListView ----
            taskListView.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    taskListViewValueChanged(e);
                }
            });
            scrollPane1.setViewportView(taskListView);
        }

        //---- label1 ----
        label1.setText("Task list");

        //---- label2 ----
        label2.setText("Task");

        //---- label3 ----
        label3.setText("Task name:");

        //---- label4 ----
        label4.setText("Alert date:");

        //---- label5 ----
        label5.setText("Contact list");

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(contactList);
        }

        //---- label6 ----
        label6.setText("Task description");

        //---- deleteButton ----
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonActionPerformed(e);
            }
        });

        //---- addButton ----
        addButton.setText("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(e);
            }
        });

        //---- saveButton ----
        saveButton.setText("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonActionPerformed(e);
            }
        });

        //======== scrollPane3 ========
        {
            scrollPane3.setViewportView(descriptionArea);
        }

        //---- dateSpinner ----
        dateSpinner.setModel(new SpinnerDateModel(new java.util.Date((System.currentTimeMillis()/60000)*60000), null, null, java.util.Calendar.MINUTE));

        //======== scrollPane4 ========
        {

            //---- performedListView ----
            performedListView.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    performedListViewValueChanged(e);
                }
            });
            scrollPane4.setViewportView(performedListView);
        }

        //---- label7 ----
        label7.setText("Performed tasks");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteButton)
                        .addComponent(label7)
                        .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
                        .addComponent(scrollPane1))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(label3)
                                .addComponent(label4))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nameTextField))
                        .addComponent(scrollPane2)
                        .addComponent(scrollPane3)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(label2)
                                .addComponent(dateSpinner, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label5)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(addButton, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
                                .addComponent(label6))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(label2)
                        .addComponent(label1))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(label3)
                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label4)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label5)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label7)
                                .addComponent(label6))))
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                        .addComponent(scrollPane4, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(addButton)
                            .addComponent(saveButton))
                        .addComponent(deleteButton))
                    .addGap(18, 18, 18))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ilya Lyubaikin
    private JScrollPane scrollPane1;
    private JList taskListView;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JScrollPane scrollPane2;
    private JList contactList;
    private JLabel label6;
    private JButton deleteButton;
    private JButton addButton;
    private JButton saveButton;
    private JTextField nameTextField;
    private JScrollPane scrollPane3;
    private JTextArea descriptionArea;
    private JSpinner dateSpinner;
    private JScrollPane scrollPane4;
    private JList performedListView;
    private JLabel label7;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
