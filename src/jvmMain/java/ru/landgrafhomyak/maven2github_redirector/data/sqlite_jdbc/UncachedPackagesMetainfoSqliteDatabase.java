package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import kotlin.collections.ArraysKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.landgrafhomyak.maven2github_redirector.data.PackagesMetainfoDatabase;
import ru.landgrafhomyak.maven2github_redirector.data.PackagesMetainfoDatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UncachedPackagesMetainfoSqliteDatabase implements PackagesMetainfoDatabase {
    private final Connection connection;

    public UncachedPackagesMetainfoSqliteDatabase(Connection connection) {
        this.connection = connection;
    }


    @Nullable
    @Override
    public String getPackageVersionDownloadLink(@NotNull String[] groupLevels, @NotNull String packageName, @NotNull String version) throws PackagesMetainfoDatabaseException {
        return this.getPackageVersionDownloadLink(Utilities.joinGroupLevels(groupLevels), packageName, version);
    }

    @Nullable
    @Override
    public String getPackageVersionDownloadLink(@NotNull String group, @NotNull String packageName, @NotNull String version) throws PackagesMetainfoDatabaseException {
        try {
            ResultSet rs;
            try (PreparedStatement stmt = this.connection.prepareStatement(QueriesV0_1.GET_PACKAGE_VERSION_DOWNLOAD_LINK)) {
                stmt.setString(1, group);
                stmt.setString(2, packageName);
                stmt.setString(3, version);
                rs = stmt.executeQuery();
            }
            try (rs) {
                if (!rs.next())
                    return null;
                return rs.getString(1);
            }
        } catch (SQLException ex) {
            throw new PackagesMetainfoDatabaseException("Unexpected SQL exception", ex);
        }
    }
}






















