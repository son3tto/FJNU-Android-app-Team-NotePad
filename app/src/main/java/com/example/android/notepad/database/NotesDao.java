package com.example.android.notepad.database;

import com.example.android.notepad.entity.Notes;

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
    void insertNote(Notes note);

    /**
     * 删除数据
     *
     * @param note
     */
    @Delete
    void deleteNote(Notes... note);

    /**
     *
     * @param note
     */
    @Update
    void updateNote(Notes note);

    /**
     * 查询全部数据
     *
     * @return list of Notes
     */
    @Query("SELECT * FROM notes")
    List<Notes> getNotes();

    @Query("SELECT * FROM notes WHERE text LIKE  '%' || :text || '%' ")
    List<Notes> searchNotes(String text);
    /**
     * 根据id查询一条数据
     * @param noteId note id
     * @return Note
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Notes getNoteById(int noteId);

    /**
     * 根据id删除一条数据
     *
     * @param noteId
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}
