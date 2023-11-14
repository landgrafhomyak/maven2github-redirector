package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import static ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc.Utilities.loadSqlScript;

class QueriesV0_1 {
    final String GET_ALL_GROUPS_AND_PACKAGES = loadSqlScript("v0.1", "get_all_groups_and_packages.sql");
    final String FIND_GROUP_PACKAGES = loadSqlScript("v0.1", "find_group_packages.sql");
}
