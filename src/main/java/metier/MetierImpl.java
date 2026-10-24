package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {

    // Injection automatique de dépendance :
    // Avec les profils Spring ("dev" ou "prod"), Spring injecte l'unique implémentation active.
    @Autowired
    private IDao dao;

    // Constructeur par défaut (nécessaire pour la réflexion et Spring)
    public MetierImpl() {
    }

    // Constructeur avec paramètre (injection par constructeur)
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        return dao.getValue() * 2;
    }

    // Setter pour l'injection par setter (utilisé pour les tests unitaires et la configuration XML)
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    public IDao getDao() {
        return dao;
    }
}
