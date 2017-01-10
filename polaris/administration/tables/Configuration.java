package polaris.administration.tables;

/**
 * Created by CE7 on 4/29/2016.
 * Updated by Waleed ElSaid on 01/10/2017 to add some DB, and table names for the Movements Filter Tab comboBoxes
 */
public class Configuration
{
    public static String defaultDateFormat     = "MM-DD-YYYY HH24:MI";
    public static String systemBRWhere         = "ID IN (SELECT SYSTEM_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String scheduleSystemBRWhere = "ID IN (SELECT SCHED_SYSTEM_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String batchRouteWhere      = "ID IN (SELECT ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String recLocationBRWhere   = "ID IN (SELECT REC_LOCATION_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String delLocationBRWhere   = "ID IN (SELECT DEL_LOCATION_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String recConCarrierBRWhere = "ID IN (SELECT REC_CON_CARRIER_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String delConCarrierBRWhere = "ID IN (SELECT DEL_CON_CARRIER_ID FROM POLARIS.BATCH_ROUTE_CONF ";
    public static String osAreaBRWhere        = "ID IN (SELECT OS_AREA_ID FROM POLARIS.BATCH_ROUTE_CONF ";


    public static String systemOSWhere        = "ID IN (SELECT SYSTEM_ID FROM POLARIS.OS_AREA_CONF ";

    public static String systemDBName            = "(SYSTEM_ID";
    public static String scheduleSystemDBName    = "(SCHED_SYSTEM_ID";

    public static String recLocationDBName       = "(REC_LOCATION_ID";
    public static String delLocationDBName       = "(DEL_LOCATION_ID";
    public static String recConCarrierDBName     = "(REC_CON_CARRIER_ID";
    public static String delConCarrierDBName      = "(DEL_CON_CARRIER_ID";
    public static String inactiveIndicatorDBName  = "(INACTIVE_INDICATOR_FLAG";
    public static String osAreaDBName             = "(OS_AREA_ID";
    public static String userDBName               = "(USER_ID";
    public static String userUpdateDateDBName     = "(USER_UPDATE_DATE";
    public static String descriptionDBName        = "(DESCRIPTION";
    public static String aliasDBName              = "(ALIAS";
    public static String nameDBName                  = "(NAME";
    public static String idDBName                    = "(ID";
    public static String defaultRecMeterDBName       = "(DEFAULT_REC_METER_ID";
    public static String defaultDelMeterDBName       = "(DEFAULT_DEL_METER_ID";
    public static String defaultRecManifoldDBName    = "(DEFAULT_REC_MANIFOLD_ID";
    public static String defaultDelManifoldDBName    = "(DEFAULT_DEL_MANIFOLD_ID";
    public static String systemGroupDBName           = "(SYSTEM_GROUP_ID";
    public static String linespaceDBName             = "(LINESPACE_ID";
    public static String gradeSpecDBName             = "(GRADE_SPEC_ID";
    public static String movementDBName              = "(ID";
    public static String defaultRecInvLocationDBName = "(DEFAULT_REC_INV_LOCATION_ID";
    public static String defaultDelInvLocationDBName = "(DEFAULT_DEL_INV_LOCATION_ID";
    public static String reportableVolDBName         = "(REPORTABLE_VOLUME_SYSTEM_ID";
    public static String revenueAllocMethodDBName    = "(REVENUE_ALLOC_METHOD_ID";
    public static String bookInvLocationDBName       = "(BOOK_INV_LOCATION_ID";
    public static String genericNameDBName           = "(GENERIC_NAME";

    public static String systemTableName         = "SYSTEM_CONF";
    public static String scheduleSystemTableName = "SCHED_SYSTEM_CONF";
    public static String batchRouteTableName     = "BATCH_ROUTE_CONF";
    public static String locationTableName       = "LOCATION_CONF";
    public static String meterTableName          = "METER_CONF";
    public static String manifoldTableName       = "MANIFOLD_CONF";
    public static String conCarrierTableName     = "CON_CARRIER_CONF";
    public static String osAreaTableName         = "OS_AREA_CONF";
    public static String userTableName           = "USER_CONF";
    public static String fileFormatTypeTableName = "USER_CONF";
    public static String systemGroupTableName    = "SYSTEM_GROUP_CONF";
    public static String lineSpaceTableName      = "LINESPACE_CONF";
    public static String gradeSpecTableName      = "GRADE_SPEC_CONF";
    public static String movementTableName       = "MOVEMENT_CONF";
    public static String revAllocMethodTableName = "REVENUE_ALLOC_METHOD_DESC";
    public static String bookInvLocationTableName = "BOOK_INV_LOCATION_CONF";

    public static String newKey  = new String("<New>");
}
