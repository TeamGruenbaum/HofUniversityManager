package Model.NutzerdatenModel;

public class Login {
    private String benutzername;
    private String passwort;

    public Login (String benutzername, String passwort)
    {
        this.benutzername=benutzername;
        this.passwort=passwort;
    }

    public String getName()
    {
        return benutzername;
    }

    public void setName(String name)
    {
        benutzername=name;
    }

    public String getPasswort()
    {
        return passwort;
    }

    public void setPasswort(String passwort)
    {
        this.passwort=passwort;
    }
}
