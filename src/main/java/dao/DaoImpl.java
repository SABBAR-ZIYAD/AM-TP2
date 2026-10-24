package dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("dao")
@Profile("prod")  // Ce bean sera actif uniquement avec le profil "prod"
public class DaoImpl implements IDao {
    @Override
    public double getValue() {
        System.out.println("Version de production");
        return 100.0;
    }
}
