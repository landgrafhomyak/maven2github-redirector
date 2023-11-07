package ru.landgrafhomyak.maven2github_redirector.data

public interface Group {
    public val parentGroup: Group?
    public val name: String
    public fun findPackage(name: String): Package?

    public fun getPackages(): Sequence<Package>
    public fun getSubGroups(): Sequence<Group>
}