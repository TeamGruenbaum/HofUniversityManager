package Model.StudiengangModel;

public final class StudiengangInformationen {
    private static String studiengangLeiter;
    private static String studiengangTyp;
    private static String spoDataURL;
    private static Modulhandbuch modulhandbuch;


    private StudiengangInformationen() {

    }

    public static void set(String studiengangLeiter, String studiengangTyp, String spoDataURL, Modulhandbuch modulhandbuch){
        StudiengangInformationen.studiengangLeiter = studiengangLeiter;
        StudiengangInformationen.studiengangTyp = studiengangTyp;
        StudiengangInformationen.spoDataURL = spoDataURL;
        StudiengangInformationen.modulhandbuch = modulhandbuch;
    }

    public static String getStudiengangLeiter() {
        return studiengangLeiter;
    }

    public static String getStudiengangTyp() {
        return studiengangTyp;
    }

    public static String getSPO() {
        return spoDataURL;
    }

    public static Modulhandbuch getModulhandbuch() {
        return modulhandbuch;
    }

}
