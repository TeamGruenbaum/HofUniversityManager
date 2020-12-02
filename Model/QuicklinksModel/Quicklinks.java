package Model.QuicklinksModel;

public final class Quicklinks
{
    private static String nextcloudLink;
    private static String moodleLink="https://moodle.hof-university.de/";
    private static String panoptoLink;
    private static String primussLink;
    private static String bayernfahrplanLink="https://www.bayern-fahrplan.de/m5/de/#trips";
    private static String campusSportLink;
    private static String campusSportPlanLink;


    private Quicklinks(){}

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

    public static String getBayernfahrplanLink()
    {
        return bayernfahrplanLink;
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
