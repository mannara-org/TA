package gui.nav;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Generic breadcrumb + drill-down list navigator.
 *
 * Holds a stack of {@link NavLevel}s. Double-clicking a row with children
 * pushes a new level onto the stack; clicking a breadcrumb segment pops back
 * to it directly (not just one step). A row with no children ({@code
 * children() == null}) is treated as a leaf and reported through {@link
 * #onLeafSelected}.
 */
public class DrillDownView extends VBox {

    private final Deque<NavLevel> path = new ArrayDeque<>();
    private final HBox breadcrumbBar = new HBox(6);
    private final ListView<NavRow> listView = new ListView<>();

    private Consumer<NavRow> onLeafSelected = row -> {};

    public DrillDownView(NavLevel root) {
        setSpacing(8);
        setPadding(new Insets(12));
        breadcrumbBar.setPadding(new Insets(0, 0, 4, 0));

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(NavRow row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty || row == null ? null : row.label());
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                activate(listView.getSelectionModel().getSelectedItem());
            }
        });

        VBox.setVgrow(listView, Priority.ALWAYS);
        getChildren().addAll(breadcrumbBar, listView);

        push(root);
    }

    /** Called when the professor double-clicks a leaf row (no children). */
    public void onLeafSelected(Consumer<NavRow> handler) {
        this.onLeafSelected = handler;
    }

    private void activate(NavRow row) {
        if (row == null) {
            return;
        }
        NavLevel next = row.children() == null ? null : row.children().get();
        if (next == null) {
            onLeafSelected.accept(row);
        } else {
            push(next);
        }
    }

    private void push(NavLevel level) {
        path.addLast(level);
        render();
    }

    private void popTo(int depth) {
        while (path.size() > depth) {
            path.removeLast();
        }
        render();
    }

    private void render() {
        listView.setItems(FXCollections.observableArrayList(path.peekLast().rows()));
        renderBreadcrumb();
    }

    private void renderBreadcrumb() {
        breadcrumbBar.getChildren().clear();
        int i = 0;
        int total = path.size();
        for (NavLevel level : path) {
            int depth = ++i;
            Label crumb = new Label(level.title());
            if (depth != total) {
                crumb.setUnderline(true);
                crumb.setOnMouseClicked(e -> popTo(depth));
                breadcrumbBar.getChildren().addAll(crumb, new Label("\u203A"));
            } else {
                breadcrumbBar.getChildren().add(crumb);
            }
        }
    }
}
