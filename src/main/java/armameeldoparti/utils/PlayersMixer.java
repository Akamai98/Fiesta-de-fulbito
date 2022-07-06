package armameeldoparti.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Clase correspondiente a los algoritmos de distribución de jugadores.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 04/07/2021
 */
public class PlayersMixer {

    // ---------------------------------------- Campos privados ----------------------------------

    private Random randomGenerator;

    // ---------------------------------------- Constructor --------------------------------------

    /**
     * Construye el objeto repartidor de jugadores.
     */
    public PlayersMixer() {
        randomGenerator = new Random();
    }

    // ---------------------------------------- Métodos públicos ---------------------------------

    /**
     * Reparte los jugadores de manera aleatoria en dos equipos.
     *
     * @param teams      Lista contenedora de equipos.
     * @param anchorages Si la mezcla debe tener en cuenta anclajes establecidos.
     *
     * @return Los equipos actualizados con los jugadores repartidos.
     */
    public List<List<Player>> randomMix(List<List<Player>> teams, boolean anchorages) {
        if (anchorages) {
            return Collections.emptyList(); // TODO
        } else {
            int index;

            List<Integer> alreadySetted = new ArrayList<>();

            for (Position position : Position.values()) {
                /*
                 * Se elige un número aleatorio entre 0 y 1 (+1)
                 * para asignarle como equipo a un conjunto de jugadores,
                 * y el resto tendrá asignado el equipo opuesto.
                 * Se recorre la mitad de los jugadores del conjunto de manera
                 * aleatoria y se les asigna a los jugadores escogidos
                 * como equipo el número aleatorio generado al principio.
                 * A medida que se van eligiendo jugadores, su índice en
                 * el arreglo se almacena para evitar reasignarle un equipo.
                 * Al resto de jugadores que quedaron sin elegir de manera
                 * aleatoria (aquellos con team == 0) del mismo grupo, se
                 * les asigna el número de equipo opuesto.
                 */

                int teamSubset1 = randomGenerator.nextInt(2) + 1;
                int teamSubset2 = (teamSubset1 == 1) ? 2 : 1;

                List<Player> set = Main.getPlayersSets()
                                       .get(position);

                for (int j = 0; j < (set.size() / 2); j++) {
                    do {
                        index = randomGenerator.nextInt(set.size());
                    } while (alreadySetted.contains(index));

                    alreadySetted.add(index);

                    set.get(index).setTeam(teamSubset1);

                    (teamSubset1 == 1 ? teams.get(0) : teams.get(1)).add(set.get(index));
                }

                set.stream()
                    .filter(p -> p.getTeam() == 0)
                    .forEach(p -> {
                        p.setTeam(teamSubset2);

                        (teamSubset2 == 1 ? teams.get(0) : teams.get(1)).add(p);
                    });

                alreadySetted.clear();
            }
        }

        return teams;
    }

    /**
     * Reparte los jugadores en dos equipos de la manera más equitativa posible
     * en base a los puntuaciones ingresados por el usuario.
     *
     * @param anchorages Si la mezcla por puntuaciones debe tener en cuenta anclajes establecidos.
     */
    public List<List<Player>> ratingsMix(List<List<Player>> teams, boolean anchorages) {
        if (anchorages) {
            return Collections.emptyList(); // TODO
        } else {
            // Se ordenan los jugadores de cada posición en base a su puntuación, de mayor a menor
            for (Position position : Position.values()) {
                int setSize = Main.getPlayersSets()
                                  .get(position)
                                  .size();

                Main.getPlayersSets()
                    .get(position)
                    .sort(Comparator.comparing(Player::getRating)
                                    .reversed());

                /*
                 * Se ordenan los equipos en base a la suma de los puntuaciones
                 * de sus jugadores hasta el momento, de menor a mayor.
                 */
                teams.sort(Comparator.comparingInt(t -> t.stream()
                                                         .mapToInt(Player::getRating)
                                                         .reduce(0, Math::addExact)));

                if (setSize == 2) {
                    // Al equipo con menor puntuación se le asigna el jugador de mayor puntuación
                    teams.get(0)
                         .add(Main.getPlayersSets()
                                  .get(position)
                                  .get(0));

                    // Al equipo con mayor puntuación se le asigna el jugador de menor puntuación
                    teams.get(1)
                         .add(Main.getPlayersSets()
                                  .get(position)
                                  .get(1));
                } else {
                    List<List<Player>> playersSubsets = new ArrayList<>();

                    List<Player> playersSubset1 = new ArrayList<>();
                    List<Player> playersSubset2 = new ArrayList<>();

                    /*
                     * Agregamos los jugadores de los extremos, desde afuera
                     * hacia adentro, alternando entre subconjuntos.
                     *
                     * ¡¡¡IMPORTANTE!!!
                     *
                     * Si la cantidad de jugadores a repartir no es 4, este reparto debería
                     * automatizarse con un bucle 'for' que altere entre subconjuntos mediante
                     * una operación de la forma:
                     *
                     * (i % 2 == 0 ? playersSubset1 : playersSubset2).add(...)
                     */
                    playersSubset1.add(Main.getPlayersSets()
                                           .get(position)
                                           .get(0));
                    playersSubset1.add(Main.getPlayersSets()
                                           .get(position)
                                           .get(setSize - 1));

                    playersSubset2.add(Main.getPlayersSets()
                                           .get(position)
                                           .get(1));
                    playersSubset2.add(Main.getPlayersSets()
                                           .get(position)
                                           .get(setSize - 2));

                    playersSubsets.add(playersSubset1);
                    playersSubsets.add(playersSubset2);

                    // Ordenamos los subconjuntos en base a sus puntuaciones, de mayor a menor
                    playersSubsets.sort(Comparator.comparingInt(set -> set.stream()
                                  .mapToInt(Player::getRating)
                                  .reduce(0, Math::addExact)));

                    // Al equipo con mayor puntuación se le asigna el conjunto de jugadores que sumen menor puntuación
                    teams.get(0)
                         .addAll(playersSubsets.get(1));

                    // Al equipo con menor puntuación se le asigna el conjunto de jugadores que sumen mayor puntuación
                    teams.get(1)
                         .addAll(playersSubsets.get(0));
                }
            }
        }

        return teams;
    }
}