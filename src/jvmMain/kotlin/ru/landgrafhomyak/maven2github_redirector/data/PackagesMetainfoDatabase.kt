package ru.landgrafhomyak.maven2github_redirector.data

public interface PackagesMetainfoDatabase {
    @Throws(PackagesMetainfoDatabaseException::class)
    public fun getPackageVersionDownloadLink(vararg groupLevels: String, packageName: String, version: String): String?

    @Throws(PackagesMetainfoDatabaseException::class)
    public fun getPackageVersionDownloadLink(group: String, packageName: String, version: String): String?
}