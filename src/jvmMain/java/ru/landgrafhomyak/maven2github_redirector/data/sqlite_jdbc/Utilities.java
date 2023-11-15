package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import org.jetbrains.annotations.NotNull;
import ru.landgrafhomyak.utility.JavaResourceLoader;



class Utilities {
    private Utilities() {
        throw new UnsupportedOperationException();
    }

    private static final String SQL_SCRIPT_RESOURCES_PREFIX_PATH = "/ru/landgrafhomyak/maven2github_redirector/data/sqlite_jdbc/";

    @NotNull
    static String loadSqlScript(final String version, final String scriptFilename) {
        return JavaResourceLoader.loadTextExitOnFail(Utilities.class,
                Utilities.SQL_SCRIPT_RESOURCES_PREFIX_PATH +
                        version.replace('.', '_') + "/" + scriptFilename
        );
    }

    final static String GET_DATA_VERSION_QUERY = JavaResourceLoader.loadTextExitOnFail(
            Utilities.class,
            SQL_SCRIPT_RESOURCES_PREFIX_PATH + "get_data_version.sql"
    );

    static String joinGroupLevels(String[] levels) {
        if (levels.length == 0)
            return "";
        int capacity = levels[0].length();
        for (int i = 1; i < levels.length; i++)
            capacity += levels[i].length() + 1;
        StringBuilder sb = new StringBuilder(capacity);
        sb.append(levels[0]);
        for (int i = 1; i < levels.length; i++) {
            sb.append('.');
            sb.append(levels[i]);
        }
        return sb.toString();
    }
}










