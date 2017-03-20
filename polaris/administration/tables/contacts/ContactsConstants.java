package polaris.administration.tables.contacts;

/**
 * Container class for constants shared across modules related to Contacts configuration.
 * <p>
 * Author: Jean Wurster & Joe Hunsaker
 * Modified: 2/7/2017 with additional filter options
 */
class ContactsConstants
{

    // Base where statement
    public static final String where0 = "(\n" +
                                        "SELECT DISTINCT ID CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, NULL SYSTEM_GROUP_ID, NULL SCHED_SYSTEM_ID, NULL  SHIPPER_ID, NULL CON_CARRIER_ID, NULL LOCATION_ID, NULL PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C\n" +
                                        "UNION \n" +
                                        "SELECT DISTINCT SCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, SSC.SYSTEM_GROUP_ID, SC.SCHED_SYSTEM_ID, SCX.SHIPPER_ID, CCLCX.CON_CARRIER_ID, CCLCX.LOCATION_ID, NULL PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.SCHED_SYSTEM_CONF SSC, POLARIS.SCHEDULE_CONF SC, POLARIS.SCHEDULE_CONTACT_XREF SCX, POLARIS.GROUP_SHIPPER_CONTACT_XREF GSCX, POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX\n" +
                                        "WHERE \n" +
                                        "SCX.CONTACT_ID = C.ID\n" +
                                        "AND C.ID = CCLCX.CONTACT_ID(+)\n" +
                                        "AND SCX.SHIPPER_ID = GSCX.SHIPPER_ID (+)\n" +
                                        "AND SC.SCHED_SYSTEM_ID = SSC.ID\n" +
                                        "AND SCX.SCHEDULE_ID = SC.ID\n" +
                                        "UNION\n" +
                                        "SELECT DISTINCT GSCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, SSC.SYSTEM_GROUP_ID, SC.SCHED_SYSTEM_ID, GSCX.SHIPPER_ID, CCLCX.CON_CARRIER_ID, CCLCX.LOCATION_ID, NULL PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.SCHED_SYSTEM_CONF SSC, POLARIS.SCHEDULE_CONF SC, POLARIS.GROUP_SHIPPER_CONTACT_XREF GSCX, POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX\n" +
                                        "WHERE \n" +
                                        "GSCX.CONTACT_ID = C.ID\n" +
                                        "AND C.ID = CCLCX.CONTACT_ID(+)\n" +
                                        "AND SC.SCHED_SYSTEM_ID = SSC.ID\n" +
                                        "AND GSCX.SYSTEM_GROUP_ID = SSC.SYSTEM_GROUP_ID \n" +
                                        "UNION\n" +
                                        "SELECT DISTINCT PCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, SSC.SYSTEM_GROUP_ID, SC.SCHED_SYSTEM_ID, SCX.SHIPPER_ID, PCX.REC_CON_CARRIER_ID CON_CARRIER_ID, PCX.REC_LOCATION_ID LOCATION_ID, PCX.PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.SCHED_SYSTEM_CONF SSC, POLARIS.SCHEDULE_CONF SC, POLARIS.SCHEDULE_CONTACT_XREF SCX, POLARIS.GROUP_SHIPPER_CONTACT_XREF GSCX, POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX, POLARIS.PROVER_PHY_METER_CONTACT_XREF PCX\n" +
                                        "WHERE \n" +
                                        "C.ID = PCX.CONTACT_ID\n" +
                                        "AND PCX.CONTACT_ID = SCX.CONTACT_ID (+)\n" +
                                        "AND PCX.CONTACT_ID = CCLCX.CONTACT_ID(+)\n" +
                                        "AND SCX.SHIPPER_ID = GSCX.SHIPPER_ID (+)\n" +
                                        "AND SCX.SCHEDULE_ID = SC.ID (+)\n" +
                                        "AND SC.SCHED_SYSTEM_ID = SSC.ID (+)\n" +
                                        "UNION\n" +
                                        "SELECT DISTINCT PCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, SSC.SYSTEM_GROUP_ID, SC.SCHED_SYSTEM_ID, SCX.SHIPPER_ID, PCX.DEL_CON_CARRIER_ID CON_CARRIER_ID, PCX.DEL_LOCATION_ID LOCATION_ID, PCX.PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.SCHED_SYSTEM_CONF SSC, POLARIS.SCHEDULE_CONF SC, POLARIS.SCHEDULE_CONTACT_XREF SCX, POLARIS.GROUP_SHIPPER_CONTACT_XREF GSCX, POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX, POLARIS.PROVER_PHY_METER_CONTACT_XREF PCX\n" +
                                        "WHERE \n" +
                                        "C.ID = PCX.CONTACT_ID\n" +
                                        "AND PCX.CONTACT_ID = SCX.CONTACT_ID (+)\n" +
                                        "AND PCX.CONTACT_ID = CCLCX.CONTACT_ID(+)\n" +
                                        "AND SCX.SHIPPER_ID = GSCX.SHIPPER_ID (+)\n" +
                                        "AND SCX.SCHEDULE_ID = SC.ID (+)\n" +
                                        "AND SC.SCHED_SYSTEM_ID = SSC.ID (+) \n" +
                                        ") \n";

    public static final String schedSystemDBWhere0   = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT DISTINCT SCHED_SYSTEM_ID FROM\n" +
                                                      where0;

    public static final String systemGroupDBWhere0   = "ID IN  \n" +
                                                       "(\n" +
                                                       "SELECT DISTINCT SYSTEM_GROUP_ID FROM\n" +
                                                       where0;

    public static final String shipperDBWhere0     = "ID IN  \n" +
                                                   "(\n" +
                                                   "SELECT DISTINCT SHIPPER_ID FROM\n" +
                                                   where0;

    public static final String conCarrierDBWhere0     = "ID IN  \n" +
                                                    "(\n" +
                                                    "SELECT DISTINCT CON_CARRIER_ID FROM\n" +
                                                    where0;

    public static final String locationDBWhere0     = "ID IN  \n" +
                                                        "(\n" +
                                                        "SELECT DISTINCT LOCATION_ID FROM\n" +
                                                        where0;

    public static final String physicalMeterDBWhere0     = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT DISTINCT PHYSICAL_METER_ID FROM\n" +
                                                      where0;

    public static final String companyDBWhere0      = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT DISTINCT COMPANY_ID FROM\n" +
                                                      where0;

    public static final String contactDBWhere0      = "ID IN  \n" +
                                                             "(\n" +
                                                             "SELECT DISTINCT CONTACT_ID FROM\n" +
                                                             where0;

    public static final String systemGroupDBName    = "(SYSTEM_GROUP_ID";
    public static final String schedSystemDBName    = "(SCHED_SYSTEM_ID";
    public static final String locationDBName    = "(LOCATION_ID";
    public static final String conCarrierDBName    = "(CON_CARRIER_ID";
    public static final String shipperDBName    = "(SHIPPER_ID";
    public static final String physicalMeterDBName    = "(PHYSICAL_METER_ID";
    public static final String companyDBName    = "(COMPANY_ID";
    public static final String descriptionDBName    = "(DESCRIPTION";
}
