package polaris.administration.tables.companies;

/**
 * Container class for constants shared across modules related to Movements configuration.
 * <p>
 * Author: Joe Hunsaker
 */
class CompaniesConstants
{
    //query used to display all tank grade groups.
    public static final String stateDBWhere      = "ID IN (SELECT STATE_PROVINCE_ID FROM POLARIS.COMPANY_CONF ";

    public static final String nameDBName    = "(NAME";
    public static final String descriptionDBName    = "(DESCRIPTION";
    public static final String stateDBName    = "(STATE_PROVINCE_ID";
    public static final String stateTableName    = "STATE_PROVINCE_CONF";
}
