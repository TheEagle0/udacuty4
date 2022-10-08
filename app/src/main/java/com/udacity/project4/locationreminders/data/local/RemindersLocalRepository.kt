package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.utils.EspressoIdlingResource.wrapEspressoIdlingResource
import kotlinx.coroutines.*


class RemindersLocalRepository(
    private val remindersDao: RemindersDao,
) : ReminderDataSource {


    override suspend fun getReminders(): Result<List<ReminderDTO>> = withContext(Dispatchers.IO) {
        wrapEspressoIdlingResource{
            return@withContext try {
                Result.Success(remindersDao.getReminders())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }

    }

    override suspend fun saveReminder(reminder: ReminderDTO) =
        withContext(Dispatchers.IO) {
            wrapEspressoIdlingResource {
                remindersDao.saveReminder(reminder)
            }

        }


    override suspend fun getReminder(id: String): Result<ReminderDTO> = withContext(Dispatchers.IO) {
        wrapEspressoIdlingResource {
            try {
                val reminder = remindersDao.getReminderById(id)
                if (reminder != null) {
                    return@withContext Result.Success(reminder)
                } else {
                    return@withContext Result.Error("Reminder not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }

    }


    override suspend fun deleteAllReminders() {
        wrapEspressoIdlingResource {
            withContext(Dispatchers.IO) {
                remindersDao.deleteAllReminders()
            }
        }

    }
}
