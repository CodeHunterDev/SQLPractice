package randomappsinc.com.sqlpractice.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import randomappsinc.com.sqlpractice.database.models.ResultSet;
import randomappsinc.com.sqlpractice.database.models.Schema;

public class DataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private SchemaServer schemaServer;

    // Constructor
    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        schemaServer = SchemaServer.getSchemaServer();
    }

    // Open connection to database
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Terminate connection to database
    private void close() {
        dbHelper.close();
    }

    // 1. Create any tables that haven't been created yet
    // 2. Check all non-persistent tables to see if they need an update. If so, rebuild them
    // NOTE: This doesn't need an open()/close() pair since it's a collection of other commands
    public void refreshTables() {
        // Create any missing tables
        String[] allTables = schemaServer.serveAllTableNames();
        for (String table : allTables) {
            if (!tableExists(table)) {
                createTable(table);
            }
        }

        // Get list of tables that can renewed from schema server
        String[] targetTables = schemaServer.serveTableNames();

        // For each of those tables, check its current row count with what it should be
        // Then destroy/rebuild it if those numbers don't match
        for (String targetTable : targetTables) {
            if (getNumRowsInTable(targetTable) !=
                    schemaServer.serveTable(targetTable).numRows()) {
                clearTable(targetTable);
                repopulateTable(targetTable);
            }
        }
    }

    // Clear a table's contents based on name
    private void clearTable(String tableName) {
        open();
        database.delete(tableName, null, null);
        close();
    }

    // Repopulate a table based on name
    private void repopulateTable(String tableName) {
        open();
        Schema table = schemaServer.serveTable(tableName);
        String[] inserts = table.insertStatements();

        if (inserts != null) {
            // Run each of the table's inserts
            for (String insert : inserts) {
                database.execSQL(insert);
            }
        }
        close();
    }

    // Creates a table
    private void createTable(String tableName) {
        open();

        // Create the table
        database.execSQL(schemaServer.serveTable(tableName).creationStatement());

        // Fill it with data
        repopulateTable(tableName);

        close();
    }

    // Checks to see if the table with the given name exists
    private boolean tableExists(String tableName) {
        open();
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master " +
                "where tbl_name = '" + tableName+ "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                close();
                return true;
            }
            cursor.close();
        }
        close();
        return false;
    }

    // Return the number of tuples in a certain table. This number may be outdated
    private int getNumRowsInTable(String tableName) {
        open();
        int numRows = 0;
        String query = "SELECT COUNT(*) FROM " + tableName + ";";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println("Table not found.");
        }
        else {
            cursor.moveToNext();
            numRows = cursor.getInt(0);
        }
        // This way, the connection to the DB is closed for sure while also freeing the cursor
        cursor.close();
        close();
        return numRows;
    }

    public ResultSet getResultsOfQuery(String queryString) {
        open();
        try {
            Cursor cursor = database.rawQuery(queryString, null);
            int row = cursor.getCount(), col = cursor.getColumnCount();
            String columns[] = new String[col];
            for (int i = 0; i < col; i++) {
                columns[i] = cursor.getColumnName(i);
            }

            // If no data was gotten, return null
            if (row == 0) {
                String[][] empty = {};
                return new ResultSet(columns, empty, "");
            }

            String[][] ourData = new String[row][col];
            int[] typeDict = new int[col];

            cursor.moveToNext();
            for (int i = 0; i < col; i++) {
                typeDict[i] = cursor.getType(i);
            }
            cursor.moveToPrevious();

            int eye = 0;
            while (cursor.moveToNext()) {
                for (int i = 0; i < col; i++) {
                    switch (typeDict[i]) {
                        case Cursor.FIELD_TYPE_STRING:
                            ourData[eye][i] = cursor.getString(i);
                            break;

                        case Cursor.FIELD_TYPE_INTEGER:
                            ourData[eye][i] = String.valueOf(cursor.getInt(i));
                            break;

                        case Cursor.FIELD_TYPE_FLOAT:
                            ourData[eye][i] = String.valueOf(cursor.getFloat(i));
                            break;

                        case Cursor.FIELD_TYPE_NULL:
                            ourData[eye][i] = null;
                            break;

                        case Cursor.FIELD_TYPE_BLOB:
                            break;
                    }
                }
                eye++;
            }
            cursor.close();
            close();
            return new ResultSet(columns, ourData, "");
        } catch (SQLiteException exception) {
            String[] pieces = exception.getMessage().split(" \\(code");
            if (pieces.length > 0) {
                return new ResultSet(null, null, pieces[0]);
            } else {
                return new ResultSet(null, null, exception.getMessage());
            }
        }
    }
}
