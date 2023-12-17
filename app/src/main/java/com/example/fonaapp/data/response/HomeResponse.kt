package com.example.fonaapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HomeResponse(
	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: DataResponse

)
data class DataResponse(

	@field:SerializedName("calorie_intake")
	val calorieIntake: CalorieIntake,

	@field:SerializedName("daily_needs")
	val dailyNeeds: DailyNeeds,

	@field:SerializedName("daily_analysis")
	val dailyAnalysis: DailyAnalysis,

	@field:SerializedName("record_foods")
	val recordFoods: RecordFoods,

	@field:SerializedName("record_water")
	val recordWater: Int,

	@field:SerializedName("food_suggestion")
	val foodSuggestion: FoodSuggestion
)

data class CalorieIntake(
	@field:SerializedName("lowest_breakfast_intake")
	val lowestBreakfastIntake: Int,

	@field:SerializedName("highest_breakfast_intake")
	val highestBreakfastIntake: Int,

	@field:SerializedName("lowest_lunch_intake")
	val lowestLunchIntake: Int,

	@field:SerializedName("highest_lunch_intake")
	val highestLunchIntake: Int,

	@field:SerializedName("lowest_dinner_intake")
	val lowestDinnerIntake: Int,

	@field:SerializedName("highest_dinner_intake")
	val highestDinnerIntake: Int
)

data class DailyNeeds(

	@field:SerializedName("fats")
	val fats: Double,

	@field:SerializedName("proteins")
	val proteins: Double,

	@field:SerializedName("TDEE")
	val tDEE: Double,

	@field:SerializedName("carbos")
	val carbos: Double
)

data class DailyAnalysis(

	@field:SerializedName("total_fibers")
	val totalFibers: Double,

	@field:SerializedName("total_carbos")
	val totalCarbos: Double,

	@field:SerializedName("total_proteins")
	val totalProteins: Double,

	@field:SerializedName("total_fats")
	val totalFats: Double,

	@field:SerializedName("total_cals")
	val totalCals: Double,

	@field:SerializedName("total_sodiums")
	val totalSodiums: Double,

	@field:SerializedName("total_caliums")
	val totalCaliums: Double,

	@field:SerializedName("total_glucoses")
	val totalGlucoses: Double
)

data class RecordFoods(
	@field:SerializedName("breakfast")
	var breakfast: MutableList<BreakfastItem>,

	@field:SerializedName("lunch")
	val lunch: MutableList<LunchItem>,

	@field:SerializedName("dinner")
	val dinner: MutableList<DinnerItem>
)

@Parcelize
data class BreakfastItem(
	@field:SerializedName("food_id")
	val food_id: Int,

	@field:SerializedName("nutrition_id")
	val nutrition_id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("serving_size")
	val serving_size: String,

	@field:SerializedName("quantity")
	val quantity: Double,

	@field:SerializedName("total_cals")
	val total_cals: Double,

	@field:SerializedName("total_carbos")
	val total_carbos: Double,

	@field:SerializedName("total_proteins")
	val total_proteins: Double,

	@field:SerializedName("total_fats")
	val total_fats: Double,

	@field:SerializedName("total_fibers")
	val total_fibers: Double,

	@field:SerializedName("total_caliums")
	val total_caliums: Double,

	@field:SerializedName("total_sodiums")
	val total_sodiums: Double,

	@field:SerializedName("total_glucoses")
	val glucoses: Double
) : Parcelable

@Parcelize
data class LunchItem(

	@field:SerializedName("food_id")
	val food_id: Int,

	@field:SerializedName("nutrition_id")
	val nutrition_id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("serving_size")
	val serving_size: String,

	@field:SerializedName("quantity")
	val quantity: Double,

	@field:SerializedName("total_cals")
	val total_cals: Double,

	@field:SerializedName("total_carbos")
	val total_carbos: Double,

	@field:SerializedName("total_proteins")
	val total_proteins: Double,

	@field:SerializedName("total_fats")
	val total_fats: Double,

	@field:SerializedName("total_fibers")
	val total_fibers: Double,

	@field:SerializedName("total_caliums")
	val total_caliums: Double,

	@field:SerializedName("total_sodiums")
	val total_sodiums: Double,

	@field:SerializedName("total_glucoses")
	val glucoses: Double
) : Parcelable

@Parcelize
data class DinnerItem(

	@field:SerializedName("food_id")
	val food_id: Int,

	@field:SerializedName("nutrition_id")
	val nutrition_id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("serving_size")
	val serving_size: String,

	@field:SerializedName("quantity")
	val quantity: Double,

	@field:SerializedName("total_cals")
	val total_cals: Double,

	@field:SerializedName("total_carbos")
	val total_carbos: Double,

	@field:SerializedName("total_proteins")
	val total_proteins: Double,

	@field:SerializedName("total_fats")
	val total_fats: Double,

	@field:SerializedName("total_fibers")
	val total_fibers: Double,

	@field:SerializedName("total_caliums")
	val total_caliums: Double,

	@field:SerializedName("total_sodiums")
	val total_sodiums: Double,

	@field:SerializedName("total_glucoses")
	val glucoses: Double
) : Parcelable

data class FoodSuggestion(

	@SerializedName("breakfast")
	val breakfast: Meal,

	@SerializedName("lunch")
	val lunch: Meal,

	@SerializedName("dinner")
	val dinner: Meal
)

data class Meal(
	@SerializedName("id")
	val id: Int,

	@SerializedName("name")
	val name: String,

	@SerializedName("image_url")
	val imageUrl: String,

	@SerializedName("total_cals")
	val totalCals: Double,

	@SerializedName("foods")
	val foods: List<FoodsItem>
)

data class FoodsItem(

	@SerializedName("id")
	val id: Int,

	@SerializedName("name")
	val name: String,

	@SerializedName("cals")
	val cals: Double
)