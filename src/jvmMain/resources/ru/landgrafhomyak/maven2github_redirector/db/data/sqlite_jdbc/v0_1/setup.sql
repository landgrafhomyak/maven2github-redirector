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
    id             INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name           TEXT    NOT NULL,
    location_group INTEGER NOT NULL,
--  short_description     TEXT             DEFAULT NULL,
--  link_to_doc           TEXT             DEFAULT NULL,
--  is_maintained         INTEGER NOT NULL DEFAULT TRUE,
--  git_repo_link         TEXT             DEFAULT NULL,
--  link_to_maven_central TEXT             DEFAULT NULL,

    FOREIGN KEY (location_group) REFERENCES groups (id),
    UNIQUE (name, location_group)
);

CREATE TABLE package_versions
(
    id                   INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    package              INTEGER NOT NULL,
    version              TEXT    NOT NULL,
--  publishing_unix_timestamp INTEGER NOT NULL,
    download_prefix_link TEXT    NOT NULL,
--  release_notes             TEXT             DEFAULT NULL,
--  git_repo_commit_link      TEXT             DEFAULT NULL,
--  link_to_maven_central     TEXT             DEFAULT NULL,
--  forbidden_to_use          INTEGER NOT NULL DEFAULT FALSE,

    FOREIGN KEY (package) REFERENCES packages (id),
    UNIQUE (package, version)
);

CREATE TABLE _db_read_compatible_versions
(
    version TEXT NOT NULL UNIQUE
);

INSERT INTO _db_read_compatible_versions(version)
VALUES ('v0.1');


