package polaris.administration.tables.tankGradeGroups;

import polaris.administration.tables.GenericTableColumnModel;

/**
 * Table Column Model Class for TANK_GRADE_GROUP_CONF. It
 * defines the columns to display
 *
 * @Author: TCI - Krista Riggs
 *
 * Created 1/9/17 TCI - Krista Riggs
 *
 */
public class TankGradeGroupsTableColumnModel extends GenericTableColumnModel
{
    public final static String[] names = {"EDIT_MODE", "NAME", "DESCRIPTION"};

    /**
     * Instantiate the Tank Grade Group Table Column Model
     */
    public TankGradeGroupsTableColumnModel()
    {
        super(names);
    }

}