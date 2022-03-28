package com.yaman.ui.impl;

import com.yaman.property.IPropertyManager;
import com.yaman.property.enums.ConnectionProperty;
import com.yaman.ui.IUIGenerator;
import com.yaman.ui.IUIUpdaterService;
import com.yaman.ui.constants.Captions;
import com.yaman.ui.constants.Messages;
import com.yaman.ui.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

@Component
public class UIGenerator implements IUIGenerator {

    @Autowired
    IPropertyManager propertyManager;

    @Autowired
    IUIUpdaterService uiUpdaterService;

    private JFrame frame;
    private JButton startBtn, stopBtn, forceStopBtn;
    private JLabel statusValueLabel, operatingLabel;

    public void initialize(){
        Properties props = propertyManager.getProperties();
        String connectionDetail = String.format("%s:%s",
                        props.getProperty(ConnectionProperty.HOST.getKey()),
                        props.getProperty(ConnectionProperty.PORT.getKey()));

        frame = new JFrame(Captions.FrameTitle);
        JPanel panel = new JPanel(new GridLayout(3,1));
        JPanel connectionDetailsPanel = new JPanel(new GridBagLayout());
        JPanel statusPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JLabel connectionDetailsLabel = new JLabel(Captions.ConnectionDetailsLabel, JLabel.CENTER);
        JLabel connectionDetailsValueLabel = new JLabel(connectionDetail, JLabel.CENTER);
        JLabel statusLabel = new JLabel(Captions.StatusLabel, JLabel.CENTER);
        statusValueLabel = new JLabel(Messages.Loading, JLabel.CENTER);
        startBtn = new JButton(Captions.StartButtonText);
        stopBtn = new JButton(Captions.StopButtonText);
        forceStopBtn = new JButton(Captions.ForceStopButtonText);
        operatingLabel = new JLabel(Messages.OperatingLabel, JLabel.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380,150);
        frame.setResizable(false);
        frame.setContentPane(panel);
        frame.setVisible(true);

        connectionDetailsPanel.setBackground(Color.YELLOW);
        connectionDetailsPanel.add(connectionDetailsLabel);
        connectionDetailsPanel.add(connectionDetailsValueLabel);

        statusPanel.add(statusLabel);
        statusPanel.add(statusValueLabel);
        statusPanel.setBackground(Color.LIGHT_GRAY);

        buttonPanel.add(startBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(forceStopBtn);
        buttonPanel.add(operatingLabel);

        panel.add(connectionDetailsPanel);
        panel.add(statusPanel);
        panel.add(buttonPanel);

        connectionDetailsValueLabel.setForeground(Color.BLUE);

        startBtn.setBackground(Color.GREEN);
        startBtn.setVisible(false);
        startBtn.addActionListener(event -> {
            uiUpdaterService.startAndUpdateView();
        });

        stopBtn.setBackground(Color.ORANGE);
        stopBtn.setVisible(false);
        stopBtn.addActionListener(event -> {
            uiUpdaterService.stopAndUpdateView();
        });

        forceStopBtn.setBackground(Color.RED);
        forceStopBtn.setVisible(false);
        forceStopBtn.addActionListener(event -> {
            uiUpdaterService.forceStopAndUpdateView();
        });

        operatingLabel.setVisible(false);
    }

    public void viewModeStarted(){
        statusValueLabel.setText(Messages.Running);
        statusValueLabel.setForeground(Color.decode("#179020"));
        startBtn.setVisible(false);
        stopBtn.setVisible(true);
        forceStopBtn.setVisible(false);
        operatingLabel.setVisible(false);
    }

    public void viewModeStopped(){
        statusValueLabel.setText(Messages.Stopped);
        statusValueLabel.setForeground(Color.RED);
        startBtn.setVisible(true);
        stopBtn.setVisible(false);
        forceStopBtn.setVisible(false);
        operatingLabel.setVisible(false);
    }

    public void viewModeForcedStop(){
        viewModeStarted();
        forceStopBtn.setVisible(true);
    }

    public void viewModeOperating(){
        startBtn.setVisible(false);
        stopBtn.setVisible(false);
        forceStopBtn.setVisible(false);
        operatingLabel.setVisible(true);
    }

    public void showMessage(String message, MessageType messageType){
        JOptionPane optionPane;
        String title;
        if(messageType.equals(MessageType.INFO)){
            optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
            title = Messages.Info;
        } else if (messageType.equals(MessageType.WARNING)){
            optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
            title = Messages.Warning;
        } else if (messageType.equals(MessageType.ERROR)){
            optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
            title = Messages.Error;
        } else {
            optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
            title = Messages.Info;
        }
        JDialog dialog = optionPane.createDialog(title);
        dialog.setLocationRelativeTo(frame);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
