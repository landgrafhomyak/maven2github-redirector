package ru.landgrafhomyak.maven2github_redirector.data

public interface Group {
    // public val parentGroup: Group?
    // public val name: String
    public val qualname: String
    public interface WithPackages : Group, AutoCloseable {
        public val packages: Iterable<Package>
    }
}