package ru.landgrafhomyak.maven2github_redirector.data

public interface Package {
    public val name: String
    public val group: Group
    public val description: String?
    public val linkToDoc: String?
    public val isMaintained: Boolean
    public val gitRepoLink: String?
    public val mavenCentralLink: String?

    public fun findVersion(v: String): PackageVersion?

    public fun getAllVersionDescending(): Sequence<PackageVersion>
    public fun getAllVersionAscending(): Sequence<PackageVersion>
}