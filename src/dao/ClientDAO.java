package dao;

import com.sun.rowset.CachedRowSetImpl;
import pojo.Client;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ilies
 * @version 2
 * this is the Dao for client, which allows to create, delete, update or find a client in the data base. It also allows to have the list of all the client in the database
 */
public class ClientDAO extends DAO<Client> {

    private Connection con;

    /**
     * this is the ClientDAO constructor. This use a connection in the Connection pool to have access to the database
     * @param conn
     */
    public ClientDAO(Connection conn){
        super(conn);
        this.con=conn;
    }

    public Connection getConnection(){
        return this.con;
    }

    /**
     * this method allows to create a client row in the database
     * @param obj
     * @return boolean
     */
    @Override
    public boolean create(Client obj) {
        try{
            int result = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate("INSERT INTO Client( pseudo, password, ClientName, ClientSurname, phone, address, gender) values ('"+obj.getPseudo()+"','"+obj.getPassword()+"','"+obj.getClientName()+"','"+obj.getClientSurname()+"',',"+obj.getPhone()+"','"+obj.getAddress()+"','"+obj.getGender()+"')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * this method allows to delete a client row in the database
     * @param obj
     * @return
     */
    @Override
    public boolean delete(Client obj) {
        try {
            int result = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate("DELETE from Client WHERE idClient=" + obj.getIdClient());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * this method allows to update a client row in the database
     * @param obj
     * @return
     */
    @Override
    public boolean update(Client obj) {
        try {
            int result = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeUpdate("UPDATE Client SET idClient='" + obj.getIdClient() + "',pseudo='" + obj.getPseudo() +"',password='" + obj.getPassword() +"', ClientName='" + obj.getClientName() + "',ClientSurname='" + obj.getClientSurname() + "', phone='" + obj.getPhone() + "', address='" + obj.getAddress()+ "', gender='" + obj.getGender()+"' where idClient=" + obj.getIdClient());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * this method allows to find a client row in the database with its id
     * @param id
     * @return Client
     */
    @Override
    public Client find(int id) {
        try{
            ResultSet result = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT idClient,pseudo, password, ClientName, ClientSurname, phone, address, gender FROM Client Where idClient="+ id);
            while(result.next()){
                Client client = new Client(result.getInt("idClient"),result.getString("pseudo"), result.getString("password"),result.getString("ClientName"), result.getString("ClientSurname"), result.getString("phone"), result.getString("address"), result.getString("gender"));
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method return the list of the client id on the client table
     * @return List<String>
     */
    public List<String> listOfClients(){
        try{
            List<String> clientsList = new ArrayList<String>();
            String sql = "SELECT idClient FROM client";
            CachedRowSet rs = new CachedRowSetImpl();
            rs.setCommand(sql);
            rs.execute(con);
            con.close();
            while (rs.next()) {
                clientsList.add(rs.getString("idClient"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client ConnectionClient( String pseudo, String password){
        try{
            ResultSet result = this.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT idClient,pseudo, password, ClientName, ClientSurname, phone, address, gender FROM Client where pseudo='"+ pseudo +"' and password='"+password+"'");
            while(result.next()){
                Client client = new Client(result.getInt("idClient"),result.getString("pseudo"), result.getString("password"),result.getString("ClientName"), result.getString("ClientSurname"), result.getString("phone"), result.getString("address"), result.getString("gender"));
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
