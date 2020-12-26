package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileTransferController implements ActionListener {
    private FileTransferView view;

    public FileTransferController(FileTransferView view) {
        this.view = view;
        view.getBtnBrowse().addActionListener(this);
        view.getBtnSendFile().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(view.getBtnBrowse().getText())) {
            view.chooseFile();
        }
        if (e.getActionCommand().equals(view.getBtnSendFile().getText())) {
        	Thread t = new Thread() {
        		@Override
        		public void run() {
        			String host = view.getTextFieldHost().getText().trim();
                    int port = Integer.parseInt(view.getTextFieldPort().getText().trim());
                    String sourceFilePath = view.getTextFieldFilePath().getText();
                    if (host != "" && sourceFilePath != "") {
                        Client tcpClient = new Client(host, port,
                                view.getTextAreaResult(), view.getProgress());
                        tcpClient.connectServer();
                        tcpClient.sendFile(sourceFilePath);
                        tcpClient.closeSocket();
                    }
                    else {
                        JOptionPane.showMessageDialog(view, "Host, Port "
                                + "và FilePath phải khác rỗng.");
                    }
        		}
        	};
            t.start();
         } 
     }
}