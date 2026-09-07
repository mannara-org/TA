package gui.nav;

import java.util.function.Supplier;

/**
 * A single row in a drill-down list.
 *
 * If {@code children()} returns null, the row is a leaf (e.g. a Student) and
 * activating it should trigger a "leaf selected" action instead of pushing a
 * new level. Otherwise the supplier is invoked lazily, only when the row is
 * actually activated, so we never fire a query for levels the professor
 * never opens.
 */
public interface NavRow {
    String label();
    Supplier<NavLevel> children();
}
