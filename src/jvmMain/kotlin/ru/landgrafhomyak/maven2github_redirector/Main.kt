package ru.landgrafhomyak.maven2github_redirector

import com.sun.net.httpserver.HttpServer
import ru.landgrafhomyak.maven2github_redirector.data.sqlite_jdbc.UncachedPackagesMetainfoSqliteDatabase
import java.net.InetSocketAddress
import java.sql.DriverManager

private const val PATH_MAX_LENGTH = 256

public fun main(argv: Array<String>) {
    val port: UShort?
    val path2db: String
    when (argv.size) {
        0 -> {
            println("Port and database are missed")
            return
        }

        1 -> {
            println("Database is missed")
            return
        }

        2 -> {
            port = argv[0].toUShortOrNull()
            if (port == null) {
                println("Invalid port")
                return
            }
            path2db = argv[1]
        }

        else -> {
            println("Unexpected arguments")
            return
        }
    }

    val dbConn = DriverManager.getConnection("jdbc:sqlite:${path2db}")
    val db = UncachedPackagesMetainfoSqliteDatabase(dbConn)
    val server = HttpServer.create(InetSocketAddress(port.toUInt().toInt()), 0)
    server.createContext("/", RedirectorHandler(PATH_MAX_LENGTH, db))
    server.start()
}