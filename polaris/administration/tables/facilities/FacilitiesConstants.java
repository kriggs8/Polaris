package polaris.administration.tables.facilities;

/**
 * Container class for constants shared across modules related to Facilities configuration.
 * <p>
 * Author: Joe Hunsaker
 * Created: 2/21/2017 with additional filter options
 */
class FacilitiesConstants
{

    // Base where statement
    public static final String where0 = "(\n" +
                                        "SELECT DISTINCT F.ID FACILITY_ID, F.ENTITY_COMPANY_ID COMPANY_ID, F.DESCRIPTION, NULL LOCATION_ID, NULL CONTACT_ID, NULL SYSTEM_GROUP_ID, NULL SYSTEM_ID FROM POLARIS.FACILITY_CONF F\n" +
                                        "UNION\n" +
                                        "SELECT DISTINCT F.ID FACILITY_ID, F.ENTITY_COMPANY_ID COMPANY_ID, F.DESCRIPTION, X.LOCATION_ID, X.CONTACT_ID, GLX.SYSTEM_GROUP_ID, SC.ID SYSTEM_ID FROM POLARIS.FACILITY_CONF F, POLARIS.FACILITY_LOCATION_CONTACT_XREF X, POLARIS.GROUP_LOCATION_XREF GLX, POLARIS.SYSTEM_CONF SC\n" +
                                        "WHERE F.ID = X.FACILITY_ID (+)\n" +
                                        "AND X.LOCATION_ID = GLX.LOCATION_ID (+)\n" +
                                        "AND GLX.SYSTEM_GROUP_ID = SC.SYSTEM_GROUP_ID (+) \n" +
                                        ") \n";

    public static final String systemDBWhere0   = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT DISTINCT SYSTEM_ID FROM\n" +
                                                      where0;

    public static final String systemGroupDBWhere0   = "ID IN  \n" +
                                                       "(\n" +
                                                       "SELECT DISTINCT SYSTEM_GROUP_ID FROM\n" +
                                                       where0;

    public static final String locationDBWhere0     = "ID IN  \n" +
                                                        "(\n" +
                                                        "SELECT DISTINCT LOCATION_ID FROM\n" +
                                                        where0;

    public static final String companyDBWhere0      = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT DISTINCT COMPANY_ID FROM\n" +
                                                      where0;

    //query used to display all facilities.
    public static final String facilityDBWhereDefault      = "ID IN\n" +
                                                             "(\n" +
                                                             "SELECT  FACILITY_ID FROM\n" +
                                                             "(\n" +
                                                             "SELECT UNIQUE ID FACILITY_ID\n" +
                                                             "FROM \n" +
                                                             "POLARIS.FACILITY_CONF \n" +
                                                             ") \n";

    public static final String facilityDBWhere0      = "ID IN  \n" +
                                                             "(\n" +
                                                             "SELECT DISTINCT FACILITY_ID FROM\n" +
                                                             where0;

    public static final String systemGroupDBName    = "(SYSTEM_GROUP_ID";
    public static final String systemDBName    = "(SYSTEM_ID";
    public static final String locationDBName    = "(LOCATION_ID";
    public static final String companyDBName    = "(ENTITY_COMPANY_ID";
    public static final String contactDBName    = "(CONTACT_ID";
    public static final String descriptionDBName    = "(DESCRIPTION";
}
