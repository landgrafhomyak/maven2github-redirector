SELECT parent_groups.full_name        AS parent_group_name,
       groups.full_name               AS group_name,
       packages.name                  AS package_name,
       packages.short_description     AS package_short_description,
       packages.link_to_doc           AS link_to_package_doc,
       packages.is_maintained         AS is_package_maintained,
       packages.git_repo_link         AS link_to_package_git_repo,
       packages.link_to_maven_central AS link_to_package_on_maven_central
FROM groups
         LEFT JOIN groups parent_groups ON parent_groups.id = groups.parent
         LEFT JOIN packages ON groups.id = packages.location_group
-- GROUP BY group_name
-- ORDER BY group_name, package_name