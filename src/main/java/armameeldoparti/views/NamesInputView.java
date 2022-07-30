package armameeldoparti.views;

import armameeldoparti.abstracts.View;
import armameeldoparti.models.Player;
import armameeldoparti.models.Position;
import armameeldoparti.utils.Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;

/**
 * Clase correspondiente a la ventana de ingreso de nombres de jugadores
 * y de parámetros de distribución.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 28/02/2021
 */
public class NamesInputView extends View {

  // ---------------------------------------- Constantes privadas -------------------------------

  private static final int TEXT_AREA_ROWS = 14;
  private static final int TEXT_AREA_COLUMNS = 12;

  private static final String FRAME_TITLE = "Ingreso de jugadores";
  private static final String PDA_DATA_RETRIEVE_REGEX = "[CLMFG].+>.+";
  private static final String PDA_FILENAME = "dist.pda";

  private static final String[] OPTIONS_COMBOBOX = {
    "Defensores centrales",
    "Defensores laterales",
    "Mediocampistas",
    "Delanteros",
    "Arqueros"
  };

  // ---------------------------------------- Campos privados -----------------------------------

  private JButton mixButton;

  private JCheckBox anchoragesCheckBox;

  private JComboBox<String> comboBox;

  private JPanel leftPanel;
  private JPanel rightPanel;

  private JTextArea textArea;

  private Map<Position, List<JTextField>> textFieldsMap;

  // ---------------------------------------- Constructor ---------------------------------------

  /**
   * Construye una ventana de ingreso de jugadores.
   */
  public NamesInputView() {
    getPlayersDistributionData();
    initializeInterface();
  }

  // ---------------------------------------- Métodos públicos ----------------------------------

  // ---------------------------------------- Getters -------------------------------------------

  /**
   * Obtiene el botón de selección de distribución.
   *
   * @return El botón de selección de distribución.
   */
  public JButton getMixButton() {
    return mixButton;
  }

  /**
   * Obtiene la casilla de selección de anclajes.
   *
   * @return La casilla de selección de anclajes.
   */
  public JCheckBox getAnchoragesCheckBox() {
    return anchoragesCheckBox;
  }

  /**
   * Obtiene la lista desplegable.
   *
   * @return La lista desplegable.
   */
  public JComboBox<String> getComboBox() {
    return comboBox;
  }

  /**
   * Obtiene el área de texto.
   *
   * @return El área de texto.
   */
  public JTextArea getTextArea() {
    return textArea;
  }

  /**
   * Obtiene el panel izquierdo de la ventana.
   *
   * @return El panel izquierdo de la ventana.
   */
  public JPanel getLeftPanel() {
    return leftPanel;
  }

  /**
   * Obtiene las opciones de la lista desplegable.
   *
   * @return Las opciones de la lista desplegable.
   */
  public String[] getComboBoxOptions() {
    return OPTIONS_COMBOBOX;
  }

  /**
   * Obtiene el mapa que asocia cada conjunto de campos de texto
   * con la posición correspondiente de los jugadores a los que
   * representa.
   *
   * @return El mapa que asocia cada conjunto de campos de texto
   *         con la posición correspondiente de los jugadores a
   *         los que representa.
   */
  public Map<Position, List<JTextField>> getTextFieldsMap() {
    return textFieldsMap;
  }

  // ---------------------------------------- Métodos protegidos --------------------------------

  /**
   * Inicializa y muestra la interfaz gráfica de la ventana.
   */
  @Override
  protected void initializeInterface() {
    List<Player> centralDefenders = new ArrayList<>();

    initializeSet(centralDefenders, Position.CENTRAL_DEFENDER,
                  Main.getPlayersAmountMap()
                      .get(Position.CENTRAL_DEFENDER) * 2);

    List<Player> lateralDefenders = new ArrayList<>();

    initializeSet(lateralDefenders, Position.LATERAL_DEFENDER,
                  Main.getPlayersAmountMap()
                      .get(Position.LATERAL_DEFENDER) * 2);

    List<Player> midfielders = new ArrayList<>();

    initializeSet(midfielders, Position.MIDFIELDER,
                  Main.getPlayersAmountMap()
                      .get(Position.MIDFIELDER) * 2);

    List<Player> forwards = new ArrayList<>();

    initializeSet(forwards, Position.FORWARD,
                  Main.getPlayersAmountMap()
                      .get(Position.FORWARD) * 2);

    List<Player> goalkeepers = new ArrayList<>();

    initializeSet(goalkeepers, Position.GOALKEEPER,
                  Main.getPlayersAmountMap()
                      .get(Position.GOALKEEPER) * 2);

    Main.getPlayersSets()
        .put(Position.CENTRAL_DEFENDER, centralDefenders);

    Main.getPlayersSets()
        .put(Position.LATERAL_DEFENDER, lateralDefenders);

    Main.getPlayersSets()
        .put(Position.MIDFIELDER, midfielders);

    Main.getPlayersSets()
        .put(Position.FORWARD, forwards);

    Main.getPlayersSets()
        .put(Position.GOALKEEPER, goalkeepers);

    List<JTextField> centralDefendersTextFields = new ArrayList<>();
    List<JTextField> lateralDefendersTextFields = new ArrayList<>();
    List<JTextField> midfieldersTextFields = new ArrayList<>();
    List<JTextField> forwardsTextFields = new ArrayList<>();
    List<JTextField> goalkeepersTextFields = new ArrayList<>();

    textFieldsMap = new EnumMap<>(Position.class);

    textFieldsMap.put(Position.CENTRAL_DEFENDER, centralDefendersTextFields);
    textFieldsMap.put(Position.LATERAL_DEFENDER, lateralDefendersTextFields);
    textFieldsMap.put(Position.MIDFIELDER, midfieldersTextFields);
    textFieldsMap.put(Position.FORWARD, forwardsTextFields);
    textFieldsMap.put(Position.GOALKEEPER, goalkeepersTextFields);

    leftPanel = new JPanel(new MigLayout("wrap"));
    rightPanel = new JPanel(new MigLayout("wrap"));

    JPanel masterPanel = new JPanel(new MigLayout("wrap 2"));

    masterPanel.add(leftPanel, "west");
    masterPanel.add(rightPanel, "east");

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle(FRAME_TITLE);
    setIconImage(Main.ICON.getImage());
    setResizable(false);
    addComboBox();
    addTextFields(Position.CENTRAL_DEFENDER, centralDefenders);
    addTextFields(Position.LATERAL_DEFENDER, lateralDefenders);
    addTextFields(Position.MIDFIELDER, midfielders);
    addTextFields(Position.FORWARD, forwards);
    addTextFields(Position.GOALKEEPER, goalkeepers);
    addTextArea();
    addButtons();
    addanchoragesCheckBox();
    add(masterPanel);
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Coloca los botones en los paneles de la ventana.
   */
  @Override
  protected void addButtons() {
    mixButton = new JButton("Distribuir");

    JButton backButton = new JButton("Atrás");

    mixButton.setEnabled(false);

    mixButton.addActionListener(e ->
        Main.getNamesInputController()
            .mixButtonEvent()
    );

    backButton.addActionListener(e ->
        Main.getNamesInputController()
            .backButtonEvent()
    );

    rightPanel.add(mixButton, "grow");
    rightPanel.add(backButton, "grow");
  }

  // ---------------------------------------- Métodos privados ----------------------------------

  /**
   * Llena el conjunto de jugadores recibido con jugadores sin nombre ni puntuación,
   * y con la posición especificada.
   *
   * @param set      Arreglo de jugadores a inicializar.
   * @param position Posición de los jugadores del arreglo.
   * @param capacity Capacidad del arreglo.
   */
  private void initializeSet(List<Player> set, Position position, int capacity) {
    for (int i = 0; i < capacity; i++) {
      set.add(new Player("", position));
    }
  }

  /**
   * Agrega la lista desplegable y le establece el oyente de eventos.
   */
  private void addComboBox() {
    comboBox = new JComboBox<>(OPTIONS_COMBOBOX);

    comboBox.setSelectedIndex(0);
    comboBox.addActionListener(e ->
        Main.getNamesInputController()
            .comboBoxEvent((String) ((JComboBox<?>) e.getSource()).getSelectedItem())
    );

    leftPanel.add(comboBox, "growx");
  }

  /**
   * Añade al panel de la ventan el campo de texto de sólo lectura donde se
   * mostrarán en tiempo real los nombres de jugadores ingresados por el usuario.
   */
  private void addTextArea() {
    textArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLUMNS);

    textArea.setBorder(BorderFactory.createBevelBorder(1));
    textArea.setEditable(false);

    rightPanel.add(textArea, "push, grow, span");
  }

  /**
   * Agrega la casilla de anclaje de jugadores.
   */
  private void addanchoragesCheckBox() {
    anchoragesCheckBox = new JCheckBox("Anclar jugadores", false);

    anchoragesCheckBox.addActionListener(e -> Main.setAnchorages(!Main.thereAreAnchorages()));

    rightPanel.add(anchoragesCheckBox, "center");
  }

  /**
   * Crea, almacena y configura los campos de texto correspondientes a cada posición.
   *
   * @param position     Posición de los jugadores.
   * @param playersSet   Arreglo de jugadores con dicha posición.
   */
  private void addTextFields(Position position, List<Player> playersSet) {
    for (int i = 0; i < Main.getPlayersAmountMap()
                            .get(position) * 2; i++) {
      JTextField tf = new JTextField();

      tf.addActionListener(e ->
          Main.getNamesInputController()
              .textFieldEvent(textFieldsMap.get(position), playersSet,
                              tf, (JTextField) e.getSource())
      );

      textFieldsMap.get(position)
                   .add(tf);
    }
  }

  /**
   * Obtiene la cantidad de jugadores para cada posición por equipo
   * mediante expresiones regulares.
   *
   * <p>[CLMFG].+>.+ : Obtiene las líneas que comiencen con C, L, M, F, ó W,
   * seguido por al menos un caracter '>' (busca las líneas que nos importan en
   * el archivo .pda).
   *
   * <p>(?!(?<=X)\\d). : Obtiene el trozo de la línea que no sea un número que nos
   * interesa (el número que nos interesa ocuparía el lugar de la X).
   *
   * <p>Si el archivo .pda es modificado en cuanto a orden de las líneas importantes,
   * se debe tener en cuenta que Position.values()[index] confía en que lo hallado
   * se corresponde con el orden en el que están declarados los valores en el enum
   * Position. Idem, si se cambian de orden los valores del enum Position, se
   * deberá tener en cuenta que Position.values()[index] confía en el orden en el
   * que se leerán los datos del archivo .pda y, por consiguiente, se deberá rever
   * el orden de las líneas importantes de dichos archivos.
   */
  private void getPlayersDistributionData() {
    BufferedReader buff = new BufferedReader(
        new InputStreamReader(getClass().getClassLoader()
                                        .getResourceAsStream(Main.DOCS_PATH + PDA_FILENAME))
    );

    var wrapperIndex = new Object() {
      private int index;
    };

    buff.lines()
        .forEach(l -> {
          if (l.matches(PDA_DATA_RETRIEVE_REGEX)) {
            Main.getPlayersAmountMap()
                .put(Position.values()[wrapperIndex.index],
                    Integer.parseInt(l.replaceAll("(?!(?<="
                                                  + Main.PLAYERS_PER_TEAM
                                                  + ")\\d).", "")));

            wrapperIndex.index++;
          }
        });
  }
}