package Model.QuicklinksModel;

import java.io.Serializable;

public final class Quicklinks implements Serializable
{
    private String nextcloudLink="https://nextcloud.hof-university.de/apps/user_saml/saml/login";
    private String moodleLink="https://moodle.hof-university.de/auth/shibboleth/index.php";
    private String panoptoLink="";
    private String primussLink="https://www3.primuss.de/cgi-bin/login/index.pl?FH=fhh";
    private String bayernfahrplanLink="https://www.bayern-fahrplan.de/m5/de/#trips";
    private String campusSportLink="https://sport.aiv.hfoed.de";
    private String campusSportPlanLink="";

    private Quicklinks(){}

    public String getNextcloudLink()
    {
        return nextcloudLink;
    }

    public String getMoodleLink()
    {
        return moodleLink;
    }

    public String getPanoptoLink()
    {
        return panoptoLink;
    }

    public String getPrimussLink()
    {
        return primussLink;
    }

    public String getBayernfahrplanLink()
    {
        return bayernfahrplanLink;
    }

    public String getCampusSportLink()
    {
        return campusSportLink;
    }

    public String getCampusSportPlanLink()
    {
        return campusSportPlanLink;
    }
}
