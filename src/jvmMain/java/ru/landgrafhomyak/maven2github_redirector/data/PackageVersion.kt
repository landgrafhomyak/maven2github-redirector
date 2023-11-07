package ru.landgrafhomyak.maven2github_redirector.data

public interface PackageVersion {
    public val version: String
    public val pkg: Package
    public val group: Group get() = this.pkg.group
    public val releaseNotes: String?
    public val downloadPrefixLink: String
    public val isMaintained: Boolean
    public val gitRepoCommitLink: String?
    public val mavenCentralLink: String?

    public fun isNewerThen(other: PackageVersion): Boolean
    public fun isOlderThen(other: PackageVersion): Boolean

    public fun compatibleWith(other: PackageVersion): Boolean

    public fun getLocalDependencies(): Sequence<PackageVersion>

    public fun getDeclaredCompatibleVersions(): Sequence<PackageVersion>
    public fun getAllCompatibleVersions(): Sequence<PackageVersion>
}
