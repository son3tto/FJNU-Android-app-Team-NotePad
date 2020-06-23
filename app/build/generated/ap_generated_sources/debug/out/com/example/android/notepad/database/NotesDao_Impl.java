package com.example.android.notepad.database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.android.notepad.entity.Notes;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NotesDao_Impl implements NotesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Notes> __insertionAdapterOfNotes;

  private final EntityDeletionOrUpdateAdapter<Notes> __deletionAdapterOfNotes;

  private final EntityDeletionOrUpdateAdapter<Notes> __updateAdapterOfNotes;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNoteById;

  public NotesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNotes = new EntityInsertionAdapter<Notes>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `notes` (`id`,`title`,`text`,`date`,`create_date`,`type`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Notes value) {
        stmt.bindLong(1, value.getId());
        if (value.getNoteTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNoteTitle());
        }
        if (value.getNoteText() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNoteText());
        }
        stmt.bindLong(4, value.getNoteDate());
        stmt.bindLong(5, value.getCreateDate());
        if (value.getNoteType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getNoteType());
        }
      }
    };
    this.__deletionAdapterOfNotes = new EntityDeletionOrUpdateAdapter<Notes>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `notes` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Notes value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfNotes = new EntityDeletionOrUpdateAdapter<Notes>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `notes` SET `id` = ?,`title` = ?,`text` = ?,`date` = ?,`create_date` = ?,`type` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Notes value) {
        stmt.bindLong(1, value.getId());
        if (value.getNoteTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNoteTitle());
        }
        if (value.getNoteText() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNoteText());
        }
        stmt.bindLong(4, value.getNoteDate());
        stmt.bindLong(5, value.getCreateDate());
        if (value.getNoteType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getNoteType());
        }
        stmt.bindLong(7, value.getId());
      }
    };
    this.__preparedStmtOfDeleteNoteById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertNote(final Notes note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfNotes.insert(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNote(final Notes... note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNotes.handleMultiple(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateNote(final Notes note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfNotes.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNoteById(final int noteId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNoteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, noteId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteNoteById.release(_stmt);
    }
  }

  @Override
  public List<Notes> getNotes() {
    final String _sql = "SELECT * FROM notes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfNoteDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfCreateDate = CursorUtil.getColumnIndexOrThrow(_cursor, "create_date");
      final int _cursorIndexOfNoteType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final List<Notes> _result = new ArrayList<Notes>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Notes _item;
        final String _tmpNoteTitle;
        _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
        final String _tmpNoteText;
        _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
        final long _tmpNoteDate;
        _tmpNoteDate = _cursor.getLong(_cursorIndexOfNoteDate);
        _item = new Notes(_tmpNoteTitle,_tmpNoteText,_tmpNoteDate);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpCreateDate;
        _tmpCreateDate = _cursor.getLong(_cursorIndexOfCreateDate);
        _item.setCreateDate(_tmpCreateDate);
        final String _tmpNoteType;
        _tmpNoteType = _cursor.getString(_cursorIndexOfNoteType);
        _item.setNoteType(_tmpNoteType);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Notes> searchNotes(final String text) {
    final String _sql = "SELECT * FROM notes WHERE text LIKE  '%' || ? || '%' ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (text == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, text);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfNoteDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfCreateDate = CursorUtil.getColumnIndexOrThrow(_cursor, "create_date");
      final int _cursorIndexOfNoteType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final List<Notes> _result = new ArrayList<Notes>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Notes _item;
        final String _tmpNoteTitle;
        _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
        final String _tmpNoteText;
        _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
        final long _tmpNoteDate;
        _tmpNoteDate = _cursor.getLong(_cursorIndexOfNoteDate);
        _item = new Notes(_tmpNoteTitle,_tmpNoteText,_tmpNoteDate);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpCreateDate;
        _tmpCreateDate = _cursor.getLong(_cursorIndexOfCreateDate);
        _item.setCreateDate(_tmpCreateDate);
        final String _tmpNoteType;
        _tmpNoteType = _cursor.getString(_cursorIndexOfNoteType);
        _item.setNoteType(_tmpNoteType);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Notes getNoteById(final int noteId) {
    final String _sql = "SELECT * FROM notes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, noteId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfNoteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfNoteText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfNoteDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfCreateDate = CursorUtil.getColumnIndexOrThrow(_cursor, "create_date");
      final int _cursorIndexOfNoteType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final Notes _result;
      if(_cursor.moveToFirst()) {
        final String _tmpNoteTitle;
        _tmpNoteTitle = _cursor.getString(_cursorIndexOfNoteTitle);
        final String _tmpNoteText;
        _tmpNoteText = _cursor.getString(_cursorIndexOfNoteText);
        final long _tmpNoteDate;
        _tmpNoteDate = _cursor.getLong(_cursorIndexOfNoteDate);
        _result = new Notes(_tmpNoteTitle,_tmpNoteText,_tmpNoteDate);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpCreateDate;
        _tmpCreateDate = _cursor.getLong(_cursorIndexOfCreateDate);
        _result.setCreateDate(_tmpCreateDate);
        final String _tmpNoteType;
        _tmpNoteType = _cursor.getString(_cursorIndexOfNoteType);
        _result.setNoteType(_tmpNoteType);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
