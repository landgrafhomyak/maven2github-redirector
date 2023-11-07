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
    public val forbiddenForUse: Boolean
    public val publicationTimestamp: ULong

    public interface WithCompatibilities : PackageVersion, AutoCloseable {
        public val compatibilities: Iterable<PackageVersion>
    }
}
