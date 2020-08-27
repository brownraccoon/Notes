package com.vishal.notes.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.vishal.notes.R
import com.vishal.notes.model.Note


class NotesAdapter(list: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder?>() {
    private val list: ArrayList<Note>
    private val context: Context
    private val onNoteItemClick: OnNoteItemClick
    private val onNoteChecked: OnNoteChecked
    private var onBind: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.note_list_item,
            parent,
            false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.textViewTitle.setText(list[position].title)
        holder.textViewDescription.setText(list[position].description)
        var checked = list[position].done
        if (checked!!) {
            onBind = true
            holder.checkBox.setChecked(true)
            onBind = false
            holder.itemView.setBackgroundColor(getColor(context, R.color.gray))
        } else {
            onBind = true
            holder.checkBox.setChecked(false)
            onBind = false
            holder.itemView.setBackgroundColor(getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textViewDescription: TextView
        var textViewTitle: TextView
        var checkBox: CheckBox
        override fun onClick(view: View) {
            onNoteItemClick.onNoteClick(getAdapterPosition())
        }

        init {
            itemView.setOnClickListener(this)
            textViewTitle = itemView.findViewById(R.id.title)
            textViewDescription = itemView.findViewById(R.id.description)
            checkBox = itemView.findViewById(R.id.checkBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (!onBind) {
                        val currentPosition: Int = getAdapterPosition()
                        if (isChecked) {
                            val itemmoved: Note = list.get(currentPosition)
                            list.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                            list.add(itemmoved)
                            list.get(list.size-1).done=true
                             notifyItemInserted(list.size-1)
                           // notifyDataSetChanged()
                          onNoteChecked.onNoteChecked(list.size-1,true)




                        } else {

                            list.get(currentPosition).done=false
                            notifyItemChanged(currentPosition)
                            onNoteChecked.onNoteChecked(currentPosition,false)
                        }
                }
            }
        }

    }

    interface OnNoteItemClick {
        fun onNoteClick(pos: Int)
    }

    interface OnNoteChecked {
        fun onNoteChecked(pos: Int, value: Boolean)
    }

    init {
        this.list = list as ArrayList<Note>
        this.context = context
        onNoteItemClick = context as OnNoteItemClick
        onNoteChecked = context as OnNoteChecked

    }
}