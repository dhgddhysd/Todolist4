package com.soyeon.todolist4

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item_view.view.*

class TodoAdapter (list: ArrayList<Todo>, _context: Context) : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    private var todoList: ArrayList<Todo> = list
    private val context = _context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.todo_item_view, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val db = DBHelper(context).writableDatabase
        holder.itemView.title_tv.text = todoList.get(position).title
        holder.itemView.checkbox.setChecked(todoList.get(position).checked)
        holder.itemView.del_btn.setOnClickListener {
            db.execSQL("DELETE FROM todolist WHERE title = ('${todoList[position].title}')")
            todoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, todoList.size)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
    class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    }
}
