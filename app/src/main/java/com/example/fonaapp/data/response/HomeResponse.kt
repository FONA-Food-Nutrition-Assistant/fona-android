package com.example.fonaapp.data.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("method")
	val method: String,

	@field:SerializedName("data")
	val data: DataHome,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class FoodsItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class DataHome(

	@field:SerializedName("daily_needs")
	val dailyNeeds: DailyNeeds,

	@field:SerializedName("food_suggestion")
	val foodSuggestion: FoodSuggestion,

	@field:SerializedName("record_foods")
	val recordFoods: RecordFoods,

	@field:SerializedName("record_water")
	val recordWater: List<RecordWaterItem>,

	@field:SerializedName("daily_analysis")
	val dailyAnalysis: DailyAnalysis
)

data class BreakfastItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("total_cals")
	val totalCals: Int
)

data class DailyNeeds(

	@field:SerializedName("fats")
	val fats: Double,

	@field:SerializedName("proteins")
	val proteins: Double,

	@field:SerializedName("TDEE")
	val tDEE: Int,

	@field:SerializedName("carbos")
	val carbos: Double
)

data class Lunch(

	@field:SerializedName("foods")
	val foods: List<FoodsItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("total_cals")
	val totalCals: Int
)

data class LunchItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("total_cals")
	val totalCals: Int
)

data class DinnerItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("total_cals")
	val totalCals: Int
)

data class RecordFoods(

	@field:SerializedName("lunch")
	val lunch: List<LunchItem>,

	@field:SerializedName("breakfast")
	val breakfast: List<BreakfastItem>,

	@field:SerializedName("dinner")
	val dinner: List<DinnerItem>
)

data class Dinner(

	@field:SerializedName("foods")
	val foods: List<FoodsItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("total_cals")
	val totalCals: Int
)

data class DailyAnalysis(

	@field:SerializedName("total_fibers")
	val totalFibers: Any,

	@field:SerializedName("total_carbos")
	val totalCarbos: Any,

	@field:SerializedName("total_proteins")
	val totalProteins: Any,

	@field:SerializedName("total_fats")
	val totalFats: Any,

	@field:SerializedName("total_cals")
	val totalCals: Int,

	@field:SerializedName("total_sodiums")
	val totalSodiums: Any,

	@field:SerializedName("total_caliums")
	val totalCaliums: Any,

	@field:SerializedName("total_glucoses")
	val totalGlucoses: Any
)

data class RecordWaterItem(

	@field:SerializedName("number_of_cups")
	val numberOfCups: Int
)

data class FoodSuggestion(

	@field:SerializedName("lunch")
	val lunch: Lunch,

	@field:SerializedName("breakfast")
	val breakfast: Breakfast,

	@field:SerializedName("dinner")
	val dinner: Dinner
)

data class Breakfast(

	@field:SerializedName("foods")
	val foods: List<FoodsItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("total_cals")
	val totalCals: Int
)
