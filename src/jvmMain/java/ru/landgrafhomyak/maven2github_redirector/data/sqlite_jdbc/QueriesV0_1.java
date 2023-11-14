package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import static ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc.Utilities.loadSqlScript;

class QueriesV0_1 {
    final static String GET_PACKAGE_VERSION_DOWNLOAD_LINK = loadSqlScript("v0.1", "get_package_version_download_link.sql");
}
