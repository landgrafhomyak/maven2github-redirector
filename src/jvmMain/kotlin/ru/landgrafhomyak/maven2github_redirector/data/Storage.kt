package ru.landgrafhomyak.maven2github_redirector.data

public interface Storage {
    public interface PackagesIterator : StorageIterator<Package>, AutoCloseable {
        @Throws(StorageException::class)
        override fun close()
    }

    @Throws(StorageException::class)
    public fun getAllPackages():PackagesIterator

    @Throws(StorageException::class)
    public fun findGroupWithPackages(vararg packageLevels: String): Group.WithPackages?

    @Throws(StorageException::class)
    public fun findPackageWithVersions(vararg packageLevels: String, packageName: String): Package.WithVersions?

    @Throws(StorageException::class)
    public fun findPackageVersionWithCompatibilities(vararg packageLevels: String, packageName: String, version: String): PackageVersion.WithCompatibilities?

    @Throws(StorageException::class)
    public fun getPackageVersionDownloadLink(vararg packageLevels: String, packageName: String, version: String): String
}