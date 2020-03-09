package com.nvest.user.databaseFiles;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;

public interface QueryBuilder extends SupportSQLiteQuery{
    String argumentsPassed();

}
