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
                                        "-- Main Query\n" +
                                        "SELECT DISTINCT ID CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, NULL SYSTEM_GROUP_ID, NULL SCHED_SYSTEM_ID, NULL  SHIPPER_ID, NULL CON_CARRIER_ID, NULL LOCATION_ID, NULL PHYSICAL_METER_ID, C.INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C\n" +
                                        "UNION \n" +
                                        "-- CON_CARRIER_LOC_CONTACT_XREF & Movement_conf - REC\n" +
                                        "SELECT DISTINCT C.ID CONTACT_ID, C.COMPANY_ID,C.DESCRIPTION, MV.SYSTEM_GROUP_ID,SSC.ID SCHED_SYSTEM_ID,\n" +
                                        "NULL SHIPPER_ID, CCLCX.CON_CARRIER_ID, CCLCX.LOCATION_ID, NULL PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, CCLCX.INACTIVE_INDICATOR_FLAG, MV.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        " POLARIS.MOVEMENT_CONF MV,\n" +
                                        "POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX,\n" +
                                        "  POLARIS.CONTACT_CONF C,\n" +
                                        "  POLARIS.SCHED_SYSTEM_CONF SSC\n" +
                                        "WHERE\n" +
                                        "MV.REC_LOCATION_ID = CCLCX.LOCATION_ID\n" +
                                        "AND MV.REC_CON_CARRIER_ID = CCLCX.CON_CARRIER_ID\n" +
                                        "AND CCLCX.CONTACT_ID = C.ID\n" +
                                        "AND SSC.SYSTEM_GROUP_ID = MV.SYSTEM_GROUP_ID\n" +
                                        "UNION\n" +
                                        "-- CON_CARRIER_LOC_CONTACT_XREF & Movement_conf - DEL\n" +
                                        "SELECT DISTINCT C.ID CONTACT_ID, C.COMPANY_ID,C.DESCRIPTION, MV.SYSTEM_GROUP_ID,SSC.ID SCHED_SYSTEM_ID,\n" +
                                        "NULL SHIPPER_ID, CCLCX.CON_CARRIER_ID, CCLCX.LOCATION_ID, NULL PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, CCLCX.INACTIVE_INDICATOR_FLAG, MV.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        " POLARIS.MOVEMENT_CONF MV,\n" +
                                        "POLARIS.CON_CARRIER_LOC_CONTACT_XREF CCLCX,\n" +
                                        "  POLARIS.CONTACT_CONF C,\n" +
                                        "  POLARIS.SCHED_SYSTEM_CONF SSC\n" +
                                        "WHERE\n" +
                                        "MV.DEL_LOCATION_ID = CCLCX.LOCATION_ID\n" +
                                        "AND MV.DEL_CON_CARRIER_ID = CCLCX.CON_CARRIER_ID\n" +
                                        "AND CCLCX.CONTACT_ID = C.ID\n" +
                                        "AND SSC.SYSTEM_GROUP_ID = MV.SYSTEM_GROUP_ID\n" +
                                        "UNION\n" +
                                        "-- GROUP_SHIPPER_CONTACT_XREF\n" +
                                        "SELECT DISTINCT C.ID CONTACT_ID, C.COMPANY_ID,C.DESCRIPTION, SC.SYSTEM_GROUP_ID,SC.ID SCHED_SYSTEM_ID,\n" +
                                        "SCX.SHIPPER_ID, NULL CON_CARRIER_ID, NULL LOCATION_ID, NULL PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, SCX.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        " POLARIS.SCHED_SYSTEM_CONF SC,\n" +
                                        "POLARIS.GROUP_SHIPPER_CONTACT_XREF SCX,\n" +
                                        "  POLARIS.CONTACT_CONF C\n" +
                                        "WHERE\n" +
                                        "SCX.SYSTEM_GROUP_ID = SC.SYSTEM_GROUP_ID\n" +
                                        "AND SCX.CONTACT_ID = C.ID\n" +
                                        "UNION\n" +
                                        "-- Prover Distribution (Physical Meters)\n" +
                                        "SELECT DISTINCT PCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, MV.SYSTEM_GROUP_ID, SSC.ID SCHED_SYSTEM_ID, \n" +
                                        "NULL SHIPPER_ID, PCX.REC_CON_CARRIER_ID CON_CARRIER_ID, PCX.REC_LOCATION_ID LOCATION_ID, PCX.PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, PCX.INACTIVE_INDICATOR_FLAG, MV.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.PROVER_PHY_METER_CONTACT_XREF PCX, MOVEMENT_CONF MV, POLARIS.SCHED_SYSTEM_CONF SSC\n" +
                                        "WHERE \n" +
                                        "C.ID = PCX.CONTACT_ID\n" +
                                        "AND PCX.REC_CON_CARRIER_ID = MV.REC_CON_CARRIER_ID (+)\n" +
                                        "AND PCX.REC_LOCATION_ID = MV.REC_LOCATION_ID (+)\n" +
                                        "AND MV.SYSTEM_GROUP_ID = SSC.SYSTEM_GROUP_ID (+)\n" +
                                        "UNION\n" +
                                        "SELECT DISTINCT PCX.CONTACT_ID, C.COMPANY_ID, C.DESCRIPTION, MV.SYSTEM_GROUP_ID, SSC.ID SCHED_SYSTEM_ID, \n" +
                                        "NULL SHIPPER_ID, PCX.DEL_CON_CARRIER_ID CON_CARRIER_ID, PCX.DEL_LOCATION_ID LOCATION_ID, PCX.PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, PCX.INACTIVE_INDICATOR_FLAG, MV.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG \n" +
                                        "FROM POLARIS.CONTACT_CONF C, POLARIS.PROVER_PHY_METER_CONTACT_XREF PCX, MOVEMENT_CONF MV, POLARIS.SCHED_SYSTEM_CONF SSC\n" +
                                        "WHERE \n" +
                                        "C.ID = PCX.CONTACT_ID\n" +
                                        "AND PCX.DEL_CON_CARRIER_ID = MV.DEL_CON_CARRIER_ID (+)\n" +
                                        "AND PCX.DEL_LOCATION_ID = MV.DEL_LOCATION_ID (+)\n" +
                                        "AND MV.SYSTEM_GROUP_ID = SSC.SYSTEM_GROUP_ID (+)\n" +
                                        "UNION\n" +
                                        "-- SCHEDULE_CONTACT_XREF\n" +
                                        "SELECT DISTINCT C.ID CONTACT_ID, C.COMPANY_ID,C.DESCRIPTION, SSC.SYSTEM_GROUP_ID, SC.SCHED_SYSTEM_ID,\n" +
                                        "X.SHIPPER_ID, X.CON_CARRIER_ID, X.LOCATION_ID, NULL PHYSICAL_METER_ID, GREATEST(C.INACTIVE_INDICATOR_FLAG, X.INACTIVE_INDICATOR_FLAG, SC.INACTIVE_INDICATOR_FLAG) INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        "  POLARIS.SCHEDULE_CONTACT_XREF X\n" +
                                        "  ,POLARIS.CONTACT_CONF C\n" +
                                        "  ,POLARIS.SCHEDULE_CONF SC\n" +
                                        "  ,POLARIS.SCHED_SYSTEM_CONF SSC\n" +
                                        "WHERE\n" +
                                        "X.CONTACT_ID = C.ID\n" +
                                        "AND X.SCHEDULE_ID = SC.ID\n" +
                                        "AND SC.SCHED_SYSTEM_ID = SSC.ID \n" +
                                        ") \n";

    public static final String schedSystemDBWhere0 = "ID IN  \n" +
                                                     "(\n" +
                                                     "SELECT DISTINCT SCHED_SYSTEM_ID FROM \n" +
                                                     where0;

    public static final String systemGroupDBWhere0 = "ID IN  \n" +
                                                     "(\n" +
                                                     "SELECT DISTINCT SYSTEM_GROUP_ID FROM \n" +
                                                     where0;

    public static final String shipperDBWhere0 = "ID IN  \n" +
                                                 "(\n" +
                                                 "SELECT DISTINCT SHIPPER_ID FROM \n" +
                                                 where0;

    public static final String conCarrierDBWhere0 = "ID IN  \n" +
                                                    "(\n" +
                                                    "SELECT DISTINCT CON_CARRIER_ID FROM \n" +
                                                    where0;

    public static final String locationDBWhere0 = "ID IN  \n" +
                                                  "(\n" +
                                                  "SELECT DISTINCT LOCATION_ID FROM \n" +
                                                  where0;

    public static final String physicalMeterDBWhere0 = "ID IN  \n" +
                                                       "(\n" +
                                                       "SELECT DISTINCT PHYSICAL_METER_ID FROM \n" +
                                                       where0;

    public static final String companyDBWhere0 = "ID IN  \n" +
                                                 "(\n" +
                                                 "SELECT DISTINCT COMPANY_ID FROM \n" +
                                                 where0;

    public static final String contactDBWhere0 = "ID IN  \n" +
                                                 "(\n" +
                                                 "SELECT DISTINCT CONTACT_ID FROM\n" +
                                                 where0;

    public static final String systemGroupDBName   = "(SYSTEM_GROUP_ID";
    public static final String schedSystemDBName   = "(SCHED_SYSTEM_ID";
    public static final String locationDBName      = "(LOCATION_ID";
    public static final String conCarrierDBName    = "(CON_CARRIER_ID";
    public static final String shipperDBName       = "(SHIPPER_ID";
    public static final String physicalMeterDBName = "(PHYSICAL_METER_ID";
    public static final String companyDBName       = "(COMPANY_ID";
    public static final String descriptionDBName   = "(DESCRIPTION";
}
