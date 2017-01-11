package polaris.administration.tables.movements;

/** A class containing some constants used in the Movements Module
 * These constants are usually used to build combo boxes connecting to the DB
 * @Author: Waleed Elsaid on 1/10/2017.
 */
public class MovementsContants {
    public static String SYSTEM_GROUP_WHERE_CLAUSE = "ID IN (SELECT SYSTEM_GROUP_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String LINE_SPACE_WHERE_CLAUSE = "ID IN (SELECT LINESPACE_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String GRADE_SPEC_WHERE_CLAUSE = "ID IN (SELECT GRADE_SPEC_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String MOVEMENT_WHERE_CLAUSE = "ID IN (SELECT ID FROM POLARIS.MOVEMENT_CONF ";
    public static String REC_LOCATION_WHERE_CLAUSE = "ID IN (SELECT REC_LOCATION_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String REC_CON_CARRIER_WHERE_CLAUSE = "ID IN (SELECT REC_CON_CARRIER_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String DEL_LOCATION_WHERE_CLAUSE = "ID IN (SELECT DEL_LOCATION_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String DEL_CON_CARRIER_WHERE_CLAUSE = "ID IN (SELECT DEL_CON_CARRIER_ID FROM POLARIS.MOVEMENT_CONF ";


}
