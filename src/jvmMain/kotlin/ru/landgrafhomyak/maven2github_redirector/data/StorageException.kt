package ru.landgrafhomyak.maven2github_redirector.data

public class StorageException: Exception {
    public constructor(message:String, cause: Throwable) : super(message, cause)
    public constructor(message:String) : super(message)
}