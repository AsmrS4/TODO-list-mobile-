package com.example.mobile_todo


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.mobile_todo.interfaces.DialogCloseListener
import com.example.mobile_todo.utils.DatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddNewTask<Activity> : BottomSheetDialogFragment() {

    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private var db: DatabaseHandler? = null

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, saveInstanceState: Bundle?) {
        super.onViewCreated(requireView(), saveInstanceState)
        newTaskText = requireView().findViewById(R.id.newTaskText)
        newTaskSaveButton = requireView().findViewById(R.id.newTaskButton)
        newTaskSaveButton.setText(R.string.save_text_btn)
        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            if (task!!.isNotEmpty()) {
                newTaskSaveButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.active_text_color
                    )
                )
            }
        }
        db = DatabaseHandler(activity);
        db!!.openDatabase();

        val finalIsUpdated = isUpdate
        newTaskSaveButton.setOnClickListener(View.OnClickListener {
            val text = newTaskText.text.toString()
            if (finalIsUpdated) {
                if (bundle != null) {
                    db!!.updateTask(bundle.getInt("id"), text);
                }
            } else {
                val task = Task()
                task.setText(text)
                task.setStatus(0)
                db!!.insertTask(task);
            }
            dismiss()
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity: FragmentActivity? = activity
        if (activity is DialogCloseListener) {
            (activity as DialogCloseListener?)?.handleDialogClose(dialog)
        }
    }

    companion object {
        const val TAG: String = "ActionBottomDialog"
        fun newInstance(): AddNewTask<Any?> {
            return AddNewTask()
        }
    }
}