package com.norm.foodrecipes.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.norm.foodrecipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.norm.foodrecipes.util.Constants.Companion.PREFS_BACK_ONLINE
import com.norm.foodrecipes.util.Constants.Companion.PREFS_DIET_TYPE
import com.norm.foodrecipes.util.Constants.Companion.PREFS_DIET_TYPE_ID
import com.norm.foodrecipes.util.Constants.Companion.PREFS_MEAL_TYPE
import com.norm.foodrecipes.util.Constants.Companion.PREFS_MEAL_TYPE_ID
import com.norm.foodrecipes.util.Constants.Companion.PREFS_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Preferences keys */
    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(PREFS_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFS_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFS_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFS_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFS_BACK_ONLINE)
    }

    /** creating [DataStore] to store preferences data */
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFS_NAME
    )

    /** save meal and diet type to [dataStore] */
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.selectedMealType] = mealType
            prefs[PreferenceKeys.selectedMealTypeId] = mealTypeId
            prefs[PreferenceKeys.selectedDietType] = dietType
            prefs[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    /** save backOnline status to [dataStore] */
    suspend fun saveBackOnlineStatus(backOnline: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.backOnline] = backOnline
        }
    }

    /** read meal and diet type from [dataStore] */
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val selectedMealType = prefs[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = prefs[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = prefs[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = prefs[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    /** read back online status from [dataStore] */
    val readBackOnlineStatus: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val backOnline = prefs[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

/** Data model for meal and diet type */
data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)