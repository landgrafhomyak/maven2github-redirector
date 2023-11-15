SELECT DISTINCT package_versions.download_prefix_link
FROM package_versions
         INNER JOIN packages on packages.id = package_versions.package
         INNER JOIN groups on groups.id = packages.location_group
WHERE groups.full_name = ?
  AND packages.name = ?
  AND package_versions.version = ?