package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.stream.Collectors;

class Utilities {
    private Utilities() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    static String loadResource(final String name) {
        try (InputStream is = Utilities.class.getResourceAsStream(name);) {
            if (is == null) throw new FileNotFoundException(name);
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }


    private static final String SQL_SCRIPT_RESOURCES_PREFIX_PATH = "/ru/landgrafhomyak/maven2github_redirector/data/sqlite_jdbc/";

    @NotNull
    static String loadSqlScript(final String version, final String scriptFilename) {
        return loadResource(
                Utilities.SQL_SCRIPT_RESOURCES_PREFIX_PATH +
                        version.replace('.', '_') + "/" + scriptFilename
        );
    }

    final static String GET_DATA_VERSION_QUERY = loadResource(SQL_SCRIPT_RESOURCES_PREFIX_PATH + "get_data_version.sql");

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










