package pojo;

public class Identification {
    private String pseudo;
    private String password;
    public Identification(String pseudo, String password){
        this.pseudo=pseudo;
        this.password=password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
