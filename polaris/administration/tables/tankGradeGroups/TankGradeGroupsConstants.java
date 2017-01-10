package polaris.administration.tables.tankGradeGroups;

/**
 * Container class for constants shared across modules related to Movements configuration.
 * <p>
 * Author: John McDonough
 */
class TankGradeGroupsConstants
{

    public static final String where0 = "(\n" +
                                        "SELECT \n" +
                                        "UNIQUE BR.SCHED_SYSTEM_ID,BR.SYSTEM_ID,TTG.TANK_GRADE_GROUP_ID, C.INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        "POLARIS.BATCH_ROUTE_CONF BR,\n" +
                                        "POLARIS.BATCH_ROUTE_TANK_XREF BRT,\n" +
                                        "POLARIS.TANK_TANK_GRADE_GROUP_XREF TTG,\n" +
                                        "POLARIS.TANK_GRADE_GROUP_CONF C\n" +
                                        "WHERE \n" +
                                        "BR.ID = BRT.BATCH_ROUTE_ID\n" +
                                        "AND BRT.TANK_ID = TTG.TANK_ID \n" +
                                        "AND C.ID = TTG.TANK_GRADE_GROUP_ID \n" +
                                        ") \n";

    public static final String where1 = "(\n" +
                                        "SELECT \n" +
                                        "UNIQUE BR.SCHED_SYSTEM_ID,BR.SYSTEM_ID,TTG.TANK_GRADE_GROUP_ID,TGG.GRADE_ID,  C.INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        "POLARIS.BATCH_ROUTE_CONF BR,\n" +
                                        "POLARIS.BATCH_ROUTE_TANK_XREF BRT,\n" +
                                        "POLARIS.TANK_TANK_GRADE_GROUP_XREF TTG,\n" +
                                        "POLARIS.TANK_GRADE_GROUP_GRADE_XREF TGG,\n" +
                                        "POLARIS.TANK_GRADE_GROUP_CONF C\n" +
                                        "WHERE \n" +
                                        "BR.ID = BRT.BATCH_ROUTE_ID\n" +
                                        "AND BRT.TANK_ID = TTG.TANK_ID \n" +
                                        "AND TTG.TANK_GRADE_GROUP_ID = TGG.TANK_GRADE_GROUP_ID \n" +
                                        "AND C.ID = TGG.TANK_GRADE_GROUP_ID \n" +
                                        ") \n";
    public static final String where2 = "(\n" +
                                        "SELECT UNIQUE TANK_GRADE_GROUP_ID, GRADE_ID,C.INACTIVE_INDICATOR_FLAG\n" +
                                        "FROM \n" +
                                        "POLARIS.TANK_GRADE_GROUP_GRADE_XREF, \n" +
                                        "POLARIS.TANK_GRADE_GROUP_CONF C\n" +
                                        "WHERE \n" +
                                        "C.ID = TANK_GRADE_GROUP_ID \n" +
                                        ") \n";

    public static final String schedSystemDBWhere0   = "ID IN  \n" +
                                                      "(\n" +
                                                      "SELECT  SCHED_SYSTEM_ID FROM\n" +
                                                      where0;

    public static final String schedSystemDBWhere1   = "ID IN  \n" +
                                                       "(\n" +
                                                       "SELECT  SCHED_SYSTEM_ID FROM\n" +
                                                       where1;

    public static final String systemDBWhere0     = "ID IN  \n" +
                                                   "(\n" +
                                                   "SELECT  SYSTEM_ID FROM\n" +
                                                   where0;

    public static final String systemDBWhere1     = "ID IN  \n" +
                                                    "(\n" +
                                                    "SELECT  SYSTEM_ID FROM\n" +
                                                    where1;


    //get list of all grades
    public static final String gradeDBWhere0     = "ID IN\n" +
                                                  "(\n" +
                                                  "SELECT  GRADE_ID FROM\n" +
                                                   where2;

    //query used if sched system/system and tank grade group is populated.
    public static final String gradeDBWhere1     = "ID IN\n" +
                                                   "(\n" +
                                                   "SELECT  GRADE_ID FROM\n" +
                                                   where1;

    //query used if sched system or system is populated
    public static final String gradeDBWhere2     = "ID IN\n" +
                                                   "(\n" +
                                                   "SELECT  GRADE_ID FROM\n" +
                                                   where1;

    //query used to display all tank grade groups.
    public static final String tankGradeGroupDBWhere0      = "ID IN\n" +
                                                             "(\n" +
                                                             "SELECT  TANK_GRADE_GROUP_ID FROM\n" +
                                                             "(\n" +
                                                             "SELECT UNIQUE ID TANK_GRADE_GROUP_ID\n" +
                                                             "FROM \n" +
                                                             "POLARIS.TANK_GRADE_GROUP_CONF \n" +
                                                             ") \n";

    //query used if sched system/system and grade is populated.
    public static final String tankGradeGroupDBWhere1      = "ID IN\n" +
                                                             "(\n" +
                                                             "SELECT  TANK_GRADE_GROUP_ID FROM\n" +
                                                             where1;
    //query used if sched system or system is populated
    public static final String tankGradeGroupDBWhere2      = "ID IN  \n" +
                                                             "(\n" +
                                                             "SELECT  TANK_GRADE_GROUP_ID FROM\n" +
                                                             where0;

    //query used if the grade is only populated
    public static final String tankGradeGroupDBWhere3      = "ID IN\n" +
                                                             "(\n" +
                                                             "SELECT  TANK_GRADE_GROUP_ID FROM\n" +
                                                             where2;

    public static final String systemDBName            = "(SYSTEM_ID";
    public static final String schedSystemDBName    = "(SCHED_SYSTEM_ID";
    public static final String gradeDBName    = "(GRADE_ID";
    public static final String tankGradeGroupDBName    = "(TANK_GRADE_GROUP_ID";
}
