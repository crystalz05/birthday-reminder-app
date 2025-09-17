package com.tyro.birthdayreminder.repository


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://zfaubbhaecfoxausjaax.supabase.co" ,
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpmYXViYmhhZWNmb3hhdXNqYWF4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTczOTU5OTUsImV4cCI6MjA3Mjk3MTk5NX0.HL6RUlYQv52GoyXvf2TdfMfQcuHbQ8KUQk_QWcWOj6Y"
        ){

            install(Postgrest)
            install(Storage)
        }
    }
}