package ru.landgrafhomyak.maven2github_redirector.data

public interface DatabaseInterface {
    public fun findRootGroup(name: String): Group?

    public fun findGroup(vararg levels: String): Group?
}