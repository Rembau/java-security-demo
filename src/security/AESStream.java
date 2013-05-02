package security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESStream {
	public static void main(String[] args) {
		try{
			if(args[0].equals("-genkey")){
				KeyGenerator keygen = KeyGenerator.getInstance("AES");
				SecureRandom random = new SecureRandom();
				keygen.init(random);
				SecretKey key = keygen .generateKey();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]));
		        out.writeObject(key);
		        out.close();
			} else {
				Cipher cipher = Cipher.getInstance("AES");
				ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[3]));
		        Key key = (Key) keyIn.readObject();
		        keyIn.close();
		        int blockSize = cipher.getBlockSize();
		        byte outBytes[] = new byte[blockSize];
		        
				if(args[0].equals("-encrypt")){
					cipher.init(Cipher.ENCRYPT_MODE, key);
					CipherOutputStream outc = new CipherOutputStream(new FileOutputStream(args[2]),cipher);
					InputStream in = new FileInputStream(args[1]);
					int length=in.read(outBytes);
					while(length!=-1){
						outc.write(outBytes, 0, length);
						length = in.read(outBytes);
					}
					outc.flush();
					in.close();
					outc.close();
				} else if(args[0].equals("-decrypt")){
					cipher.init(Cipher.DECRYPT_MODE, key);
					CipherInputStream inc = new CipherInputStream(new FileInputStream(args[1]),cipher);
					OutputStream out = new FileOutputStream(args[2]);
					int length=inc.read(outBytes);
					while(length!=-1){
						out.write(outBytes, 0, length);
						length = inc.read(outBytes);
					}
					out.flush();
					inc.close();
					out.close();
				}
			}
		} catch (IOException e){
	        e.printStackTrace();
	    } catch (GeneralSecurityException e){
	        e.printStackTrace();
	    } catch (ClassNotFoundException e){
	        e.printStackTrace();
	    }
	}
}
