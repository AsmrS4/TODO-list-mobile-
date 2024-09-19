package com.example.mobile_todo.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_todo.AddNewTask
import com.example.mobile_todo.MainActivity
import com.example.mobile_todo.R
import com.example.mobile_todo.Task
import com.example.mobile_todo.utils.DatabaseHandler


class ToDoAdapter(db: DatabaseHandler?, activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    private var todoList: MutableList<Task>? = null
    private var activity: MainActivity? = null
    private var db: DatabaseHandler? = null

    init {
        this.activity = activity
        this.db = db
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db?.openDatabase();
        val item: Task = todoList!![position]
        holder.task.text = item.getText()
        holder.task.isChecked = toBoolean(item.getStatus())
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                db!!.updateStatus(item.getId(), 1)

            } else {
                db!!.updateStatus(item.getId(), 0)

            }
        }
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return todoList!!.size
    }

    val context: Context
        get() = activity!!

    fun setTasks(todoList:MutableList<Task>?) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item: Task = todoList!![position]
        db!!.deleteTask(item.getId())
        todoList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item: Task = todoList!![position]
        val bundle = Bundle()
        bundle.putInt("id", item.getId())
        bundle.putString("task", item.getText())
        val fragment: AddNewTask<*> = AddNewTask<Any?>()
        fragment.arguments = bundle
        activity?.let { fragment.show(it.supportFragmentManager, AddNewTask.TAG) }
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox = itemView.findViewById<CheckBox>(R.id.todoCheckBox)
    }
}
