import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {

	private Socket socket;

	public Cliente(String ip, int porta, String pasta) {
		try {
			System.out.println("Iniciando!");
			Sincronizar(pasta, ip, porta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//instancia responsavel pela sincronização dos arquivos 
	public void Sincronizar(String pasta, String ip, int porta) throws IOException {
		socket = new Socket(ip, porta);

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeUTF(pasta);
		objectOutputStream.flush();

		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream()); 

		File[] files = new File(pasta).listFiles();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
		DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

		dataOutputStream.writeInt(files.length);

		for(File file : files)
		{
			long length = file.length();
			dataOutputStream.writeLong(length);

			String name = file.getName();
			dataOutputStream.writeUTF(name);

			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);

			int theByte = 0;
			while((theByte = bis.read()) != -1)
				bufferedOutputStream.write(theByte);

			bis.close();
		}
		dataOutputStream.close();
		socket.close();
	}
//setar IP, porta que sera usada e caminho onde o programa cliente esta
	public static void main(String[] args) {
		String ip = "192.168.100.107";
		int porta = 12345;
		String pasta = "C:\\SocketArquivo\\cliente";
		Cliente fc = new Cliente(ip, porta, pasta);
	}

}
//java Cliente.java faz com que o programa execute e sindronize entre as pastas