package analyseAnaxSocket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import analyseAnax.ChiffreDaffaires;
import clientSocket.AbstractClientSocket;

/**
 * @author Anaximandro
 * @version 1.0
 * This chiffreDaffaires socket class is used to return turnover datas
 */

public class SocketChiffreDaffaires extends AbstractClientSocket{

	public Collection<ChiffreDaffaires> getChiffreDaffaires(String type) {
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			Socket s = new Socket(InetAddress.getLocalHost(), 5000);
			PrintWriter w1 = new PrintWriter(s.getOutputStream(), true);
			BufferedInputStream b2 = new BufferedInputStream(s.getInputStream());
			// We inform the server that we want to find data in database
			String demand = "FINDCHIFFREDAFFAIRES";
			w1.write(demand);
			w1.flush();
			// we wait for server's response
			String reponse = read(b2);
			System.out.println(reponse);
			// Now we send to server the JSON file, with the data to insert
			w1.write(type);
			w1.flush();
			// we read the response from the server
			String retourServer = read(b2);
			System.out.println("retour du serveur:" + retourServer);
			Type getChiffre = new TypeToken<ArrayList<ChiffreDaffaires>>(){}.getType();
			Collection <ChiffreDaffaires> chiffres = gson.fromJson(retourServer, getChiffre);
			s.close();
			return chiffres;

		} catch (IOException e) {
			return null;
		}
	}
}
