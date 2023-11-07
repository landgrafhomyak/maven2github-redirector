CREATE TABLE groups
(
    id     INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name   TEXT    NOT NULL,
    parent INTEGER,

    FOREIGN KEY (parent) REFERENCES groups (id),
    UNIQUE (name, parent)
);

CREATE TABLE packages
(
    id                    INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name                  TEXT    NOT NULL,
    location_group        INTEGER NOT NULL,
    short_description     TEXT    DEFAULT NULL,
    link_to_doc           TEXT    DEFAULT NULL,
    is_maintained         INTEGER DEFAULT TRUE,
    git_repo_link         TEXT    DEFAULT NULL,
    link_to_maven_central TEXT    DEFAULT NULL,

    FOREIGN KEY (location_group) REFERENCES groups (id),
    UNIQUE (name, location_group)
);

CREATE TABLE package_versions
(
    id                    INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    package               INTEGER NOT NULL,
    version               TEXT    NOT NULL,
    version_ordinal       INTEGER NOT NULL,
    download_prefix_link  TEXT    NOT NULL,
    release_notes         TEXT DEFAULT NULL,
    git_repo_commit_link  TEXT DEFAULT NULL,
    link_to_maven_central TEXT DEFAULT NULL,

    FOREIGN KEY (package) REFERENCES packages (id),
    UNIQUE (package, version),
    UNIQUE (package, version, version_ordinal)
);

CREATE TABLE backward_compatibilities
(
    newer_version      INTEGER NOT NULL,
    compatibility_with INTEGER NOT NULL,

    UNIQUE (newer_version, compatibility_with),
    FOREIGN KEY (newer_version) REFERENCES package_versions (id),
    FOREIGN KEY (compatibility_with) REFERENCES package_versions (id),
    CHECK ( newer_version != compatibility_with )
);

CREATE TABLE local_dependencies
(
    pkg        INTEGER NOT NULL,
    dependency INTEGER NOT NULL,

    UNIQUE (pkg, dependency),
    FOREIGN KEY (pkg) REFERENCES package_versions (id),
    FOREIGN KEY (dependency) REFERENCES package_versions (id),
    CHECK ( pkg != dependency )
);

CREATE TABLE db_info
(
    var_name TEXT UNIQUE NOT NULL,
    value    TEXT        NOT NULL
);

INSERT INTO db_info(var_name, value)
VALUES ('version', 'v0.1');

