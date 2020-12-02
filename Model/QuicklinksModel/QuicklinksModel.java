package Model.QuicklinksModel;

public class QuicklinksModel
{
    private static String nextcloudLink;
    private static String moodleLink;
    private static String panoptoLink;
    private static String primussLink;
    private static String oepnvLink="https://www.bayern-fahrplan.de/m5/de/#trips";
    private static String campusSportLink;
    private static String campusSportPlanLink;

    public static String getNextcloudLink()
    {
        return nextcloudLink;
    }

    public static String getMoodleLink()
    {
        return moodleLink;
    }

    public static String getPanoptoLink()
    {
        return panoptoLink;
    }

    public static String getPrimussLink()
    {
        return primussLink;
    }

    public static String getOepnvLink()
    {
        return oepnvLink;
    }

    public static String getCampusSportLink()
    {
        return campusSportLink;
    }

    public static String getCampusSportPlanLink()
    {
        return campusSportPlanLink;
    }
}
