package com.example.currencyconverter.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.domain.entity.Currency
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideConverterDatabase(
        @ApplicationContext context: Context
    ): ConverterDatabase {
        return Room.databaseBuilder(
            context,
            ConverterDatabase::class.java,
            "converter_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val initialBalanceQuery = "INSERT INTO accounts (currency_code, amount) VALUES ('${Currency.RUB.name}', 75000.0)"
                        db.execSQL(initialBalanceQuery)
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountDao(database: ConverterDatabase): AccountDao {
        return database.accountDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: ConverterDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideRemoteRatesService(): RemoteRatesServiceImpl {
        return RemoteRatesServiceImpl()
    }
}