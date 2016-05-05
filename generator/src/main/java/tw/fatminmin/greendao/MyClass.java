package tw.fatminmin.greendao;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public final class MyClass {

    private MyClass() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static void main(String args[]) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Schema schema = new Schema(1, "tw.fatminmin.xposed.minminguard.orm");
        Entity appData = schema.addEntity("AppData");
        appData.addStringProperty("pkgName").primaryKey();
        appData.addStringProperty("adNetworks");
        appData.addIntProperty("blockNum");
        try {
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "../app/src/main/java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
