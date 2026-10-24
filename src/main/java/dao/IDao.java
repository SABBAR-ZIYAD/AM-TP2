package dao;

public interface IDao {
    double getValue();

    /**
     * Méthode alternative correspondant au diagramme de classes UML (getData).
     * Redirige par défaut vers getValue().
     */
    default double getData() {
        return getValue();
    }
}
