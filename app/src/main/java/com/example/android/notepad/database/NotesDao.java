package com.example.android.notepad.database;

import com.example.android.notepad.entity.Note;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * 访问数据
 *
 */
@Dao
public interface NotesDao {
    /**
     * 插入数据
     *
     * @param note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    /**
     * 删除数据
     *
     * @param note
     */
    @Delete
    void deleteNote(Note... note);

    /**
     *
     * @param note
     */
    @Update
    void updateNote(Note note);

    /**
     * 查询全部数据
     *
     * @return list of Note
     */
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    @Query("SELECT * FROM notes WHERE title LIKE  '%' || :text || '%' ")
    List<Note> searchNotes(String text);

    /**
     * 根据id查询一条数据
     * @param noteId note id
     * @return Note
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);

    /**
     * 根据类型查询
     * @param noteType
     * @return
     */
    @Query("SELECT * FROM notes WHERE type = :noteType")
    List<Note>  getNotesByType(int noteType);
    /**
     * 根据id删除一条数据
     *
     * @param noteId
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}
