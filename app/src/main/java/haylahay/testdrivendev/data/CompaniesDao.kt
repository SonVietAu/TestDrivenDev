package haylahay.testdrivendev.data

import androidx.room.*

@Dao
interface CompaniesDao {

    @Query("SELECT * FROM Company order by stockIndex")
    fun getCompanies(): List<Company>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(company: Company)

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)

}