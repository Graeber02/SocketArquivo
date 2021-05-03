import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
//instancia responsavel pela sincronização dos arquivos utilizando socket
    public Servidor(int porta, String pasta) {
        try {
            ServerSocket server = new ServerSocket(porta);
            System.out.println("Aguardando Conexões.");

            while (true) {
                Socket socket = server.accept();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String diretorio = "";
                diretorio = objectInputStream.readUTF();
                
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

                int count = dataInputStream.readInt();
                File[] arquivos = new File[count];

                for(int i = 0; i < count; i++)
                {
                    long fileLength = dataInputStream.readLong();
                    String fileName = dataInputStream.readUTF();

                    arquivos[i] = new File(pasta + "\\" + fileName);

                    FileOutputStream fileOutputStream = new FileOutputStream(arquivos[i]);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                    for(int j = 0; j < fileLength; j++)
                        bufferedOutputStream.write(dataInputStream.read());

                    bufferedOutputStream.close();
                }
                dataInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
//setar IP, porta que sera usada e caminho onde o programa servidor esta
    public static void main(String[] args) {
        int porta = 12345;
        String pasta = "C:\\SocketArquivo\\servidor";
		Servidor server = new Servidor(porta, pasta);
	}

}
//java Servidor.java faz com que o programa execute e sindronize entre as pastas
