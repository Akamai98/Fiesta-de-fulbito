package armameeldoparti.utils.common.custom.graphical;

import javax.swing.JTextArea;

/**
 * Custom text area class.
 *
 * <p>This class is used to instantiate a custom text area that fits the overall program
 * aesthetics. It is supposed to be used along with a CustomScrollPane if a scrollbar is needed.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since 3.0
 */
public class CustomTextArea extends JTextArea {

  // ---------------------------------------- Constructors --------------------------------------

  /**
   * Builds a basic text area using the established program aesthetics.
   *
   * @param rows    Row count for the text area.
   * @param columns Column count for the text area.
   */
  public CustomTextArea(int rows, int columns) {
    super(rows, columns);
    setUpGraphicalProperties();
  }

  /**
   * Builds a basic empty text area using the established program aesthetics.
   */
  public CustomTextArea() {
    super();
    setUpGraphicalProperties();
  }

  // ---------------------------------------- Private methods -----------------------------------

  /**
   * Configures the graphical properties for the text area in order to fit the program aesthetics.
   */
  private void setUpGraphicalProperties() {
    setLineWrap(true);
    setWrapStyleWord(true);
    setEditable(false);
    setOpaque(false);
  }
}