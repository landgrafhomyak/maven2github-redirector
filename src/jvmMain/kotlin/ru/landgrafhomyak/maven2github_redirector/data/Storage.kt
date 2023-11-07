package ru.landgrafhomyak.maven2github_redirector.data

public interface Storage {
    public fun findGroup(vararg packageLevels: String): Group.WithPackages?

    public fun findPackage(vararg packageLevels: String, packageName: String): Package.WithVersions?

    public fun findPackageVersion(vararg packageLevels: String, packageName: String, version: String): PackageVersion.WithCompatibilities?
}