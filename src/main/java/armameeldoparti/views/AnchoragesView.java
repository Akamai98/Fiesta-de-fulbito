package armameeldoparti.views;

import armameeldoparti.controllers.AnchoragesController;
import armameeldoparti.models.Player;
import armameeldoparti.models.Position;
import armameeldoparti.models.ProgramView;
import armameeldoparti.utils.common.CommonFields;
import armameeldoparti.utils.common.CommonFunctions;
import armameeldoparti.utils.common.Constants;
import armameeldoparti.utils.common.custom.graphical.CustomBorderedLabel;
import armameeldoparti.utils.common.custom.graphical.CustomScrollPane;
import armameeldoparti.utils.common.custom.graphical.CustomTextArea;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import lombok.Getter;
import lombok.NonNull;
import net.miginfocom.swing.MigLayout;

/**
 * Anchorages view class.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since v3.0
 */
@Getter
public class AnchoragesView extends View {

  // ---------------------------------------- Private fields ------------------------------------

  private JButton finishButton;
  private JButton newAnchorageButton;
  private JButton deleteAnchorageButton;
  private JButton deleteLastAnchorageButton;
  private JButton clearAnchoragesButton;

  private JPanel leftPanel;
  private JPanel rightPanel;

  private JScrollPane scrollPane;

  private JTextArea textArea;

  private List<JButton> anchorageButtons;

  /**
   * Map that associates each checkboxes-list with its corresponding position.
   */
  private Map<Position, List<JCheckBox>> checkboxesMap;

  // ---------------------------------------- Constructor ---------------------------------------

  /**
   * Builds the anchorages view.
   */
  public AnchoragesView() {
    super("Anclaje de jugadores", Constants.MIG_LAYOUT_WRAP2);

    leftPanel = new JPanel(new MigLayout(Constants.MIG_LAYOUT_WRAP2));
    rightPanel = new JPanel(new MigLayout(Constants.MIG_LAYOUT_WRAP));

    textArea = new CustomTextArea(true);

    scrollPane = new CustomScrollPane(textArea);

    anchorageButtons = new ArrayList<>();

    checkboxesMap = new EnumMap<>(Position.class);

    for (Position position : Position.values()) {
      checkboxesMap.put(position, new ArrayList<>());
    }

    initializeInterface();
  }

  // ---------------------------------------- Protected methods ---------------------------------

  /**
   * Initializes the view and makes it visible.
   */
  @Override
  protected void initializeInterface() {
    CommonFields.getPlayersSets()
                .forEach((key, value) -> {
                  fillCheckboxesSet(value, checkboxesMap.get(key));
                  addCheckboxesSet(checkboxesMap.get(key),
                                   CommonFields.getPositionsMap()
                                               .get(key));
                });

    getMasterPanel().add(leftPanel, Constants.MIG_LAYOUT_WEST);
    getMasterPanel().add(rightPanel, Constants.MIG_LAYOUT_EAST);
    addButtons();
    add(getMasterPanel());
    setTitle(getFrameTitle());
    setResizable(false);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setIconImage(Constants.ICON
                          .getImage());
    pack();
  }

  /**
   * Adds the buttons to their corresponding panel.
   */
  @Override
  protected void addButtons() {
    finishButton = new JButton("Finalizar");
    finishButton.setEnabled(false);
    finishButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .finishButtonEvent(CommonFunctions.getComponentFromEvent(e))
    );

    newAnchorageButton = new JButton("Anclar");
    newAnchorageButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .newAnchorageButtonEvent(CommonFunctions.getComponentFromEvent(e))
    );

    deleteAnchorageButton = new JButton("Borrar un anclaje");
    deleteAnchorageButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .deleteAnchorageButtonEvent(CommonFunctions.getComponentFromEvent(e))
    );

    deleteLastAnchorageButton = new JButton("Borrar último anclaje");
    deleteLastAnchorageButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .deleteLastAnchorageButtonEvent()
    );

    clearAnchoragesButton = new JButton("Limpiar anclajes");
    clearAnchoragesButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .clearAnchoragesButtonEvent()
    );

    JButton backButton = new JButton("Atrás");
    backButton.addActionListener(e ->
        ((AnchoragesController) CommonFunctions.getController(ProgramView.ANCHORAGES))
        .backButtonEvent()
    );

    anchorageButtons.add(finishButton);
    anchorageButtons.add(newAnchorageButton);
    anchorageButtons.add(deleteAnchorageButton);
    anchorageButtons.add(deleteLastAnchorageButton);
    anchorageButtons.add(clearAnchoragesButton);

    leftPanel.add(
        finishButton,
        CommonFunctions.buildMigLayoutConstraints(Constants.MIG_LAYOUT_GROWX,
                                                  Constants.MIG_LAYOUT_SPAN)
    );
    leftPanel.add(
        backButton,
        CommonFunctions.buildMigLayoutConstraints(Constants.MIG_LAYOUT_GROWX,
                                                  Constants.MIG_LAYOUT_SPAN)
    );

    rightPanel.add(
        scrollPane,
        CommonFunctions.buildMigLayoutConstraints(
          Constants.MIG_LAYOUT_SPAN2,
          Constants.MIG_LAYOUT_PUSH,
          Constants.MIG_LAYOUT_GROW
        )
    );
    rightPanel.add(newAnchorageButton, Constants.MIG_LAYOUT_GROW);
    rightPanel.add(deleteAnchorageButton, Constants.MIG_LAYOUT_GROW);
    rightPanel.add(deleteLastAnchorageButton, Constants.MIG_LAYOUT_GROW);
    rightPanel.add(clearAnchoragesButton, Constants.MIG_LAYOUT_GROW);
  }

  // ---------------------------------------- Private methods -----------------------------------

  /**
   * Fills the checkboxes sets.
   *
   * @param playersSet Players sets from where to obtain the names.
   * @param cbSet      Check boxes set to fill.
   */
  private void fillCheckboxesSet(@NonNull List<Player> playersSet,
                                 @NonNull List<JCheckBox> cbSet) {
    playersSet.forEach(p -> cbSet.add(new JCheckBox(p.getName())));
  }

  /**
   * Adds the checkboxes to the view with a label that specifies the corresponding position.
   *
   * @param cbSet     Check boxes to add.
   * @param labelText Label text.
   */
  private void addCheckboxesSet(@NonNull List<JCheckBox> cbSet,
                                @NonNull String labelText) {
    leftPanel.add(new CustomBorderedLabel(labelText, SwingConstants.LEFT),
                  CommonFunctions.buildMigLayoutConstraints(Constants.MIG_LAYOUT_GROWX,
                                                            Constants.MIG_LAYOUT_SPAN));

    cbSet.forEach(cb -> leftPanel.add(
        cb,
        CommonFunctions.buildMigLayoutConstraints(
          Constants.MIG_LAYOUT_ALIGN_LEFT,
          Constants.MIG_LAYOUT_PUSHX
        )
      )
    );
  }
}