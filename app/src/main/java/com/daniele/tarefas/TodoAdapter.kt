package com.daniele.tarefas

import  android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log

import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TodoAdapter(
    private val todos: MutableList<Todo>
): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(Menu.NONE, R.id.excluir, Menu.NONE, "Excluir")
            menu?.add(Menu.NONE, R.id.cancelar, Menu.NONE, "Cancelar")
        }

        init {
            itemView.setOnCreateContextMenuListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteOne(index: Int) {
        todos.removeAt(index);

        notifyItemRemoved(index);
    }

    fun clearAllToDos() {
        todos.clear()
        notifyDataSetChanged()
    }
    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
    val curTodo = todos [position]
        holder.itemView.apply {
            val tvTodoTitle = findViewById<TextView>(R.id.tvTodoTitle)
            tvTodoTitle.text = curTodo.title
            val  cbDone = findViewById<CheckBox>(R.id.cbDone)
            cbDone.isChecked = curTodo.isChecked
            toggleStrikeThrough(tvTodoTitle, curTodo.isChecked)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.isChecked = !curTodo.isChecked
            }

            setOnClickListener(View.OnClickListener {
                Log.d( "Click", "curto no " + curTodo.title)
                cbDone.isChecked = !cbDone.isChecked
            })

            setOnLongClickListener(View.OnLongClickListener {
                setCurrentPosition(holder.adapterPosition)
                false
            })
        }
    }

    override fun onViewRecycled(holder: TodoViewHolder) {
        holder.itemView.setOnLongClickListener(null)

        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    private var currentPosition = 0;

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
    }
}
