package ru.landgrafhomyak.maven2github_redirector.data

public interface StorageIterator<T> {
    @Throws(StorageException::class)
    public fun hasNext(): Boolean

    @Throws(StorageException::class)
    public fun next(): T
}