package com.example.mobile_todo

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_todo.adapters.ToDoAdapter
import com.example.mobile_todo.interfaces.DialogCloseListener
import com.example.mobile_todo.utils.DatabaseHandler
import kotlinx.serialization.Serializable
import java.util.Collections


@Serializable
data class TaskData(val id: Int, val text: String)

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private var taskList: MutableList<Task>? = null
    private lateinit var addButton: Button
    private lateinit var loadButton: Button
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

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        taskList = db!!.allTasks;
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList!!)

        addButton.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        };
    }
    override fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db?.allTasks;
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

}

