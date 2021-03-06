buildscript {
    extra["compose_version"] = "1.2.0-beta03"
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("com.google.dagger.hilt.android") version "2.42" apply false
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}