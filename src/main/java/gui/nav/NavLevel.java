package gui.nav;

import java.util.List;
import java.util.function.Supplier;

/**
 * One screen's worth of rows, plus the title it should show in the
 * breadcrumb bar while it's active.
 */
public record NavLevel(String title, List<NavRow> rows) {

    /** Convenience factory so callers don't need an anonymous NavRow. */
    public static NavRow row(String label, Supplier<NavLevel> children) {
        return new SimpleNavRow(label, children);
    }

    private record SimpleNavRow(String label, Supplier<NavLevel> children) implements NavRow {}
}
