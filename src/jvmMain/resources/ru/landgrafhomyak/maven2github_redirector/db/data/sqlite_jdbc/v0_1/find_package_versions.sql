SELECT name AS package_name, short_description, link_to_doc, is_maintained, git_repo_link, link_to_maven_central
FROM packages
         INNER JOIN groups on groups.id = packages.location_group
WHERE groups.full_name = ?