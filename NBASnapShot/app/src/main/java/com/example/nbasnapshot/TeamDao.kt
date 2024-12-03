import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nbasnapshot.TeamEntity

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Query("SELECT * FROM teams")
    suspend fun getAllTeams(): List<TeamEntity>

    @Query("DELETE FROM teams")
    suspend fun deleteAll()
}
