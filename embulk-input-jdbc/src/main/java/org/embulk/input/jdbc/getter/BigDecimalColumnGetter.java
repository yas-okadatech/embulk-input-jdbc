package org.embulk.input.jdbc.getter;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.embulk.spi.Column;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;

public class BigDecimalColumnGetter
        extends AbstractColumnGetter
{
    private BigDecimal value;

    public BigDecimalColumnGetter(PageBuilder to)
    {
        super(to);
    }

    @Override
    protected void fetch(ResultSet from, int fromIndex) throws SQLException
    {
        value = from.getBigDecimal(fromIndex);
    }

    @Override
    public Type getToType()
    {
        return Types.DOUBLE;
    }

    @Override
    public void booleanColumn(Column column)
    {
        to.setBoolean(column, value.signum() > 0);
    }

    @Override
    public void longColumn(Column column)
    {
        to.setLong(column, value.longValue());
    }

    @Override
    public void doubleColumn(Column column)
    {
        // rounded value could be Double.NEGATIVE_INFINITY or Double.POSITIVE_INFINITY.
        double rounded = value.doubleValue();
        to.setDouble(column, rounded);
    }

    @Override
    public void stringColumn(Column column)
    {
        to.setString(column, value.toPlainString());
    }

}
