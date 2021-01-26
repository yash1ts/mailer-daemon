package com.mailerdaemon.app.clubs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.facebook.drawee.view.SimpleDraweeView
import com.mailerdaemon.app.R
import com.mailerdaemon.app.utils.DialogOptions
import java.util.*

class ClubAdapter internal constructor(context: Context, options: DialogOptions) :
    RecyclerView.Adapter<ClubAdapter.Holder>() {
    private var iconModel: List<ClubIconModel> = ArrayList()
    private var drawable: CircularProgressDrawable
    private val options: DialogOptions
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_club, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val (url, tag) = iconModel[position]
        if (url == null) {
            holder.icon.setActualImageResource(R.drawable.ic_account_circle_black_24dp)
        } else {
            var id = 0
            when (tag) {
                1 -> id = R.drawable.md_logo
                2 -> id = R.drawable.litc_logo
                3 -> id = R.drawable.mechismu_logo
                4 -> id = R.drawable.cyberlabs_logo
                5 -> id = R.drawable.chayanika_logo
                6 -> id = R.drawable.ecell_logo
                7 -> id = R.drawable.arka_logo
                8 -> id = R.drawable.quiz_logo
                9 -> id = R.drawable.udaan_logo
                10 -> id = R.drawable.toastmaster_logo
                11 -> id = R.drawable.ffi_logo
                12 -> id = R.drawable.wtc_logo
                13 -> id = R.drawable.kartavya_logo
                14 -> id = R.drawable.rythm_logo
                15 -> id = R.drawable.litm_logo
                16 -> id = R.drawable.lci_logo
                17 -> id = R.drawable.ff_logo
                18 -> id = R.drawable.roboism_logo
                19 -> id = R.drawable.manthan_logo
                20 -> id = R.drawable.adc_logo
                21 -> id = R.drawable.art_logo
            }
            holder.icon.setActualImageResource(id)
        }
        holder.icon.setOnClickListener { options.showDialog(Objects.requireNonNull(tag).toString()) }
    }

    override fun getItemCount(): Int {
        return iconModel.size
    }

    fun setData(iconModel: List<ClubIconModel>) {
        this.iconModel = iconModel
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: SimpleDraweeView = itemView.findViewById(R.id.club_icon)

        init {
            drawable = CircularProgressDrawable(itemView.context)
            icon.hierarchy.setProgressBarImage(drawable)
        }
    }

    init {
        drawable = CircularProgressDrawable(context)
        this.options = options
    }
}
