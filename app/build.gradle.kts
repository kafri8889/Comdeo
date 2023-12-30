import com.google.devtools.ksp.gradle.KspTaskJvm

plugins {
	id("idea")
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("kotlin-android")
	id("kotlin-kapt")
	id("kotlin-parcelize")
	id("com.squareup.wire")
	id("com.google.devtools.ksp")
	id("org.jetbrains.kotlin.plugin.serialization")
}

android {
	namespace = "com.anafthdev.comdeo"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.anafthdev.comdeo"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true

			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)

			kotlinOptions {
				freeCompilerArgs += listOf(
					"-opt-in=kotlin.RequiresOptIn",
					"-Xjvm-default=all"
				)
			}
		}
		debug {
			isMinifyEnabled = false
			isDebuggable = true

			kotlinOptions {
				freeCompilerArgs += listOf(
					"-opt-in=kotlin.RequiresOptIn",
					"-Xjvm-default=all"
				)
			}
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs += listOf(
			"-P",
			"plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.buildDir.absolutePath + "/compose_metrics"
		)
		freeCompilerArgs += listOf(
			"-P",
			"plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination="  + project.buildDir.absolutePath + "/compose_metrics"
		)
	}
	buildFeatures {
		buildConfig = true
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.5"
	}
	packaging {
		resources {
			excludes.add("/META-INF/{AL2.0,LGPL2.1}")
		}
	}
}

// Add this to fix ksp debug error when using wire and ksp
androidComponents {
	onVariants { variant ->
		// https://github.com/square/wire/issues/2335
		val buildType = variant.buildType.toString()
		val flavor = variant.flavorName.toString()
		tasks.withType<KspTaskJvm> {
			if (name.contains(buildType, ignoreCase = true) && name.contains(
					flavor,
					ignoreCase = true
				)
			) {
				dependsOn("generate${flavor.capitalize()}${buildType.capitalize()}Protos")
			}
		}
	}
}

wire {
	kotlin {
		android = true
	}
}

dependencies {

	val compose_version = "1.5.4"
	val lifecycle_version = "2.6.2"
	val accompanist_version = "0.32.0"

	implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
	kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.0")

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation("androidx.compose.runtime:runtime:1.5.4")
	implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
	implementation("androidx.navigation:navigation-compose:2.7.6")
	implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
	implementation("androidx.core:core-splashscreen:1.0.1")

	// Compose Common
	implementation("androidx.compose.ui:ui:$compose_version")
	implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
	implementation("androidx.compose.foundation:foundation:$compose_version")
	implementation("androidx.compose.ui:ui-util:$compose_version")
	implementation("androidx.compose.animation:animation:$compose_version")

	// Compose Android
	implementation("androidx.compose.ui:ui-android:$compose_version")
	implementation("androidx.compose.ui:ui-tooling-preview-android:$compose_version")
	implementation("androidx.compose.foundation:foundation-android:$compose_version")
	implementation("androidx.compose.ui:ui-util-android:$compose_version")
	implementation("androidx.compose.animation:animation-android:$compose_version")

	// Constraint layout
	implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")

	// Material Design
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.compose.material:material:$compose_version")
	implementation("androidx.compose.material:material-icons-extended:$compose_version")
	implementation("androidx.compose.material3:material3-android:1.2.0-beta01")
	implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

	// Large screen support
	implementation("androidx.window:window:1.2.0")

	// Datastore
	implementation("androidx.datastore:datastore:1.0.0")
	implementation("androidx.datastore:datastore-preferences:1.0.0")
	implementation("androidx.datastore:datastore-core:1.0.0")
//    implementation("com.google.protobuf:protobuf-javalite:3.18.0")
//    implementation("com.google.protobuf:protobuf-kotlin:3.19.1")
//    implementation("com.google.protobuf:protobuf-kotlin-lite:3.19.1")

	// Lifecycle
	implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
	kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

	// Dependency Injection
	implementation("com.google.dagger:hilt-android:2.48")
	ksp("androidx.hilt:hilt-compiler:1.1.0")
	ksp("com.google.dagger:hilt-compiler:2.48")
	ksp("com.google.dagger:hilt-android-compiler:2.48")

	// Room
	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")

	// Work Manager
	implementation("androidx.hilt:hilt-work:1.1.0")
	implementation("androidx.work:work-runtime-ktx:2.9.0")

	// Media
	implementation("androidx.media3:media3-exoplayer:1.2.0")
	implementation("androidx.media3:media3-ui:1.2.0")

	// Accompanist
	implementation("com.google.accompanist:accompanist-navigation-material:$accompanist_version")

	// Other
	implementation("com.google.code.gson:gson:2.10")
	implementation("com.jakewharton.timber:timber:5.0.1")
	implementation("com.squareup.wire:wire-runtime:4.4.3")
	implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

	debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")

}