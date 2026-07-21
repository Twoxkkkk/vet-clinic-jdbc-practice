package infrastructure.config;

import infrastructure.config.utils.ResourceLoader;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//TODO:
//handle nulls from .env file

public class DbConfig {

    private final String postgresUser;
    private final String postgresPassword;
    private final String connectionString;
    private final String postgresDBName;

    private static final DbConfig INSTANCE = new DbConfig();
    public ResourceLoader scriptLoader;

    private DbConfig() {
        checkDriver();

        Dotenv dotenv = Dotenv.configure()
                .filename("postgres.env")
                .ignoreIfMissing()
                .load();

        postgresUser = dotenv.get("POSTGRES_USER");
        postgresPassword = dotenv.get("POSTGRES_PASSWORD");
        postgresDBName = dotenv.get("POSTGRES_DB");

        connectionString = String
                .format("jdbc:postgresql://vetclinic-db:5432/%s", postgresDBName);

        scriptLoader = new ResourceLoader("sql/");

        initialize();
    }

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(connectionString, postgresUser, postgresPassword);
        con.setAutoCommit(false);

        return con;
    }

    private void initialize() {

        try (Connection con = getConnection()) {
            try {
                String tablesInitScript = scriptLoader.load("db-init-ddl.sql", null);
                String dataInsertionScript = scriptLoader.load("db-init-dml.sql", null);

                try(Statement statement = con.createStatement()){
                    statement.execute(tablesInitScript);
                    statement.execute(dataInsertionScript);
                }

                con.commit();

            } catch (IOException e){
                System.out.println("Couldn't load file!");
            }

        } catch (SQLException e){
            throw new RuntimeException("Couldn't perform database initialization!", e);
        }
    }

    private static void checkDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load PostgreSQL Driver!", e);
        }
    }

    public static DbConfig getInstance() {
        return INSTANCE;
    }

}
