package projects.gabeross.fullstackdemo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;
import projects.gabeross.fullstackdemo.models.Person;
import projects.gabeross.fullstackdemo.repositories.PersonRepository;

@Route
public class MainView extends VerticalLayout {

    private final PersonRepository repo;

    private final PersonEditor editor;

    final Grid<Person> grid;

    final TextField filter;

    private final Button addNewBtn;

    public MainView(PersonRepository repo, PersonEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Person.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "name", "email");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listPerson(e.getValue()));

        // Connect selected Person to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editPerson(e.getValue());
        });

        // Instantiate and edit new Person the new button is clicked
        addNewBtn.addClickListener(e -> editor.editPerson(new Person("", "")));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPerson(filter.getValue());
        });

        // Initialize listing
        listPerson(null);
    }

    // tag::listPersons[]
    void listPerson(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByName(filterText));
        }
    }
}
