CREATE TABLE groups
(
    id        INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    full_name TEXT    NOT NULL,
    parent    INTEGER,

    FOREIGN KEY (parent) REFERENCES groups (id),
    UNIQUE (full_name, parent)
);

CREATE TABLE packages
(
    id                    INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name                  TEXT    NOT NULL,
    location_group        INTEGER NOT NULL,
    short_description     TEXT             DEFAULT NULL,
    link_to_doc           TEXT             DEFAULT NULL,
    is_maintained         INTEGER NOT NULL DEFAULT TRUE,
    git_repo_link         TEXT             DEFAULT NULL,
    link_to_maven_central TEXT             DEFAULT NULL,

    FOREIGN KEY (location_group) REFERENCES groups (id),
    UNIQUE (name, location_group)
);

CREATE TABLE package_versions
(
    id                        INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    package                   INTEGER NOT NULL,
    version                   TEXT    NOT NULL,
    publishing_unix_timestamp INTEGER NOT NULL,
    download_prefix_link      TEXT    NOT NULL,
    release_notes             TEXT             DEFAULT NULL,
    git_repo_commit_link      TEXT             DEFAULT NULL,
    link_to_maven_central     TEXT             DEFAULT NULL,
    forbidden_to_use          INTEGER NOT NULL DEFAULT FALSE,

    FOREIGN KEY (package) REFERENCES packages (id),
    UNIQUE (package, version)
);

CREATE TABLE backward_compatibilities
(
    version         INTEGER NOT NULL,
    compatible_with INTEGER NOT NULL,

    UNIQUE (version, compatible_with),
    FOREIGN KEY (version) REFERENCES package_versions (id),
    FOREIGN KEY (compatible_with) REFERENCES package_versions (id),
    CHECK ( version != compatible_with )
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

CREATE TABLE _db_compatible_versions
(
    version TEXT NOT NULL UNIQUE
);

INSERT INTO _db_compatible_versions(version)
VALUES ('v0.1');


