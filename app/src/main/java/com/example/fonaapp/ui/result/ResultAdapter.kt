import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fonaapp.data.response.AllergiesItem
import com.example.fonaapp.databinding.ItemsAllergyBinding

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.AllergyViewHolder>() {

    private var allergies: List<AllergiesItem> = emptyList()

    inner class AllergyViewHolder(private val binding: ItemsAllergyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(allergy: AllergiesItem) {
            // Menggunakan ViewBinding untuk mengatur data ke tampilan
            binding.tvAllergyName.text = allergy.name
            // Tambahan: Jika Anda memiliki gambar untuk setiap alergi, Anda bisa mengaturnya di sini
            // binding.ivAllergyIcon.setImageResource(R.drawable.ic_allergy)
        }
    }
    fun submitList(allergies: List<AllergiesItem>) {
        this.allergies = allergies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
        val binding = ItemsAllergyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllergyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
        holder.bind(allergies[position])
    }

    override fun getItemCount(): Int {
        return allergies.size
    }

}
