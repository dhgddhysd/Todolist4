package com.soyeon.todolist4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var todoList: ArrayList<Todo> = arrayListOf()
    var todoListAdapter: TodoAdapter? = null

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add_btn.setOnClickListener { view ->
            showAddListDialog()
        }

        initRecyclerView()
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.miItem1 -> Toast.makeText(applicationContext,
                        "Clicked Item 1", Toast.LENGTH_SHORT).show()
                R.id.miItem2 -> Toast.makeText(applicationContext,
                        "Clicked Item 2", Toast.LENGTH_SHORT).show()
                R.id.miItem3 -> Toast.makeText(applicationContext,
                        "Clicked Item 3", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun initRecyclerView() {

        todoListAdapter = TodoAdapter(initTodoData(), this)

        main_rv.layoutManager = LinearLayoutManager(applicationContext)
        main_rv.adapter = todoListAdapter
    }

    fun initTodoData(): ArrayList<Todo> {

        val dbHelper = DBHelper(this)
        dbHelper.onCreate(dbHelper.writableDatabase)
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from todolist", null)

        val count = cursor.count
        if (count >= 1) {
            while (cursor.moveToNext()) {
                val title = cursor.getString(0)
                todoList.add(Todo(title, false))
            }
        }
        cursor.close()

        return todoList
    }

    fun showAddListDialog() {

        val db = DBHelper(this).writableDatabase
        val titleEdit: EditText = EditText(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("할 일 추가")
        builder.setMessage("할 일을 적으세요.")
        builder.setView(titleEdit)
        builder.setPositiveButton("확인") { _, _ ->
            db.execSQL("insert into todolist values (\'${titleEdit.text}\')")
            Toast.makeText(this, "Todo 생성 완료", Toast.LENGTH_LONG).show()
            todoList.add(Todo(titleEdit.text.toString(), false))
            todoListAdapter!!.notifyItemInserted(todoList.size)
        }
        builder.setNegativeButton("취소") { _, _ -> }
        builder.show()
    }
}