package ru.landgrafhomyak.maven2github_redirector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.landgrafhomyak.maven2github_redirector.data.PackagesMetainfoDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

class RedirectorHandler implements HttpHandler {
    private final int pathSizeLimit;
    PackagesMetainfoDatabase storage;

    public RedirectorHandler(int pathSizeLimit, PackagesMetainfoDatabase storage) {
        this.pathSizeLimit = pathSizeLimit;
        this.storage = storage;
    }

    private static class PathSplitter {
        private final char[] data;
        private int pos;

        public PathSplitter(char[] data) {
            this.data = data;
            this.pos = 0;
        }

        public boolean hasNext() {
            return this.pos < this.data.length;
        }

        public String next() {
            if (this.pos >= this.data.length)
                throw new IllegalStateException();
            while (this.pos < this.data.length && this.data[this.pos] == '/')
                this.pos++;
            if (this.pos >= this.data.length)
                return null;
            int start = this.pos;
            while (this.pos < this.data.length && this.data[this.pos] != '/')
                this.pos++;
            return new String(this.data, start, this.pos - start);
        }

        public String restPathWithLeadingSlash() {
            if (this.pos >= this.data.length)
                return "";
            String s = new String(this.data, this.pos, this.data.length - this.pos);
            this.pos = this.data.length;
            return s;
        }

        public String restPathWithoutLeadingSlash() {
            while (this.pos < this.data.length && this.data[this.pos] == '/')
                this.pos++;
            if (this.pos >= this.data.length)
                return "";
            String s = new String(this.data, this.pos, this.data.length - this.pos);
            this.pos = this.data.length;
            return s;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String rawFullPath = exchange.getRequestURI().getRawPath();
            if (rawFullPath.length() > this.pathSizeLimit) {
                exchange.sendResponseHeaders(414, 0);
                exchange.close();
                return;
            }
            StringBuilder group = new StringBuilder();
            String pkg;
            String version;
            PathSplitter parser = new PathSplitter(rawFullPath.toCharArray());

            initialValues:
            {
                if (parser.hasNext()) {
                    pkg = parser.next();
                    if (pkg != null && parser.hasNext()) {
                        version = parser.next();
                        if (version != null)
                            break initialValues;
                    }
                }
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
                return;
            }


            while (true) {
                final String newLevel;
                if (!parser.hasNext() || (newLevel = parser.next()) == null) {
                    exchange.sendResponseHeaders(404, 0);
                    exchange.close();
                    return;
                }
                group.append('.');
                group.append(pkg);
                pkg = version;
                version = newLevel;
                String downloadUrl = this.storage.getPackageVersionDownloadLink(group.toString(), pkg, version);
                if (downloadUrl == null)
                    continue;

                exchange.getResponseHeaders().add("Location", downloadUrl + parser.restPathWithoutLeadingSlash());
                exchange.sendResponseHeaders(307, 0);
                exchange.close();
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            ByteArrayOutputStream contentBuilder = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintWriter(contentBuilder, false, StandardCharsets.UTF_8));
            byte[] content = contentBuilder.toByteArray();
            exchange.sendResponseHeaders(501, content.length);
            exchange.getResponseBody().write(content);
            exchange.close();
        }
    }
}
