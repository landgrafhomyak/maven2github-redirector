package ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.landgrafhomyak.maven2github_redirector.data.*;
import ru.landgrafhomyak.maven2github_redirector.data.Package;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class CachedSqliteJdbcStorage implements Storage {

    private static class CachedGroupHeader {
    }

    @NotNull
    @Override
    public PackagesIterator getAllPackages() throws StorageException {
        return null;
    }


    private final Connection connection;

    public CachedSqliteJdbcStorage(Connection connection) {
        this.connection = connection;
    }

    @Nullable
    @Override
    public Group.WithPackages findGroupWithPackages(@NotNull String... packageLevels) {
        return null;
    }

    @Nullable
    @Override
    public Package.WithVersions findPackageWithVersions(@NotNull String[] packageLevels, @NotNull String packageName) {
        return null;
    }

    @Nullable
    @Override
    public PackageVersion.WithCompatibilities findPackageVersionWithCompatibilities(@NotNull String[] packageLevels, @NotNull String packageName, @NotNull String version) {
        return null;
    }

    @NotNull
    @Override
    public String getPackageVersionDownloadLink(@NotNull String[] packageLevels, @NotNull String packageName, @NotNull String version) {
        return null;
    }


    static final class ReferenceBuffer<T> {
        @Nullable
        final ReferenceBuffer<T> nextBuffer;
        final int recursiveCapacity;
        @NotNull
        final T[] currentBuffer;

        private ReferenceBuffer(T[] currentBuffer, @NotNull final ReferenceBuffer<T> nextBuffer) {
            this.nextBuffer = nextBuffer;
            this.currentBuffer = currentBuffer;
            this.recursiveCapacity = currentBuffer.length + nextBuffer.recursiveCapacity;
        }

        private ReferenceBuffer(T[] currentBuffer) {
            this.nextBuffer = null;
            this.currentBuffer = currentBuffer;
            this.recursiveCapacity = currentBuffer.length;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private final static ReferenceBuffer EMPTY_BUFFER = new ReferenceBuffer(new Object[0]);

        @SuppressWarnings({"unchecked"})
        static <R> ReferenceBuffer<R> emptyBuffer() {
            return (ReferenceBuffer<R>) EMPTY_BUFFER;
        }

        static <R> Iterator<R> iterate(ReferenceBuffer<R> buffer) {
            return new PackageReferenceBufferIterator<>(buffer);
        }

        private static final class PackageReferenceBufferIterator<T> implements Iterator<T> {
            private ReferenceBuffer<T> currentContainer;
            private int pos;

            private PackageReferenceBufferIterator(@Nullable ReferenceBuffer<T> container) {
                this.currentContainer = container;
                this.pos = 0;
            }

            @Override
            public boolean hasNext() {
                while (this.currentContainer != null &&
                        this.pos < this.currentContainer.currentBuffer.length &&
                        this.currentContainer.currentBuffer[this.pos] != null)
                    this.currentContainer = this.currentContainer.nextBuffer;
                return this.currentContainer != null;
            }

            @Override
            public T next() {
                if (!this.hasNext())
                    throw new IllegalStateException();
                return this.currentContainer.currentBuffer[this.pos++];
            }
        }
    }

    static abstract class AbstractLazyLoader<T> implements StorageIterator<T> {
        private final ResultSet rSet;

        private T[] currentBuffer;

        private int currentPos;

        private ReferenceBuffer<T> lastPacked;

        private static final int hasNext_UNKNOWN = 0;
        private static final int hasNext_FALSE = -1;
        private static final int hasNext_TRUE = 1;
        private int hasNext;

        protected abstract T[] createArray(int size);

        protected abstract T parse(ResultSet row);

        AbstractLazyLoader(ResultSet rows) {
            this.rSet = rows;
            this.currentBuffer = null;
            this.lastPacked = null;
            this.currentPos = 0;
            this.hasNext = hasNext_UNKNOWN;
        }

        private void extend(T value) {
            if (this.currentBuffer != null) {
                if (this.currentPos >= this.currentBuffer.length) {
                    this.lastPacked = new ReferenceBuffer<>(this.currentBuffer, this.lastPacked);
                    this.currentBuffer = null;
                }
            }
            if (this.currentBuffer == null) {
                final int newSize;
                if (this.lastPacked != null) {
                    newSize = this.lastPacked.recursiveCapacity * 2 + 1;
                } else {
                    newSize = 7;
                }
                this.currentBuffer = this.createArray(newSize);
                this.currentPos = 0;
            }
            this.currentBuffer[this.currentPos] = value;
        }

        @Override
        public boolean hasNext() throws StorageException {
            switch (this.hasNext) {
                case hasNext_FALSE:
                    return false;
                case hasNext_TRUE:
                    return true;
                case hasNext_UNKNOWN:
                    break;
            }
            final T value;
            try {
                if (!this.rSet.next()) {
                    this.hasNext = hasNext_FALSE;
                    return false;
                }
                this.hasNext = hasNext_TRUE;
            } catch (SQLException e) {
                throw new StorageException("Unexpected underlying exception in fetching next row", e)
                return false;
            }
            value = this.parse(rSet);

            this.extend(value);

            return true;
        }

        @Override
        public T next() throws StorageException {
            if (!this.hasNext())
                throw new IllegalStateException("No such elements");
            return this.currentBuffer[this.currentPos++];
        }

        private void catchingCloseResultSet() throws StorageException {
            try {
                this.rSet.close();
            } catch (SQLException e) {
                throw new StorageException("Unexpected underlying exception while closing sql result set", e);
            }
        }

        ReferenceBuffer<T> closeAndGet() throws StorageException {
            if (this.hasNext())
                throw new IllegalStateException("Use .forceClose() to free all resources without finishing reading");
            if (this.currentBuffer != null) {
                this.lastPacked = new ReferenceBuffer<>(this.currentBuffer, this.lastPacked);
                this.currentBuffer = null;
            }
            this.catchingCloseResultSet();
            return this.lastPacked;
        }

        void forceClose() throws StorageException {
            this.catchingCloseResultSet();
        }
    }

    private static final class AllPackagesIteratorImpl implements Storage.PackagesIterator {

        @Override
        public void close() throws StorageException {

        }

        @Override
        public boolean hasNext() throws StorageException {
            return false;
        }

        @Override
        public Package next() throws StorageException {
            return null;
        }
    }

    private static final class CachedGroup implements Group.WithPackages {
        CachedSqliteJdbcStorage.CachedGroup next;

        String qualname;

        private CachedGroup(String qualname) {
            this.next = null;
            this.qualname = qualname;
        }

        @NotNull
        @Override
        public String getQualname() {
            return this.qualname;
        }

        @NotNull
        @Override
        public Iterable<Package> getPackages() {
            return null;
        }

        @Override
        public void close() throws Exception {

        }

    }

    static class CachedPackage implements Package.WithVersions {
    }
}
