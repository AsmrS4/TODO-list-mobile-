package com.example.mobile_todo

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_todo.adapters.ToDoAdapter
import com.example.mobile_todo.interfaces.DialogCloseListener
import com.example.mobile_todo.utils.DatabaseHandler
import java.util.Collections


class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private var taskList: MutableList<Task>? = null
    private lateinit var addButton: Button
    private lateinit var saveButton: Button
    private var db: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_layout)
        db = DatabaseHandler(this);
        db!!.openDatabase();

        tasksRecyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = ToDoAdapter(db, this)
        tasksRecyclerView.adapter = tasksAdapter
        taskList = mutableListOf<Task>()

        addButton = findViewById(R.id.addButton)
        saveButton = findViewById(R.id.saveButton)

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        taskList = db!!.allTasks;
        tasksAdapter.setTasks(taskList!!)

        saveButton.setOnClickListener {
            Toast.makeText(this, "Список сохранен", Toast.LENGTH_SHORT).show()
        }
        addButton.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        };
    }
    override fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db?.allTasks;
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}

