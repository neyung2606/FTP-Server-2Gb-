package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server extends Thread {
    private ServerSocket ss;
    private int port = 9900;
    private DataInputStream inFromClient;
    DataOutputStream dos;

    public void open() {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            Socket server = null;
            RandomAccessFile rw = null;
            long current_file_pointer = 0;
            boolean loop_break = false;
            try {
                // accept connect from client and create Socket object
                server = ss.accept();
                inFromClient = new DataInputStream(server.getInputStream());
                dos = new DataOutputStream(server.getOutputStream());
                // check folder existed
                File fileOrDir = new File("C:\\");
                checkFiles(fileOrDir);

                // receive file info
                while (true) {
                    byte[] initilize = new byte[1];
                    try {
                        inFromClient.read(initilize, 0, initilize.length);
                        if (initilize[0] == 2) {
                            byte[] cmd_buff = new byte[3];
                            inFromClient.read(cmd_buff, 0, cmd_buff.length);
                            byte[] recv_data = ReadStream();	
                            switch (Integer.parseInt(new String(cmd_buff))) {
                                case 124:
                                    rw = new RandomAccessFile("C:/File/" + new String(recv_data), "rw");
                                    dos.write(CreateDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                                    dos.flush();
                                    break;
                                case 126:
                                    rw.seek(current_file_pointer);
                                    rw.write(recv_data);
                                    current_file_pointer = rw.getFilePointer();
//                            System.out.println("Download percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                                    dos.write(CreateDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                                    dos.flush();
                                    break;
                                case 127:
                                    if ("Close".equals(new String(recv_data))) {
                                        loop_break = true;
                                        rw.close();
                                    }
                                    break;
                            }
                        }
                        if (loop_break == true) {
                            break;
                        }
                    } catch (IOException ex) {

                    } catch (NullPointerException exx) {
                        System.out.println("a");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }

    public void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkFiles(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            Arrays.sort(children, new Comparator<File>() {
                public int compare(final File o1, final File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            if (listFile(children) == false) {
                System.out.println("Tao file");
                File path = new File("C:\\File");
                path.mkdir();
            }
        } else {
            System.out.println(fileOrDir.getAbsolutePath());
        }
    }

    public boolean listFile(File[] list) {
        for (File each : list) {
            if (each.getName().equals("File")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Server sv = new Server();
        sv.open();
        sv.start();
    }

    private byte[] ReadStream() {
        byte[] data_buff = null;
        try {
            int b = 0;
            String buff_length = "";
            while ((b = inFromClient.read()) != 4) {
                buff_length += (char) b;
            }
            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length) {
                byte_read = inFromClient.read(data_buff, byte_offset, data_length - byte_offset);
                byte_offset += byte_read;
            }
        } catch (IOException ex) {

        }
        return data_buff;
    }

    private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
        try {
	        byte[] initialize = new byte[1];
	        initialize[0] = 2;
	        byte[] separator = new byte[1];
	        separator[0] = 4;
	        byte[] data_length;
			data_length = String.valueOf(data.length).getBytes("UTF8");
	        packet = new byte[initialize.length + cmd.length + separator.length + data_length.length + data.length];
	
	        System.arraycopy(initialize, 0, packet, 0, initialize.length);
	        System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
	        System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
	        System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length, separator.length);
	        System.arraycopy(data, 0, packet, initialize.length + cmd.length + data_length.length + separator.length, data.length);
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return packet;
    }
}