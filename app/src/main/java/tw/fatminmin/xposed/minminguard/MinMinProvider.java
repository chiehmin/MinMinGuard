package tw.fatminmin.xposed.minminguard;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import tw.fatminmin.xposed.minminguard.orm.AppData;
import tw.fatminmin.xposed.minminguard.orm.AppDataDao;
import tw.fatminmin.xposed.minminguard.orm.DaoMaster;
import tw.fatminmin.xposed.minminguard.orm.DaoSession;

/**
 * Created by fatminmin on 2015/10/24.
 */
public class MinMinProvider extends ContentProvider {

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AppDataDao appDataDao;

    @Override
    public boolean onCreate() {

        helper = new DaoMaster.DevOpenHelper(getContext(), "mmg", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        appDataDao = daoSession.getAppDataDao();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String pkgName = values.getAsString(Common.KEY_PKG_NAME);

        String network = values.getAsString(Common.KEY_NETWORK);
        Integer blockNum = values.getAsInteger(Common.KEY_BLOCK_NUM);

        List<AppData> list = appDataDao.queryBuilder()
                                .where(AppDataDao.Properties.PkgName.eq(pkgName))
                                .list();
        AppData appData;
        if (list.size() == 1) {
            appData = list.get(0);
        }
        else {
            appData = new AppData(pkgName, "", 0);
        }
        if (blockNum != null) {
            appData.setBlockNum(appData.getBlockNum() + blockNum);
        }
        if (network != null && !appData.getAdNetworks().contains(network)) {
            String cur = appData.getAdNetworks();
            if (cur.length() > 0) {
                cur += " ";
            }
            cur += network;
            appData.setAdNetworks(cur);
        }

        if (list.size() == 1) {
            appDataDao.update(appData);
        }
        else {
            appDataDao.insert(appData);
        }

        return 1;
    }
}
