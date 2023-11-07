package ru.landgrafhomyak.maven2github_redirector.data


public interface Package {
    public val name: String
    public val group: Group
    public val description: String?
    public val linkToDoc: String?
    public val isMaintained: Boolean
    public val gitRepoLink: String?
    public val mavenCentralLink: String?

    public interface WithVersions : Package, AutoCloseable {
        public val versions: Iterable<PackageVersion>

        public val topVersions: Iterable<PackageVersion>
    }
}