package server;

import com.google.gson.Gson;
import connexion.PoolDeConnexion;
import dao.ClientDAO;
import dao.EmplacementsDAO;
import pojo.Client;
import pojo.Emplacements;
import pojo.Identification;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;

/**
 * @author ilies, axel
 * @version 1.2
 * Class with all the Server sockets and operation with database
 */
public class ServerProcessor implements Runnable {

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader=null;
    private PoolDeConnexion connection;

    public ServerProcessor(Socket s, PoolDeConnexion connection){
        this.connection = connection;
        this.sock=s;

    }

    /**
     * public method with all the kind of call from the client to server
     */
    public void run() {
        boolean closeConnexion = false;
        while(!sock.isClosed()){
            try{
                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());
                Gson gson = new Gson();
                //first interaction with the client, sending the kind of action
                String demand = read();
                switch(demand.toUpperCase()){
                    case "AJOUTEMPLACEMENT":
                        //Server understands the action asked, he returns "OK"
                        String toSend = "OK for insert";
                        //the Server waits for the data
                        writer.write(toSend);
                        writer.flush();
                        //the server read the data
                        String request = read();
                        Emplacements e1 = gson.fromJson(request, Emplacements.class);
                        EmplacementsDAO daoInsert = new EmplacementsDAO(connection.getConnection());
                        Emplacements eCheck = daoInsert.find(e1.getIdEmplacement());
                        if(eCheck == null)
                        {
                            daoInsert.create(e1);
                            Emplacements e2 = daoInsert.find(e1.getIdEmplacement());
                            String reponseServ = "Nouvel emplacement numéro" + e1.getIdEmplacement()+ " bien ajouté au référentiel, merci !";
                            writer.write(reponseServ);
                            writer.flush();

                        }
                        else
                        {
                            String reponseServ = "Impossible de créer l'objet en question, l'idEmplacement "+ e1.getIdEmplacement() +" est déjà utilisé par un emplacement";
                            writer.write(reponseServ);
                            writer.flush();
                        }
                        connection.releaseConnection(connection.getListUsed().get(connection.getListUsed().size()-1));
                        break;

                    case "DELETEEMPLACEMENT":
                        //Server understands the action asked, he returns "OK"
                        String toSendDelete = "OK for insert";
                        //the Server waits for the data
                        writer.write(toSendDelete);
                        writer.flush();
                        //the server read the data
                        String requestDelete = read();
                        Emplacements eDelete = gson.fromJson(requestDelete, Emplacements.class);
                        EmplacementsDAO daoDelete = new EmplacementsDAO(connection.getConnection());
                        System.out.println("nbr id: "+ eDelete.getIdEmplacement());
                        if(daoDelete.find(eDelete.getIdEmplacement())== null){
                            String reponseServ = "Impossible de supprimer, emplacement inexistant";
                            writer.write(reponseServ);
                            writer.flush();
                        }
                        else {
                            daoDelete.delete(eDelete);
                            String reponseServ =" Emplacement n°"+eDelete.getIdEmplacement() +" bien supprimé !";
                            writer.write(reponseServ);
                            writer.flush();

                        }
                        connection.releaseConnection(connection.getListUsed().get(connection.getListUsed().size()-1));
                        break;
                    case "FINDEMPLACEMENT":
                        //Server understands the action asked, he returns "OK"
                        String toSend2 = "OK for find";
                        //the Server waits for the data
                        writer.write(toSend2);
                        writer.flush();
                        //the server read the data
                        String toFind = read();
                        System.out.println("donnée reçu sur le server: "+toFind);
                        Emplacements e2 = gson.fromJson(toFind, Emplacements.class);
                        System.out.println("donnée converti en int: "+ e2.getIdEmplacement());
                        EmplacementsDAO daoFind = new EmplacementsDAO(connection.getConnection());
                        Emplacements eFind =daoFind.find(e2.getIdEmplacement());
                        String jsonFind = gson.toJson(eFind);
                        //the server looks and find (or not) the data asked and return his answer to the client
                        if(jsonFind == null){
                            String failFind = "";
                            writer.write(failFind);
                            writer.flush();
                        }else {
                            writer.write(jsonFind);
                            writer.flush();

                        }
                        connection.releaseConnection(connection.getListUsed().get(connection.getListUsed().size()-1));
                        break;
                    case "PROFILCLIENT":
                        //Server understands the action asked, he returns "OK"
                        String toSend3 = "OK for find client";
                        //the Server waits for the data
                        writer.write(toSend3);
                        writer.flush();
                        //the server read the data
                        String checkIdentification = read();
                        Identification identification = gson.fromJson(checkIdentification, Identification.class);
                        ClientDAO clientDAO = new ClientDAO(connection.getConnection());
                        Client c = clientDAO.ConnectionClient( identification.getPseudo(), identification.getPassword());
                        if(c == null){
                            String failFind = "";
                            writer.write(failFind);
                            writer.flush();
                        }else{
                            String jsonFindClient = gson.toJson(c);
                            writer.write(jsonFindClient);
                            writer.flush();
                        }
                        break;
                    case "PROFILAGE":
                        break;
                }

            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    /**
     * This methods read the message from the client
     * @return String
     * @throws IOException
     */
    private String read() throws IOException {
        String response ="";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}
