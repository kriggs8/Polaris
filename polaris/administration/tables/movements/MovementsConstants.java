package polaris.administration.tables.movements;

/** A class containing some constants used in the Movements Module
 * These constants are usually used to build combo boxes connecting to the DB
 * Creation Date: 1/10/2017
 * @Author: TCI- Waleed Elsaid
 */
public class MovementsConstants {
    public static String systemGroupWhereClause = "ID IN (SELECT SYSTEM_GROUP_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String lineSpaceWhereClause = "ID IN (SELECT LINESPACE_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String gradeSpecWhereClause = "ID IN (SELECT GRADE_SPEC_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String movementWhereClause = "ID IN (SELECT ID FROM POLARIS.MOVEMENT_CONF ";
    public static String recLocationWhereClause = "ID IN (SELECT REC_LOCATION_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String recConCarrierWhereClause = "ID IN (SELECT REC_CON_CARRIER_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String delLocationWhereClause = "ID IN (SELECT DEL_LOCATION_ID FROM POLARIS.MOVEMENT_CONF ";
    public static String delConCarrierWhereClause = "ID IN (SELECT DEL_CON_CARRIER_ID FROM POLARIS.MOVEMENT_CONF ";



}
