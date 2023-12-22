package com.bangkit23b2.fonaapp.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadFoodResponse(
	val method: String,
	val data: List<DataFood>,
	val message: String,
	val status: Int
) : Parcelable

@Parcelize
data class NutritionsItem(
	val fibers: Double,
	val carbos: Double,
	val createdAt: String?,
	val cals: Double,
	val foodId: Int,
	val glucoses: Double,
	val caliums: Double,
	val sodiums: Double,
	val updatedAt: String?,
	val fats: Double,
	val proteins: Double,
	val serving_size: String,
	val id: Int
) : Parcelable

@Parcelize
data class DataFood(
	val name: String,
	val isUserAllergy: Boolean,
	val id: Int,
	val nutritions: List<NutritionsItem>
) : Parcelable