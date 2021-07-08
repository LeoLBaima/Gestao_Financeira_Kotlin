package br.unifor.gfinanceira.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.unifor.gfinanceira.R
import br.unifor.gfinanceira.model.Revenue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DespesaFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mExpenseFormTitle: TextView;
    private lateinit var mExpenseFormName: EditText;
    private lateinit var mExpenseFormValue: EditText;
    private lateinit var mExpenseFormSave: Button;

    private var mUserID = -1;
    private var mExpenseId = -1;

    private val handler = Handler(Looper.getMainLooper());

    private lateinit var mAuth: FirebaseAuth;
    private lateinit var mDatabase: FirebaseDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesa)

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance()

        mUserID = intent.getIntExtra("userId", -1);
        mExpenseId = intent.getIntExtra("expenseId", -1);

        mExpenseFormTitle = findViewById(R.id.expense_form_textview_title);
        mExpenseFormName = findViewById(R.id.expense_form_edittext_name);
        mExpenseFormValue = findViewById(R.id.expense_form_edittext_value);

        mExpenseFormSave = findViewById(R.id.expense_form_button_save);
        mExpenseFormSave.setOnClickListener(this);

        if(mExpenseId != -1) {
            mExpenseFormTitle.text = Editable.Factory.getInstance().newEditable("EDITANDO DESPESA")
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.expense_form_button_save -> {
                val name = mExpenseFormName.text.toString();
                val value = mExpenseFormValue.text.toString();

                var isFormRightFilled = true;

                if(name.isEmpty()){
                    mExpenseFormName.error = "Este campo não pode ser vazio"
                    isFormRightFilled = false
                }
                if(value.isEmpty()) {
                    mExpenseFormValue.error = "Este campo não pode ser vazio"
                    isFormRightFilled = false
                }

                if(isFormRightFilled) {

                    val eid = mDatabase.reference.child("users/${mAuth.uid}/expenses").push().key
                    val expense = Revenue(eid!!, name, value.toInt())
                    val ref = mDatabase.getReference("users/${mAuth.uid}/expenses/$eid")
                    ref.setValue(expense);

                        handler.post {
                            Toast.makeText(
                                applicationContext,
                                "Tarefa ${expense.name} adicionada com sucesso",
                                Toast.LENGTH_SHORT).show();

                            finish();

                        }


                }







            }
        }
    }
}