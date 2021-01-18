package Model.QuicklinksModel;



public final class Quicklinks
{
	private static final String nextcloudLink="https://nextcloud.hof-university.de";
	private static final String moodleLink="https://moodle.hof-university.de/auth/shibboleth/index.php";
	private static final String panoptoLink="https://panopto.hof-university.de";
	private static final String primussLink="https://www3.primuss.de/cgi-bin/login/index.pl?FH=fhh";
	private static final String bayernfahrplanLink="https://www.bayern-fahrplan.de/m5/de/#trips";
	private static final String campusSportLink="https://sport.aiv.hfoed.de";



	private Quicklinks()
	{
	}


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
}
