package client;

public class FileTransferMain {

	public static void main(String[] args) {
		FileTransferView view = new FileTransferView(); // khoi tao giao dien
	    new FileTransferController(view);
	}
}
