package com.dachen.dgroupdoctorcompany.db.dbUtils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.IOUtils;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class TableUtils {
    private static Logger logger = LoggerFactory.getLogger(TableUtils.class);
    private static final FieldType[] noFieldTypes = new FieldType[0];

    private TableUtils() {
    }

    public static <T> int createTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        return createTable(connectionSource, dataClass, false);
    }

    public static <T> int createTableIfNotExists(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        return createTable(connectionSource, dataClass, true);
    }

    public static <T> int createTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return createTable(connectionSource, tableConfig, false);
    }

    public static <T> int createTableIfNotExists(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return createTable(connectionSource, tableConfig, true);
    }

    public static <T, ID> List<String> getCreateTableStatements(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        Dao dao = DaoManager.createDao(connectionSource, dataClass);
        if(dao instanceof BaseDaoImpl) {
            return addCreateTableStatements(connectionSource, ((BaseDaoImpl)dao).getTableInfo(), false);
        } else {
            TableInfo tableInfo = new TableInfo(connectionSource, (BaseDaoImpl)null, dataClass);
            return addCreateTableStatements(connectionSource, tableInfo, false);
        }
    }

    public static <T, ID> List<String> getCreateTableStatements(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        Dao dao = DaoManager.createDao(connectionSource, tableConfig);
        if(dao instanceof BaseDaoImpl) {
            return addCreateTableStatements(connectionSource, ((BaseDaoImpl)dao).getTableInfo(), false);
        } else {
            tableConfig.extractFieldTypes(connectionSource);
            TableInfo tableInfo = new TableInfo(connectionSource.getDatabaseType(), (BaseDaoImpl)null, tableConfig);
            return addCreateTableStatements(connectionSource, tableInfo, false);
        }
    }

    public static <T, ID> int dropTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ignoreErrors) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        Dao dao = DaoManager.createDao(connectionSource, dataClass);
        if(dao instanceof BaseDaoImpl) {
            return doDropTable(databaseType, connectionSource, ((BaseDaoImpl)dao).getTableInfo(), ignoreErrors);
        } else {
            TableInfo tableInfo = new TableInfo(connectionSource, (BaseDaoImpl)null, dataClass);
            return doDropTable(databaseType, connectionSource, tableInfo, ignoreErrors);
        }
    }

    public static <T, ID> int dropTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig, boolean ignoreErrors) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        Dao dao = DaoManager.createDao(connectionSource, tableConfig);
        if(dao instanceof BaseDaoImpl) {
            return doDropTable(databaseType, connectionSource, ((BaseDaoImpl)dao).getTableInfo(), ignoreErrors);
        } else {
            tableConfig.extractFieldTypes(connectionSource);
            TableInfo tableInfo = new TableInfo(databaseType, (BaseDaoImpl)null, tableConfig);
            return doDropTable(databaseType, connectionSource, tableInfo, ignoreErrors);
        }
    }

    public static <T> int clearTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        String tableName = DatabaseTableConfig.extractTableName(dataClass);
        if(connectionSource.getDatabaseType().isEntityNamesMustBeUpCase()) {
            tableName = tableName.toUpperCase();
        }

        return clearTable(connectionSource, tableName);
    }

    public static <T> int clearTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return clearTable(connectionSource, tableConfig.getTableName());
    }

    private static <T, ID> int createTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ifNotExists) throws SQLException {
        Dao dao = DaoManager.createDao(connectionSource, dataClass);
        if(dao instanceof BaseDaoImpl) {
            return doCreateTable(connectionSource, ((BaseDaoImpl)dao).getTableInfo(), ifNotExists);
        } else {
            TableInfo tableInfo = new TableInfo(connectionSource, (BaseDaoImpl)null, dataClass);
            return doCreateTable(connectionSource, tableInfo, ifNotExists);
        }
    }

    private static <T, ID> int createTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig, boolean ifNotExists) throws SQLException {
        Dao dao = DaoManager.createDao(connectionSource, tableConfig);
        if(dao instanceof BaseDaoImpl) {
            return doCreateTable(connectionSource, ((BaseDaoImpl)dao).getTableInfo(), ifNotExists);
        } else {
            tableConfig.extractFieldTypes(connectionSource);
            TableInfo tableInfo = new TableInfo(connectionSource.getDatabaseType(), (BaseDaoImpl)null, tableConfig);
            return doCreateTable(connectionSource, tableInfo, ifNotExists);
        }
    }

    private static <T> int clearTable(ConnectionSource connectionSource, String tableName) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        StringBuilder sb = new StringBuilder(48);
        if(databaseType.isTruncateSupported()) {
            sb.append("TRUNCATE TABLE ");
        } else {
            sb.append("DELETE FROM ");
        }

        databaseType.appendEscapedEntityName(sb, tableName);
        String statement = sb.toString();
        logger.info("clearing table \'{}\' with \'{}", tableName, statement);
        CompiledStatement compiledStmt = null;
        DatabaseConnection connection = connectionSource.getReadWriteConnection();

        int var7;
        try {
            compiledStmt = connection.compileStatement(statement, StatementType.EXECUTE, noFieldTypes, -1);
            var7 = compiledStmt.runExecute();
        } finally {
            IOUtils.closeThrowSqlException(compiledStmt, "compiled statement");
            connectionSource.releaseConnection(connection);
        }

        return var7;
    }

    private static <T, ID> int doDropTable(DatabaseType databaseType, ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ignoreErrors) throws SQLException {
        logger.info("dropping table \'{}\'", tableInfo.getTableName());
        ArrayList statements = new ArrayList();
        addDropIndexStatements(databaseType, tableInfo, statements);
        addDropTableStatements(databaseType, tableInfo, statements);
        DatabaseConnection connection = connectionSource.getReadWriteConnection();

        int var6;
        try {
            var6 = doStatements(connection, "drop", statements, ignoreErrors, databaseType.isCreateTableReturnsNegative(), false);
        } finally {
            connectionSource.releaseConnection(connection);
        }

        return var6;
    }

    private static <T, ID> void addDropIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements) {
        HashSet indexSet = new HashSet();
        FieldType[] sb = tableInfo.getFieldTypes();
        int i$ = sb.length;

        for(int indexName = 0; indexName < i$; ++indexName) {
            FieldType fieldType = sb[indexName];
            String indexName1 = fieldType.getIndexName();
            if(indexName1 != null) {
                indexSet.add(indexName1);
            }

            String uniqueIndexName = fieldType.getUniqueIndexName();
            if(uniqueIndexName != null) {
                indexSet.add(uniqueIndexName);
            }
        }

        StringBuilder var10 = new StringBuilder(48);
        Iterator var11 = indexSet.iterator();

        while(var11.hasNext()) {
            String var12 = (String)var11.next();
            logger.info("dropping index \'{}\' for table \'{}", var12, tableInfo.getTableName());
            var10.append("DROP INDEX ");
            databaseType.appendEscapedEntityName(var10, var12);
            statements.add(var10.toString());
            var10.setLength(0);
        }

    }

    private static <T, ID> void addCreateTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements, List<String> queriesAfter, boolean ifNotExists) throws SQLException {
        StringBuilder sb = new StringBuilder(256);
        sb.append("CREATE TABLE ");
        if(ifNotExists && databaseType.isCreateIfNotExistsSupported()) {
            sb.append("IF NOT EXISTS ");
        }

        databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
        sb.append(" (");
        ArrayList additionalArgs = new ArrayList();
        ArrayList statementsBefore = new ArrayList();
        ArrayList statementsAfter = new ArrayList();
        boolean first = true;
        FieldType[] i$ = tableInfo.getFieldTypes();
        int arg = i$.length;

        for(int i$1 = 0; i$1 < arg; ++i$1) {
            FieldType fieldType = i$[i$1];
            if(!fieldType.isForeignCollection()) {
                if(first) {
                    first = false;
                } else {
                    sb.append(", ");
                }

                String columnDefinition = fieldType.getColumnDefinition();
                if(columnDefinition == null) {
                    databaseType.appendColumnArg(tableInfo.getTableName(), sb, fieldType, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
                } else {
                    databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
                    sb.append(' ').append(columnDefinition).append(' ');
                }
            }
        }

        databaseType.addPrimaryKeySql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter, queriesAfter);
        databaseType.addUniqueComboSql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter, queriesAfter);
        Iterator var16 = additionalArgs.iterator();

        while(var16.hasNext()) {
            String var15 = (String)var16.next();
            sb.append(", ").append(var15);
        }

        sb.append(") ");
        databaseType.appendCreateTableSuffix(sb);
        statements.addAll(statementsBefore);
        statements.add(sb.toString());
        statements.addAll(statementsAfter);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, false);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, true);
    }

    private static <T, ID> void addCreateIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements, boolean ifNotExists, boolean unique) {
        HashMap indexMap = new HashMap();
        FieldType[] sb = tableInfo.getFieldTypes();
        int i$ = sb.length;

        for(int indexEntry = 0; indexEntry < i$; ++indexEntry) {
            FieldType first = sb[indexEntry];
            String i$1;
            if(unique) {
                i$1 = first.getUniqueIndexName();
            } else {
                i$1 = first.getIndexName();
            }

            if(i$1 != null) {
                Object columnName = (List)indexMap.get(i$1);
                if(columnName == null) {
                    columnName = new ArrayList();
                    indexMap.put(i$1, columnName);
                }

                ((List)columnName).add(first.getColumnName());
            }
        }

        StringBuilder var12 = new StringBuilder(128);
        Iterator var13 = indexMap.entrySet().iterator();

        while(var13.hasNext()) {
            Entry var14 = (Entry)var13.next();
            logger.info("creating index \'{}\' for table \'{}", var14.getKey(), tableInfo.getTableName());
            var12.append("CREATE ");
            if(unique) {
                var12.append("UNIQUE ");
            }

            var12.append("INDEX ");
            if(ifNotExists && databaseType.isCreateIndexIfNotExistsSupported()) {
                var12.append("IF NOT EXISTS ");
            }

            databaseType.appendEscapedEntityName(var12, (String)var14.getKey());
            var12.append(" ON ");
            databaseType.appendEscapedEntityName(var12, tableInfo.getTableName());
            var12.append(" ( ");
            boolean var15 = true;

            String var17;
            for(Iterator var16 = ((List)var14.getValue()).iterator(); var16.hasNext(); databaseType.appendEscapedEntityName(var12, var17)) {
                var17 = (String)var16.next();
                if(var15) {
                    var15 = false;
                } else {
                    var12.append(", ");
                }
            }

            var12.append(" )");
            statements.add(var12.toString());
            var12.setLength(0);
        }

    }

    private static <T, ID> void addDropTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements) {
        ArrayList statementsBefore = new ArrayList();
        ArrayList statementsAfter = new ArrayList();
        FieldType[] sb = tableInfo.getFieldTypes();
        int len$ = sb.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            FieldType fieldType = sb[i$];
            databaseType.dropColumnArg(fieldType, statementsBefore, statementsAfter);
        }

        StringBuilder var9 = new StringBuilder(64);
        var9.append("DROP TABLE ");
        databaseType.appendEscapedEntityName(var9, tableInfo.getTableName());
        var9.append(' ');
        statements.addAll(statementsBefore);
        statements.add(var9.toString());
        statements.addAll(statementsAfter);
    }

    private static <T, ID> int doCreateTable(ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        logger.info("creating table \'{}\'", tableInfo.getTableName());
        ArrayList statements = new ArrayList();
        ArrayList queriesAfter = new ArrayList();
        addCreateTableStatements(databaseType, tableInfo, statements, queriesAfter, ifNotExists);
        DatabaseConnection connection = connectionSource.getReadWriteConnection();

        int var8;
        try {
            int stmtC = doStatements(connection, "create", statements, false, databaseType.isCreateTableReturnsNegative(), databaseType.isCreateTableReturnsZero());
            stmtC += doCreateTestQueries(connection, databaseType, queriesAfter);
            var8 = stmtC;
        } finally {
            connectionSource.releaseConnection(connection);
        }

        return var8;
    }

    private static int doStatements(DatabaseConnection connection, String label, Collection<String> statements, boolean ignoreErrors, boolean returnsNegative, boolean expectingZero) throws SQLException {
        int stmtC = 0;

        for(Iterator i$ = statements.iterator(); i$.hasNext(); ++stmtC) {
            String statement = (String)i$.next();
            int rowC = 0;
            CompiledStatement compiledStmt = null;

            try {
                compiledStmt = connection.compileStatement(statement, StatementType.EXECUTE, noFieldTypes, -1);
                rowC = compiledStmt.runExecute();
                logger.info("executed {} table statement changed {} rows: {}", label, Integer.valueOf(rowC), statement);
            } catch (SQLException var15) {
                if(!ignoreErrors) {
                    throw SqlExceptionUtil.create("SQL statement failed: " + statement, var15);
                }

                logger.info("ignoring {} error \'{}\' for statement: {}", label, var15, statement);
            } finally {
                IOUtils.closeThrowSqlException(compiledStmt, "compiled statement");
            }

            if(rowC < 0) {
                if(!returnsNegative) {
                    throw new SQLException("SQL statement " + statement + " updated " + rowC + " rows, we were expecting >= 0");
                }
            } else if(rowC > 0 && expectingZero) {
                throw new SQLException("SQL statement updated " + rowC + " rows, we were expecting == 0: " + statement);
            }
        }

        return stmtC;
    }

    private static int doCreateTestQueries(DatabaseConnection connection, DatabaseType databaseType, List<String> queriesAfter) throws SQLException {
        int stmtC = 0;

        for(Iterator i$ = queriesAfter.iterator(); i$.hasNext(); ++stmtC) {
            String query = (String)i$.next();
            CompiledStatement compiledStmt = null;

            try {
                compiledStmt = connection.compileStatement(query, StatementType.SELECT, noFieldTypes, -1);
                DatabaseResults e = compiledStmt.runQuery((ObjectCache)null);
                int rowC = 0;

                for(boolean isThereMore = e.first(); isThereMore; isThereMore = e.next()) {
                    ++rowC;
                }

                logger.info("executing create table after-query got {} results: {}", Integer.valueOf(rowC), query);
            } catch (SQLException var13) {
                throw SqlExceptionUtil.create("executing create table after-query failed: " + query, var13);
            } finally {
                IOUtils.closeThrowSqlException(compiledStmt, "compiled statement");
            }
        }

        return stmtC;
    }

    private static <T, ID> List<String> addCreateTableStatements(ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        ArrayList statements = new ArrayList();
        ArrayList queriesAfter = new ArrayList();
        addCreateTableStatements(connectionSource.getDatabaseType(), tableInfo, statements, queriesAfter, ifNotExists);
        return statements;
    }
}
