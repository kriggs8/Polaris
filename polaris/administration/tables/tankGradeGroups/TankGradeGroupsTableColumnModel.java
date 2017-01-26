package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.GenericTableColumnModel;

/**
 * Table Column Model Class for TANK_GRADE_GROUP_CONF. It
 * defines the columns to display
 *
 * @Author: Krista Riggs(TCI) @ 1/9/17
 */
public class TankGradeGroupsTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE", "NAME", "DESCRIPTION"};

    public TankGradeGroupsTableColumnModel()
    {
        super(names);
    }


}