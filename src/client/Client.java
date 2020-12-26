package client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private Socket client;
    private String host;
    private int port;
    private JTextArea textAreaLog;
    private JProgressBar progress;
    DataOutputStream outToServer;
    DataInputStream dis;

    public Client(String host, int port, JTextArea textAreaLog, JProgressBar progress) {
        this.host = host;
        this.port = port;
        this.textAreaLog = textAreaLog;
        this.progress = progress;
    }

    public void connectServer() {
        try {
            client = new Socket(host, port);
            textAreaLog.append("Connect to server \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String sourceFilePath) {
        try {
            outToServer = new DataOutputStream(client.getOutputStream());
            outToServer.writeUTF("Hello from " + client.getLocalSocketAddress());
            dis = new DataInputStream(client.getInputStream());
            File sourceFile = new File(sourceFilePath);
            outToServer.write(CreateDataPacket("124".getBytes("UTF8"), sourceFile.getName().getBytes("UTF8")));
            outToServer.flush();
            RandomAccessFile rw = new RandomAccessFile(sourceFile, "r");
            long current_file_pointer = 0;
            boolean loop_break = false;
            while (true) {
                if (dis.read() == 2) {
                    byte[] cmd_buff = new byte[3];
                    dis.read(cmd_buff, 0, cmd_buff.length);
                    byte[] recv_buff = ReadStream(dis);
                    switch (Integer.parseInt(new String(cmd_buff))) {
                        case 125:
                            current_file_pointer = Long.valueOf(new String(recv_buff));
                            int buff_len = (int) (rw.length() - current_file_pointer < 20000 ? rw.length() - current_file_pointer : 20000);
                            byte[] temp_buff = new byte[buff_len];
                            if (current_file_pointer != rw.length()) {
                                rw.seek(current_file_pointer);
                                rw.read(temp_buff, 0, temp_buff.length);
                                outToServer.write(CreateDataPacket("126".getBytes("UTF8"), temp_buff));
                                outToServer.flush();
                                progress.setValue((int)(((float) current_file_pointer/rw.length())*100));
                            } else {
                            	progress.setValue(100);
                                loop_break = true;
                                rw.close();
                            }
                            break;
                    }
                }
                if (loop_break == true) {
                    System.out.println("Stop Server informed");
                    outToServer.write(CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                    outToServer.flush();
                    textAreaLog.setText("Gui file thanh cong!!");
                    System.out.println("Client Socket Closed");
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeSocket() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
    	try {
	        byte[] initialize = new byte[1];
	        initialize[0] = 2;
	        byte[] separator = new byte[1];
	        separator[0] = 4;
	        byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
	        packet = new byte[initialize.length + cmd.length + separator.length + data_length.length + data.length];
	
	        System.arraycopy(initialize, 0, packet, 0, initialize.length);
	        System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
	        System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
	        System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length, separator.length);
	        System.arraycopy(data, 0, packet, initialize.length + cmd.length + data_length.length + separator.length, data.length);
    	} catch (UnsupportedEncodingException ex) {
    		
    	}
    	return packet;
    }

    public byte[] ReadStream(DataInputStream din) {
        byte[] data_buff = null;
        try {
            int b = 0;
            String buff_length = "";
            while ((b = din.read()) != 4) {
                buff_length += (char) b;
            }
            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length) {
                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
                byte_offset += byte_read;
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data_buff;
    }
}
