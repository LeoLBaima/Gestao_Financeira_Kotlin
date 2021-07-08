package br.unifor.gfinanceira.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.adapter.ExpenseAdapter
import br.unifor.gfinanceira.adapter.ExpenseItemListener
import br.unifor.gfinanceira.adapter.RevenueAdapter
import br.unifor.gfinanceira.adapter.RevenueItemListener
import br.unifor.gfinanceira.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.E

class MainActivity : AppCompatActivity(), View.OnClickListener, ExpenseItemListener, RevenueItemListener{

    private lateinit var mRevenueList:RecyclerView;
    private lateinit var mExpenseList:RecyclerView;
    private lateinit var mUserWithDespesas: UserWithDespesas;
    private lateinit var mUserWithReceitas: UserWithReceitas;

    private lateinit var revenueAdapter:RevenueAdapter;
    private lateinit var expensesAdapter:ExpenseAdapter;

    private lateinit var mAddRevenue: FloatingActionButton;
    private lateinit var mAddExpense: FloatingActionButton;

    private val RevenueList = mutableListOf<Revenue>()
    private val ExpenseList = mutableListOf<Expense>()

    private var mUserId = -1;

    private lateinit var mAuth: FirebaseAuth;
    private lateinit var mDatabase: FirebaseDatabase;

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()

        mUserId = intent.getIntExtra("userId", -1);

        mRevenueList = findViewById(R.id.main_recyclerview_revenue)
        mExpenseList = findViewById(R.id.main_recyclerview_expenses);

        mAddRevenue = findViewById(R.id.main_floatingbutton_addrevenue);
        mAddExpense = findViewById(R.id.main_floatingbutton_addexpense);

        mAddRevenue.setOnClickListener(this);
        mAddExpense.setOnClickListener(this);

    }

    override fun onStart() {
        super.onStart()

        val progress = ProgressDialog(this);
        progress.setTitle("G Finanças - Aguarde")
        progress.isIndeterminate = true;
        progress.setMessage("Carregando os dados");
        progress.setCancelable(false);

        progress.show();

        val queryR = mDatabase.reference.child("users/${mAuth.uid}/revenues")
            .orderByKey().limitToLast(5)
        queryR.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss();

                RevenueList.clear();

                snapshot.children.forEach {
                    val revenue = it.getValue(Revenue::class.java)
                    RevenueList.add(revenue!!);
                }

                revenueAdapter = RevenueAdapter(RevenueList);
                revenueAdapter.setRevenueItemListener(this@MainActivity);
                val llm = LinearLayoutManager(applicationContext);

                handler.post{
                    mRevenueList.apply {
                        adapter = revenueAdapter;
                        layoutManager = llm;
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        val queryE = mDatabase.reference.child("users/${mAuth.uid}/expenses")
            .orderByKey().limitToLast(5)
        queryE.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss();
                ExpenseList.clear();

                snapshot.children.forEach {
                    val expense = it.getValue(Expense::class.java)
                    ExpenseList.add(expense!!);
                }

                expensesAdapter = ExpenseAdapter(ExpenseList);
                expensesAdapter.setExpenseItemListener(this@MainActivity);


                val llm = LinearLayoutManager(applicationContext);

                handler.post {
                    mExpenseList.apply {
                        adapter = expensesAdapter;
                        layoutManager = llm;
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.main_floatingbutton_addexpense -> {
                val it = Intent(applicationContext, DespesaFormActivity::class.java);
                startActivity(it);
            }
            R.id.main_floatingbutton_addrevenue -> {
                val it = Intent(applicationContext, ReceitaFormActivity::class.java);
                startActivity(it);
            }
        }
    }

    override fun onExpenseItemLongClick(v: View, pos: Int) {
        val expense = ExpenseList[pos];

        val alert = AlertDialog.Builder(this)
            .setTitle("Receita")
            .setMessage("Você deseja apagar essa despesa, ${expense.name}?")
            .setPositiveButton("Sim") {dialog, _ ->
                dialog.dismiss()
                val ref = mDatabase.reference.child("users/${mAuth.uid}/expenses/${ExpenseList[pos].did}")

                ref.removeValue().addOnCompleteListener {
                    handler.post {
                        expensesAdapter.notifyItemRemoved(pos)
                    }
                }
            }
            .setNegativeButton("Não") {dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
        alert.show()
    }

    override fun onRevenueItemLongClick(v: View, pos: Int) {

        val revenue = RevenueList[pos]

        val alert = AlertDialog.Builder(this)
            .setTitle("Receita")
            .setMessage("Você deseja apagar essa receita, ${revenue.name}?")
            .setPositiveButton("Sim") {dialog, _ ->
                dialog.dismiss()
                val ref = mDatabase.reference.child("users/${mAuth.uid}/revenues/${RevenueList[pos].rid}")

                ref.removeValue().addOnCompleteListener {
                    handler.post {
                        revenueAdapter.notifyItemRemoved(pos)
                    }
                }
            }
            .setNegativeButton("Não") {dialog, _ ->
                dialog.dismiss()
            }.setCancelable(false)
            .create()
        alert.show()
    }


}