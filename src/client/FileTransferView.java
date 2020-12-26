package client;

import java.awt.Color;

import javax.swing.*;

public class FileTransferView extends JFrame {

	private static final long serialVersionUID = 1L;

    private JLabel labelHost;
    private JTextField textFieldHost;
    private JLabel labelPort;
    private JTextField textFieldPort;
    private JButton btnBrowse;
    private JTextField textFieldFilePath;
    private JButton btnSendFile;
    private JTextArea textAreaResult;
    private JProgressBar progress;

    public FileTransferView() {
        setTitle("Client - Truyen file bang giao thuc TCP/IP");
        
        labelHost = new JLabel("Host:");
        textFieldHost = new JTextField();
        labelPort = new JLabel("Port:");
        textFieldPort = new JTextField();
        
        labelHost.setBounds(20, 20, 50, 25);
        textFieldHost.setBounds(70, 20, 200, 25);
        labelPort.setBounds(300, 20, 50, 25);
        textFieldPort.setBounds(350, 20, 200, 25);

        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(20, 50, 450, 25);
        
        btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(470, 50, 80, 25);
        btnBrowse.setBackground(Color.BLUE);
        btnBrowse.setForeground(Color.WHITE);
        
        btnSendFile = new JButton("Send File");
        btnSendFile.setBounds(200, 90, 150, 25);
        btnSendFile.setBackground(Color.WHITE);
        btnSendFile.setForeground(Color.black);
        
        progress = new JProgressBar(0, 100);
        progress.setValue(0);
        progress.setBounds(10, 120, 550, 30);
        progress.setStringPainted(true);
        
        textAreaResult = new JTextArea();
        textAreaResult.setBounds(10, 170, 550, 150);

        add(labelHost);
        add(textFieldHost);
        add(labelPort);
        add(textFieldPort);
        add(textFieldFilePath);
        add(btnBrowse);
        add(btnSendFile);
        add(textAreaResult);
        add(progress);

        setLayout(null);
        setSize(600, 350);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void chooseFile() {
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        try {
            if (fc.getSelectedFile() != null) {
                textFieldFilePath.setText(fc.getSelectedFile().getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JLabel getLabelHost() {
        return labelHost;
    }

    public void setLabelHost(JLabel labelHost) {
        this.labelHost = labelHost;
    }

    public JTextField getTextFieldHost() {
        return textFieldHost;
    }

    public void setTextFieldHost(JTextField textFieldHost) {
        this.textFieldHost = textFieldHost;
    }

    public JLabel getLabelPort() {
        return labelPort;
    }

    public void setLabelPort(JLabel labelPort) {
        this.labelPort = labelPort;
    }

    public JTextField getTextFieldPort() {
        return textFieldPort;
    }

    public void setTextFieldPort(JTextField textFieldPort) {
        this.textFieldPort = textFieldPort;
    }

    public JButton getBtnBrowse() {
        return btnBrowse;
    }

    public void setBtnBrowse(JButton btnBrowse) {
        this.btnBrowse = btnBrowse;
    }

    public JTextField getTextFieldFilePath() {
        return textFieldFilePath;
    }

    public void setTextFieldFilePath(JTextField textFieldFilePath) {
        this.textFieldFilePath = textFieldFilePath;
    }

    public JButton getBtnSendFile() {
        return btnSendFile;
    }

    public void setBtnSendFile(JButton btnSendFile) {
        this.btnSendFile = btnSendFile;
    }

    public JTextArea getTextAreaResult() {
        return textAreaResult;
    }

    public void setTextAreaResult(JTextArea textAreaResult) {
        this.textAreaResult = textAreaResult;
    }
    
    public JProgressBar getProgress() {
    	return progress;
    }
    
    public void setProgress(JProgressBar progress) {
    	this.progress = progress;
    }
}
